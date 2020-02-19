package sk.zpn.zaklad.model.util.importeryDavky;
import com.google.common.collect.Maps;
import com.linuxense.javadbf.DBFReader;
import org.apache.commons.lang.StringUtils;
import sk.zpn.domena.FirmaProdukt;
import sk.zpn.domena.importy.Davka;
import sk.zpn.domena.importy.ParametreImportu;
import sk.zpn.domena.importy.ZaznamCsv;
import sk.zpn.zaklad.grafickeNastroje.ProgressBarZPN;
import sk.zpn.zaklad.model.FirmaProduktNastroje;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.model.VypoctyUtil;

import java.io.*;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DavkaDbfImporter {




    public DavkaDbfImporter() {

    }
    public static Davka nacitajDbfDavku(String suborDBF, ParametreImportu parametreImportu, ProgressBarZPN progressBarZPN) throws IOException {
        File dbfFile = new File(suborDBF);

        DBFReader dbfReader = null;
        InputStream in = null;

        //todo kontrola ci subor extuje
        //tuto
        Map<String, ZaznamCsv> polozky = Maps.newHashMap();
        progressBarZPN.zobraz();
        progressBarZPN.nadstavNadpis("Načítanie súboru");
        progressBarZPN.nadstavspustenie(true);


        Davka davka=new Davka();
        Map<String, Integer> bodyNaIco =  Maps.newHashMap();


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
            zaznam.setCiarovyKod(StringUtils.trim((String) dbfZaznam[7]));
            zaznam.setMalopredaj(StringUtils.trim((String) dbfZaznam[2]).contains("A"));

            if ((zaznam != null) && (zaznam.getKit() != null)) {

                ZaznamCsv existujuci = polozky.get(zaznam.getKit() + zaznam.getMtzDoklad()+zaznam.getIco());
                if (existujuci == null)
                    polozky.put(zaznam.getKit() + zaznam.getMtzDoklad()+zaznam.getIco(), zaznam);
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
        in.close();
        davka.setPolozky(polozky);
        davka.setBodyNaIco(bodyNaIco);
        return davka;
    }
}
