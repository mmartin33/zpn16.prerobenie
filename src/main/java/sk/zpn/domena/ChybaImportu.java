package sk.zpn.domena;

public class ChybaImportu {
    String nazovFirmy;
    String icoFirmy;
    String kit;
    String chyba;

    public ChybaImportu() {
    }

    public ChybaImportu(String nazovFirmy, String icoFirmy, String kit, String chyba) {
        this.nazovFirmy = nazovFirmy;
        this.icoFirmy = icoFirmy;
        this.kit = kit;
        this.chyba = chyba;
    }

    public String getNazovFirmy() {
        return nazovFirmy;
    }

    public void setNazovFirmy(String nazovFirmy) {
        this.nazovFirmy = nazovFirmy;
    }

    public String getIcoFirmy() {
        return icoFirmy;
    }

    public void setIcoFirmy(String icoFirmy) {
        icoFirmy = icoFirmy;
    }

    public String getKit() {
        return kit;
    }

    public void setKit(String kit) {
        this.kit = kit;
    }

    public String getChyba() {
        return chyba;
    }

    public void setChyba(String chyba) {
        this.chyba = chyba;
    }
}