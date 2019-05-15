package sk.zpn.zaklad.model;

import au.com.bytecode.opencsv.CSVReader;
import sk.zpn.domena.ZaznamCsv;
import sk.zpn.zaklad.grafickeNastroje.ProgressBarZPN;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DavkaCsvImporter {

    public DavkaCsvImporter() {

    }
    public static List<ZaznamCsv> nacitajCsvDavku(String suborCsv, ProgressBarZPN progressBarZPN) throws IOException {
        //todo kontrola ci subor extuje
        //tuto
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(suborCsv), "windows-1250"),';');

        String [] nextLine;
        List<ZaznamCsv> davka =new ArrayList<ZaznamCsv>();;
        progressBarZPN.zobraz();
        progressBarZPN.nadstavNadpis("Načítanie súboru");
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
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((zaznam !=null) && (zaznam.getKit()!=null))
                davka.add(zaznam);
            //zaznam=null;
        }
        progressBarZPN.koniec();

        return davka;
    }
}
