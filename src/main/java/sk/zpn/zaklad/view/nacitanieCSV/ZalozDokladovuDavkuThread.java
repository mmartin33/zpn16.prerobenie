package sk.zpn.zaklad.view.nacitanieCSV;

import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import sk.zpn.domena.*;
import sk.zpn.domena.importy.ChybaImportu;
import sk.zpn.domena.importy.NavratovaHodnota;
import sk.zpn.domena.importy.VysledokImportu;
import sk.zpn.domena.importy.ZaznamCsv;
import sk.zpn.zaklad.grafickeNastroje.ProgressBarZPN;
import sk.zpn.zaklad.model.DokladyNastroje;
import sk.zpn.zaklad.model.PolozkaDokladuNastroje;
import sk.zpn.zaklad.model.UzivatelNastroje;

import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.List;

class ZalozDokladovuDavkuThread extends Thread {
    private final List<ZaznamCsv> zaznam;
    private final String file;
    private final ProgressBarZPN progressBarZPN;
    private final VysledokImportu vysledok = new VysledokImportu();
    private final UI ui;
    private ActionListener koniecListener;

    ZalozDokladovuDavkuThread(List<ZaznamCsv> zaznam, String file, ProgressBarZPN progressBarZPN, UI ui) {
        this.zaznam = zaznam;
        this.file = file;
        this.progressBarZPN = progressBarZPN;
        this.ui = ui;
    }

    public void setKoniecListener(ActionListener listener) {
        koniecListener = listener;
    }

    @Override
    public void run() {
        List <ChybaImportu> chyby = new ArrayList<>();;
        progressBarZPN.nadstavNadpis("Zhranie dokladu");
        progressBarZPN.zobraz();
        //ui.access()
        Doklad hlavickaDokladu=new Doklad();

        String noveCisloDokladu = DokladyNastroje.noveCisloDokladu(null);


        if   (noveCisloDokladu==null || noveCisloDokladu.isEmpty()){
            chyby.add(new ChybaImportu(
                    "",
                    "",
                    "",
                    "Nepodarilo sa vygenerovat cislo dokladu",
                    ""));
            koniecListener.actionPerformed(null);
            return;
        }

        hlavickaDokladu.setCisloDokladu(noveCisloDokladu);
        hlavickaDokladu.setTypDokladu(TypDokladu.DAVKA);
        hlavickaDokladu.setDatum(new Date());
        hlavickaDokladu.setPoznamka(file);
        hlavickaDokladu.setStavDokladu(StavDokladu.NEPOTVRDENY);


        hlavickaDokladu.setFirma(UzivatelNastroje.getVlastnuFirmuPrihlasenehoUzivala());



        List<PolozkaDokladu> polozkyDokladu = new ArrayList<>();;



        int i=0;

        for (ZaznamCsv z: zaznam){
            i++;
           // progressBarZPN.posun(new BigDecimal(zaznam.size()),new BigDecimal(i));
            progressBarZPN.setProgresBarValue(new BigDecimal(i).divide(new BigDecimal(zaznam.size()),2,BigDecimal.ROUND_HALF_UP).floatValue());

            NavratovaHodnota navratovahodnota=PolozkaDokladuNastroje.vytvorPolozkuZoZaznamuCSV(z,hlavickaDokladu);

            //PolozkaDokladu pd=PolozkaDokladuNastroje.vytvorPolozkuZoZaznamuCSV(z,hlavickaDokladu);
            if (navratovahodnota.getPolozkaDokladu()!=null)
                polozkyDokladu.add(navratovahodnota.getPolozkaDokladu());
            else
                if (navratovahodnota.getChyba()==NavratovaHodnota.NENAJEDENA_FIRMA)
                chyby.add(new ChybaImportu(
                    z.getNazvFirmy(),
                    z.getIco(),
                    z.getKit(),
                    "Nepodarilo sa zalozit polozku dokladu (firma)",
                        z.getMtzDoklad()));

        }
        progressBarZPN.koniec();

        ulozDokladDavky(hlavickaDokladu,polozkyDokladu,progressBarZPN);

        vysledok.setDoklad(hlavickaDokladu);
        vysledok.setPolozky(polozkyDokladu);
        vysledok.setChyby(chyby);
        koniecListener.actionPerformed(null);
    }

    public VysledokImportu getVysledok() {
        return vysledok;
    }

    private static void ulozDokladDavky(Doklad hlavickaDokladu, List<PolozkaDokladu> polozkyDokladu, ProgressBarZPN progressBarZPN) {
        if (polozkyDokladu.size()==0)
            return;
        Doklad ulozenyDoklad;
        progressBarZPN.nadstavNadpis("ukladanie položiek dokladu");
        progressBarZPN.zobraz();
        ulozenyDoklad=DokladyNastroje.vytvorDoklad(hlavickaDokladu);
        int i=0;
        for (PolozkaDokladu polozka: polozkyDokladu){
            i++;
            progressBarZPN.posun(new BigDecimal(polozkyDokladu.size()),new BigDecimal(i));
            polozka.setDoklad(ulozenyDoklad);
            PolozkaDokladuNastroje.vytvorPolozkuDokladu(polozka);
        }
        progressBarZPN.koniec();





    }
}
