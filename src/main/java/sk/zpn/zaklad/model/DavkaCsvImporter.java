package sk.zpn.zaklad.model;

import au.com.bytecode.opencsv.CSVReader;
import sk.zpn.domena.ZaznamCsv;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DavkaCsvImporter {

    public DavkaCsvImporter() {

    }
    public static List<ZaznamCsv> nacitajCsvDavku(String suborCsv) throws IOException {
        //todo kontrola ci subor extuje
        //tuto
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        CSVReader reader = new CSVReader(new FileReader(suborCsv),';');

        String [] nextLine;
        List<ZaznamCsv> davka =new ArrayList<ZaznamCsv>();;
        ZaznamCsv zaznam=new ZaznamCsv();
        reader.readNext();
        while ((nextLine = reader.readNext()) != null) {
            try {
                if (!nextLine[0].isEmpty() && nextLine[0]!=null && nextLine!=null) {
                    zaznam.setScm(nextLine[0]);
                    zaznam.setNazov(nextLine[1]);
                    zaznam.setMnozstvo(Double.parseDouble(nextLine[2].replace(",", ".")));
                    zaznam.setNazvFirmy(nextLine[3]);
                    zaznam.setDatumVydaja(formatter.parse(nextLine[4]));
                    zaznam.setMtzDoklad(nextLine[5]);
                    zaznam.setIco(nextLine[6]);
                    zaznam.setMalopredaj(nextLine[6].contains("A"));
                    zaznam.setPcIco(Integer.parseInt(nextLine[8]));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((zaznam !=null) && (zaznam.getScm()!=null))
                davka.add(zaznam);
            //zaznam=null;
        }

        return davka;
    }
}
