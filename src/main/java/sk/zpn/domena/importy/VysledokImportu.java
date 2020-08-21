package sk.zpn.domena.importy;

import com.google.common.collect.Maps;
import sk.zpn.domena.Doklad;
import sk.zpn.domena.PolozkaDokladu;
import sk.zpn.domena.importy.ChybaImportu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class VysledokImportu {
    Doklad doklad;
    List<PolozkaDokladu> polozky;
    List<ChybaImportu> chyby;
    private Map<String, BigDecimal> nespracovaneKity;


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

    public void setNespracovaneKity(Map<String, BigDecimal> nespraovaneKity) {
        this.nespracovaneKity=nespraovaneKity;
    }

    public Map<String, BigDecimal> getNespracovaneKity() {
        return nespracovaneKity;
    }
}
