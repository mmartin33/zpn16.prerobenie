package sk.zpn.domena;

import java.util.List;

public class VysledokImportu {
    Doklad doklad;
    List<PolozkaDokladu> polozky;
    List<ChybaImportu> chyby;

    public VysledokImportu() {

    }

    public Doklad getDoklad() {
        return doklad;
    }

    public void setDoklad(Doklad doklad) {
        this.doklad = doklad;
    }

    public List<PolozkaDokladu> getPolozky() {
        return polozky;
    }

    public void setPolozky(List<PolozkaDokladu> polozky) {
        this.polozky = polozky;
    }

    public List<ChybaImportu> getChyby() {
        return chyby;
    }

    public void setChyby(List<ChybaImportu> chyby) {
        this.chyby = chyby;
    }
}