package sk.zpn.zaklad.model.util.importeryDavky;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.Maps;
import sk.zpn.domena.importy.Davka;
import sk.zpn.domena.importy.ParametreImportu;
import sk.zpn.domena.importy.ZaznamCsv;
import sk.zpn.zaklad.grafickeNastroje.ProgressBarZPN;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DavkaCsvImporter {

    public DavkaCsvImporter() {

    }
    public static Davka nacitajCsvDavku(String suborCsv, ParametreImportu parametreImportu, ProgressBarZPN progressBarZPN) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(suborCsv), "windows-1250"),';');

        String [] nextLine;


        Davka davka=new Davka();
        Map<String, Integer> bodyNaIco = Maps.newHashMap();;


        Map<String, ZaznamCsv> polozky = Maps.newHashMap();
        progressBarZPN.zobraz();
        progressBarZPN.nadstavNadpis("Načítanie súboru");
        progressBarZPN.nadstavspustenie(true);

        reader.readNext();
        while ((nextLine = reader.readNext()) != null) {
            ZaznamCsv zaznam=new ZaznamCsv();
            progressBarZPN.posun(new BigDecimal(500),new BigDecimal(250));
            try {
                if (!nextLine[0].isEmpty() && nextLine[0]!=null && nextLine!=null) {

                    zaznam.setKit(nextLine[0]);
                    zaznam.setNazov(nextLine[1]);
                    zaznam.setMnozstvo(new BigDecimal(nextLine[2].replace(",", ".")));
                    zaznam.setNazvFirmy(nextLine[3]);
                    zaznam.setDatumVydaja(formatter.parse(nextLine[4]));
                    zaznam.setMtzDoklad(nextLine[5]);
                    zaznam.setIco(nextLine[6]);
                    zaznam.setMalopredaj(nextLine[7].contains("A"));
                    zaznam.setPcIco(Integer.parseInt(nextLine[8]));
                    zaznam.setCiarovyKod(nextLine[9]);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((zaznam !=null) && (zaznam.getKit()!=null)) {
                ZaznamCsv existujuci = polozky.get(zaznam.getKit() + zaznam.getMtzDoklad());
                if (existujuci ==null)
                    polozky.put(zaznam.getKit()+zaznam.getMtzDoklad(),zaznam );
                else
                    existujuci.setMnozstvo(existujuci.getMnozstvo().add(zaznam.getMnozstvo()));
                bodyNaIco.put(zaznam.getIco(), ImporterNastroje.vratBodyZaIcoAKit(zaznam.getIco(),
                        zaznam.getKit(),
                        zaznam.getMnozstvo(),
                        parametreImportu.getFirma(),
                        null));


            }

            //zaznam=null;
        }
        progressBarZPN.koniec();
        davka.setPolozky(polozky);
        davka.setBodyNaIco(bodyNaIco);
        return davka;

    }
    public static Davka nacitajCsvSpodosDavku(String suborCsv, ParametreImportu parametreImportu, ProgressBarZPN progressBarZPN) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(suborCsv), "windows-1250"),'\t');

        String [] nextLine;
        Map<String, ZaznamCsv> polozky = Maps.newHashMap();
        progressBarZPN.zobraz();
        progressBarZPN.nadstavNadpis("Načítanie súboru");
        progressBarZPN.nadstavspustenie(true);

        Davka davka=new Davka();
        Map<String, Integer> bodyNaIco = Maps.newHashMap();;


        reader.readNext();
        ZaznamCsv hlavicka=new ZaznamCsv();
        while ((nextLine = reader.readNext()) != null) {

            progressBarZPN.posun(new BigDecimal(500),new BigDecimal(250));
            try {
                if (!nextLine[0].isEmpty() && nextLine[0]!=null && nextLine!=null) {
                    if (nextLine[0].equals("R01")) {
                        hlavicka.setMtzDoklad(nextLine[1]);
                        hlavicka.setNazvFirmy(nextLine[2]);
                        hlavicka.setIco(nextLine[3]);
                        hlavicka.setDatumVydaja(formatter.parse(nextLine[4]));

                    }
                    else if ((nextLine[0].equals("R02")) &&(nextLine[5].equals("V"))){

                        ZaznamCsv zaznam=new ZaznamCsv();
                        zaznam.setMtzDoklad(hlavicka.getMtzDoklad());
                        zaznam.setNazvFirmy(hlavicka.getNazvFirmy());
                        zaznam.setIco(hlavicka.getIco());
                        zaznam.setDatumVydaja(hlavicka.getDatumVydaja());

                        zaznam.setMnozstvo(new BigDecimal(nextLine[2].replace(",", ".")));
                        zaznam.setKit(nextLine[17]);
                        zaznam.setNazov(nextLine[1]);
                        if ((zaznam !=null) && (zaznam.getKit()!=null)) {
                            ZaznamCsv existujuci = polozky.get(zaznam.getKit() + zaznam.getMtzDoklad());
                            if (existujuci ==null)
                                polozky.put(zaznam.getKit()+zaznam.getMtzDoklad(),zaznam );
                            else
                                existujuci.setMnozstvo(existujuci.getMnozstvo().add(zaznam.getMnozstvo()));
                        }

                        bodyNaIco.put(zaznam.getIco(), ImporterNastroje.vratBodyZaIcoAKit(zaznam.getIco(),
                                zaznam.getKit(),
                                zaznam.getMnozstvo(),
                                parametreImportu.getFirma(),
                                zaznam.getCiarovyKod()));


                    }

                    //zaznam.setMalopredaj(nextLine[7].contains("A"));
                    //zaznam.setPcIco(Integer.parseInt(nextLine[8]));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //zaznam=null;
        }
        progressBarZPN.koniec();
        davka.setPolozky(polozky);
        davka.setBodyNaIco(bodyNaIco);
        return davka;

    }
}
