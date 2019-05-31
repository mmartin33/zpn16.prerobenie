package sk.zpn.zaklad.model.util.importeryDavky;
import com.linuxense.javadbf.DBFReader;
import org.apache.commons.lang.StringUtils;
import sk.zpn.domena.importy.ZaznamCsv;
import sk.zpn.zaklad.grafickeNastroje.ProgressBarZPN;
import java.io.*;
import java.math.BigDecimal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DavkaDbfImporter {




    public DavkaDbfImporter() {

    }
    public static List<ZaznamCsv> nacitajDbfDavku(String suborDBF, ProgressBarZPN progressBarZPN) throws IOException {
        File dbfFile = new File(suborDBF);

        DBFReader dbfReader = null;
        InputStream in = null;

        //todo kontrola ci subor extuje
        //tuto

        List<ZaznamCsv> davka = new ArrayList<ZaznamCsv>();
        ;
        progressBarZPN.zobraz();
        progressBarZPN.nadstavNadpis("Načítanie súboru");
        progressBarZPN.nadstavspustenie(true);


        in = new BufferedInputStream(new FileInputStream(dbfFile));
        dbfReader = new DBFReader(in);
        //DBFHeader  row = null;
        for (int i = 0; i < dbfReader.getRecordCount(); i++) {
            ZaznamCsv zaznam = new ZaznamCsv();
            progressBarZPN.posun(new BigDecimal(500), new BigDecimal(250));
            Object[] dbfZaznam = dbfReader.nextRecord();

            zaznam.setDatumVydaja((Date)dbfZaznam[0]);
            zaznam.setMtzDoklad((String) dbfZaznam[1]);
            zaznam.setKit(StringUtils.trim((String) dbfZaznam[2]));
            zaznam.setMnozstvo(BigDecimal.valueOf(((Double) dbfZaznam[3]).doubleValue()));
            zaznam.setIco(StringUtils.trim((String) dbfZaznam[4]));
            zaznam.setNazvFirmy(StringUtils.trim((String) dbfZaznam[6]));
            zaznam.setMalopredaj(StringUtils.trim((String) dbfZaznam[2]).contains("A"));

            if ((zaznam != null) && (zaznam.getKit() != null))
                davka.add(zaznam);
            //zaznam=null;
        }
        in.close();
        return davka;
    }
}
