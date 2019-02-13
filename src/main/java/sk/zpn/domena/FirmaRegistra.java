package sk.zpn.domena;

public class FirmaRegistra {
    private String nazov;
    private String ico;
    private String obec;
    private String psc;
    private String ulica;
    private String cisloDomu;
    private String krajina;
    private String icDPH;
    private String dic;
    private boolean pravnickaOsoba =false;

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getObec() {
        return obec;
    }

    public void setObec(String obec) {
        this.obec = obec;
    }

    public String getPsc() {
        return psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getCisloDomu() {
        return cisloDomu;
    }

    public void setCisloDomu(String cisloDomu) {
        this.cisloDomu = cisloDomu;
    }

    public String getKrajina() {
        return krajina;
    }

    public void setKrajina(String krajina) {
        this.krajina = krajina;
    }

    public String getIcDPH() {
        return icDPH;
    }

    public void setIcDPH(String icDPH) {
        this.icDPH = icDPH;
    }

    public String getDic() {
        return dic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    public boolean isPravnickaOsoba() {
        return pravnickaOsoba;
    }

    public void setPravnickaOsoba(boolean pravnickaOsoba) {
        this.pravnickaOsoba = pravnickaOsoba;
    }
}
