package sk.zpn.domena;

import org.apache.log4j.Logger;
import java.util.Date;




public class ZaznamCsv

{
    private static final Logger logger = Logger.getLogger(Doklad.class);
    String scm;
    private String nazov;
    private Double mnozstvo;
    private String nazvFirmy;
    private Date datumVydaja;
    private String mtzDoklad;
    private String ico;
    private boolean malopredaj;
    private int pcIco;


    public int getPcIco() {
        return pcIco;
    }

    public void setPcIco(int pcIco) {
        this.pcIco = pcIco;
    }

    public boolean isMalopredaj() {
        return malopredaj;
    }

    public void setMalopredaj(boolean malopredaj) {
        this.malopredaj = malopredaj;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getMtzDoklad() {
        return mtzDoklad;
    }

    public void setMtzDoklad(String mtzDoklad) {
        this.mtzDoklad = mtzDoklad;
    }

    public Date getDatumVydaja() {
        return datumVydaja;
    }

    public void setDatumVydaja(Date datumVydaja) {
        this.datumVydaja = datumVydaja;
    }

    public String getNazvFirmy() {
        return nazvFirmy;
    }

    public void setNazvFirmy(String nazvFirmy) {
        this.nazvFirmy = nazvFirmy;
    }

    public Double getMnozstvo() {
        return mnozstvo;
    }

    public void setMnozstvo(Double mnozstvo) {
        this.mnozstvo = mnozstvo;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getScm() {
        return scm;
    }

    public void setScm(String scm) {
        this.scm = scm;
    }
}
