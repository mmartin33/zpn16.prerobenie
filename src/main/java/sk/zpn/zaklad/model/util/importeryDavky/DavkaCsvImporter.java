package sk.zpn.zaklad.model.util.importeryDavky;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.Maps;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import sk.zpn.domena.FirmaProdukt;
import sk.zpn.domena.importy.Davka;
import sk.zpn.domena.importy.ParametreImportu;
import sk.zpn.domena.importy.ZaznamCsv;
import sk.zpn.zaklad.grafickeNastroje.ProgressBarZPN;
import sk.zpn.zaklad.model.FirmaProduktNastroje;
import sk.zpn.zaklad.model.ParametreNastroje;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class DavkaCsvImporter {

    private int spustit = 0;
    public static int SPUSTIT_DAVKU_SIMO = 1;
    private String file;
    private ProgressBarZPN progresBarZPN;
    private ParametreImportu parametreImportu;
    private Davka davka;

    public DavkaCsvImporter() {

    }

//    @Override
//    public void interrupt() {
//        super.interrupt();
//    }
//
//    @Override
//    public void run() {
//        super.run();
//
//        try {
//
//
//            if (spustit == SPUSTIT_DAVKU_SIMO) {
//              davka= this.nacitajCsvDavkuSimo(file, parametreImportu, progresBarZPN);
//
//
//
//            }
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//

    public static Davka nacitajCsvDavku(String suborCsv, ParametreImportu parametreImportu, ProgressBarZPN progressBarZPN) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(suborCsv), "windows-1250"), ';');

        String[] nextLine;
        String[] hlavicka;


        Davka davka = new Davka();
        Map<String, Integer> bodyNaIco = Maps.newHashMap();


        Map<String, FirmaProdukt> katKit = Maps.newHashMap();
        katKit = FirmaProduktNastroje.zoznamKatKitovZVelkosklad(parametreImportu.getFirma(),
                ParametreNastroje.nacitajParametre().getRok());


        Map<String, Integer> katEan = Maps.newHashMap();
        katEan = FirmaProduktNastroje.zoznamKatEanZVelkosklad(parametreImportu.getFirma(),
                ParametreNastroje.nacitajParametre().getRok());


        Map<String, ZaznamCsv> polozky = Maps.newHashMap();
        progressBarZPN.zobraz();
        progressBarZPN.nadstavNadpis("Načítanie súboru");
        progressBarZPN.nadstavspustenie(true);

        hlavicka = reader.readNext();
        while ((nextLine = reader.readNext()) != null) {
            ZaznamCsv zaznam = new ZaznamCsv();
            progressBarZPN.posun(new BigDecimal(500), new BigDecimal(250));
            try {
                if (!nextLine[0].isEmpty() && nextLine[0] != null && nextLine != null) {

                    zaznam.setKit(nextLine[0]);
                    zaznam.setNazov(nextLine[1]);
                    zaznam.setMnozstvo(new BigDecimal(nextLine[2].replace(",", ".")));
                    zaznam.setNazvFirmy(nextLine[3]);
                    zaznam.setDatumVydaja(((nextLine[4] != null && nextLine[4].toString().trim().length() > 0) ? formatter.parse(nextLine[4]) : null));
                    zaznam.setMtzDoklad(nextLine[5]);
                    zaznam.setIco(nextLine[6]);
                    zaznam.setMalopredaj(nextLine[7].contains("A"));
                    //zaznam.setPcIco(Integer.parseInt(nextLine[8]));
                    if (nextLine.length > 9)
                        zaznam.setCiarovyKod(nextLine[9]);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
                        null, katKit));


            }

            //zaznam=null;
        }
        progressBarZPN.koniec();
        davka.setPolozky(polozky);
        davka.setBodyNaIco(bodyNaIco);
        davka.setKatKit(katKit);
        return davka;

    }

    public static Davka nacitajCsvDavkuSimo(String suborCsv, ParametreImportu parametreImportu, ProgressBarZPN progressBarZPN) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(suborCsv), "windows-1250"), ';');

        String[] nextLine;
        String[] hlavicka;


        Davka davka = new Davka();
        Map<String, Integer> bodyNaIco = Maps.newHashMap();

        Map<String, FirmaProdukt> katKit = Maps.newHashMap();
        katKit = FirmaProduktNastroje.zoznamKatKitovZVelkosklad(parametreImportu.getFirma(),
                ParametreNastroje.nacitajParametre().getRok());


        Map<String, ZaznamCsv> polozky = Maps.newHashMap();
        progressBarZPN.zobraz();
        progressBarZPN.nadstavNadpis("Načítanie súboru");
        progressBarZPN.nadstavspustenie(true);

        hlavicka = reader.readNext();
        int i = 0;
        while ((nextLine = reader.readNext()) != null) {
            i++;
            ZaznamCsv zaznam = new ZaznamCsv();
            progressBarZPN.posun(new BigDecimal(500), new BigDecimal(250));

            if (!nextLine[0].isEmpty() && nextLine[0] != null && nextLine != null) {

                zaznam.setKit(nextLine[0]);
                zaznam.setNazov(nextLine[1]);
                zaznam.setMnozstvo(new BigDecimal(nextLine[2].replace(",", ".")));
                zaznam.setNazvFirmy(nextLine[3]);
                zaznam.setIco(nextLine[6]);
                zaznam.setMtzDoklad(nextLine[5]);
                //zaznam.setPcIco(Integer.parseInt(nextLine[8]));
            }
//            else
//                System.out.println(nextLine);

            if ((zaznam != null) && (zaznam.getKit() != null)) {



                ZaznamCsv existujuci = polozky.get(zaznam.getKit() + zaznam.getMtzDoklad());



                    if ((existujuci == null)) {

                        polozky.put(zaznam.getKit() + zaznam.getMtzDoklad(), zaznam);
                    } else {
                        existujuci.setMnozstvo(existujuci.getMnozstvo().add(zaznam.getMnozstvo()));
                    }

                bodyNaIco.put(zaznam.getIco(), ImporterNastroje.vratBodyZaIcoAKit(zaznam.getIco(),
                        zaznam.getKit(),
                        zaznam.getMnozstvo(),
                        parametreImportu.getFirma(),
                        null, null));
            }
//            else {
//                System.out.println(nextLine);
//            }


            //zaznam=null;
        }
        System.out.println("ZPN - pociet riadkov " + i);

        progressBarZPN.koniec();
        davka.setKatKit(katKit);
        davka.setPolozky(polozky);
        davka.setBodyNaIco(bodyNaIco);
        return davka;

    }

    public static Davka nacitajCsvDavkuBecica(String suborCsv, ParametreImportu parametreImportu, ProgressBarZPN progressBarZPN) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(suborCsv), "windows-1250"), ';');

        String[] nextLine;
        String[] hlavicka;


        Davka davka = new Davka();
        Map<String, Integer> bodyNaIco = Maps.newHashMap();
        Map<String, FirmaProdukt> katKit = Maps.newHashMap();
        katKit = FirmaProduktNastroje.zoznamKatKitovZVelkosklad(parametreImportu.getFirma(),
                ParametreNastroje.nacitajParametre().getRok());


        Map<String, ZaznamCsv> polozky = Maps.newHashMap();
        progressBarZPN.zobraz();
        progressBarZPN.nadstavNadpis("Načítanie súboru");
        progressBarZPN.nadstavspustenie(true);

        hlavicka = reader.readNext();
        int i=0;
        int j=0;
        while ((nextLine = reader.readNext()) != null) {
            i++;
            j++;
            if (j==500){
                j=1;
                System.out.println("Zaznamy CSV Becica"+i);
            }

//            System.out.println("zaznam:"+i);
            ZaznamCsv zaznam = new ZaznamCsv();
            progressBarZPN.posun(new BigDecimal(500), new BigDecimal(250));

            if (!nextLine[0].isEmpty() && nextLine[0] != null && nextLine != null) {

                zaznam.setKit(nextLine[0]);
                zaznam.setNazov(nextLine[1]);
                zaznam.setIco(nextLine[2]);
                zaznam.setMtzDoklad(nextLine[3]);
                try {
                    zaznam.setDatumVydaja(((nextLine[4] != null && nextLine[4].toString().trim().length() > 0) ? formatter.parse(nextLine[4]) : null));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                zaznam.setMnozstvo(new BigDecimal(nextLine[5].replace(",", ".")));
                zaznam.setNazvFirmy(nextLine[2]);

                //zaznam.setPcIco(Integer.parseInt(nextLine[8]));
            }
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
                        null, null));


            }

            //zaznam=null;
        }
        System.out.println("Pocet CSV zaznamov"+i);
        progressBarZPN.koniec();
        davka.setKatKit(katKit);
        davka.setPolozky(polozky);
        davka.setBodyNaIco(bodyNaIco);
        return davka;

    }

    public static Davka nacitajCsvSpodosDavku(String suborCsv, ParametreImportu parametreImportu, ProgressBarZPN progressBarZPN) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(suborCsv), "windows-1250"), '\t');

        String[] nextLine;
        Map<String, ZaznamCsv> polozky = Maps.newHashMap();
        progressBarZPN.zobraz();
        progressBarZPN.nadstavNadpis("Načítanie súboru");
        progressBarZPN.nadstavspustenie(true);

        Davka davka = new Davka();
        Map<String, Integer> bodyNaIco = Maps.newHashMap();
        Map<String, FirmaProdukt> katKit = Maps.newHashMap();
        katKit = FirmaProduktNastroje.zoznamKatKitovZVelkosklad(parametreImportu.getFirma(),
                ParametreNastroje.nacitajParametre().getRok());


        reader.readNext();
        ZaznamCsv hlavicka = new ZaznamCsv();
        while ((nextLine = reader.readNext()) != null) {

            progressBarZPN.posun(new BigDecimal(500), new BigDecimal(250));
            try {
                if (!nextLine[0].isEmpty() && nextLine[0] != null && nextLine != null) {
                    if (nextLine[0].equals("R01")) {
                        hlavicka.setMtzDoklad(nextLine[1]);
                        hlavicka.setNazvFirmy(nextLine[2]);
                        hlavicka.setIco(nextLine[3]);
                        hlavicka.setDatumVydaja(formatter.parse(nextLine[4]));

                    } else if ((nextLine[0].equals("R02")) && (nextLine[5].equals("V"))) {

                        ZaznamCsv zaznam = new ZaznamCsv();
                        zaznam.setMtzDoklad(hlavicka.getMtzDoklad());
                        zaznam.setNazvFirmy(hlavicka.getNazvFirmy());
                        zaznam.setIco(hlavicka.getIco());
                        zaznam.setDatumVydaja(hlavicka.getDatumVydaja());

                        zaznam.setMnozstvo(new BigDecimal(nextLine[2].replace(",", ".")));
                        zaznam.setKit(nextLine[17]);
                        zaznam.setNazov(nextLine[1]);
                        if ((zaznam != null) && (zaznam.getKit() != null)) {
                            ZaznamCsv existujuci = polozky.get(zaznam.getKit() + zaznam.getMtzDoklad());
                            if (existujuci == null)
                                polozky.put(zaznam.getKit() + zaznam.getMtzDoklad(), zaznam);
                            else
                                existujuci.setMnozstvo(existujuci.getMnozstvo().add(zaznam.getMnozstvo()));
                        }

                        bodyNaIco.put(zaznam.getIco(), ImporterNastroje.vratBodyZaIcoAKit(zaznam.getIco(),
                                zaznam.getKit(),
                                zaznam.getMnozstvo(),
                                parametreImportu.getFirma(),
                                zaznam.getCiarovyKod(), null));


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
        davka.setKatKit(katKit);
        return davka;

    }

    public void nastavParametre(int kluc, String file, ParametreImportu parametreImportu, ProgressBarZPN progressBarZPN, Davka davka) {
        this.spustit = kluc;
        this.file = file;
        this.parametreImportu = parametreImportu;
        this.progresBarZPN = progressBarZPN;
        this.davka = davka;
    }
}
