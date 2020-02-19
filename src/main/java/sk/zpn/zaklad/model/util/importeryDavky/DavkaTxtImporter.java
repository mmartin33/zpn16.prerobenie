package sk.zpn.zaklad.model.util.importeryDavky;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import sk.zpn.domena.importy.Davka;
import sk.zpn.domena.importy.ParametreImportu;
import sk.zpn.domena.importy.ZaznamCsv;
import sk.zpn.zaklad.grafickeNastroje.ProgressBarZPN;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DavkaTxtImporter {

    public DavkaTxtImporter() {

    }
    public static Davka nacitajTxtDavku(String suborTxt, ParametreImportu parametreImportu, ProgressBarZPN progressBarZPN) throws IOException {
        //todo kontrola ci subor extuje
        //tuto

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        InputStreamReader ir= new InputStreamReader(new FileInputStream(suborTxt), "windows-1250");
        BufferedReader br = new BufferedReader(ir);

        String [] nextLine;
        Map<String, ZaznamCsv> polozky = Maps.newHashMap();
        progressBarZPN.zobraz();
        progressBarZPN.nadstavNadpis("Načítanie súboru");
        progressBarZPN.nadstavspustenie(true);

        Davka davka=new Davka();
        Map<String, Integer> bodyNaIco = Maps.newHashMap();;



        String strLine;


        while ((strLine = br.readLine()) != null)   {
            progressBarZPN.posun(new BigDecimal(500),new BigDecimal(250));
            if (!StringUtils.isEmpty(strLine)&& StringUtils.left(strLine,1).equals("V")) {
                ZaznamCsv zaznam = new ZaznamCsv();
                zaznam.setKit(StringUtils.trim(StringUtils.substring(strLine,2,18)));
                zaznam.setNazov(StringUtils.trim(StringUtils.substring(strLine,19,39)));
                    zaznam.setMnozstvo(new BigDecimal((StringUtils.trim(StringUtils.substring(strLine,39,51))).replace(",", ".")));
                    zaznam.setNazvFirmy(StringUtils.trim(StringUtils.substring(strLine,53,75)));
                try {
                    zaznam.setDatumVydaja(formatter.parse((StringUtils.trim(StringUtils.substring(strLine,75,85)))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                zaznam.setMtzDoklad(StringUtils.trim(StringUtils.substring(strLine,87,98)));
                zaznam.setIco(StringUtils.trim(StringUtils.substring(strLine,99,107)));
                if ((zaznam != null) && (zaznam.getKit() != null)) {
                    ZaznamCsv existujuci = polozky.get(zaznam.getKit() + zaznam.getMtzDoklad());
                    if (existujuci == null)
                        polozky.put(zaznam.getKit() + zaznam.getMtzDoklad(), zaznam);
                    else
                        existujuci.setMnozstvo(existujuci.getMnozstvo().add(zaznam.getMnozstvo()));
                    bodyNaIco.put(zaznam.getIco(), ImporterNastroje.vratBodyZaIcoAKit(zaznam.getIco(),
                            zaznam.getKit(),
                            zaznam.getMnozstvo(),
                            parametreImportu.getFirma(),
                            null));

                }
            }
        }
        br.close();
        ir.close();
        progressBarZPN.koniec();
        davka.setPolozky(polozky);
        davka.setBodyNaIco(bodyNaIco);
        return davka;
    }
}
