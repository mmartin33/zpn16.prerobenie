package sk.zpn.domena.importy;

import sk.zpn.domena.Doklad;
import sk.zpn.domena.PolozkaDokladu;

import java.util.List;
import java.util.Map;

public class Davka {
    Map<String, Integer> bodyNaIco;
    Map<String, ZaznamCsv> polozky;

    public Davka() {

    }

    public Map<String, Integer> getBodyNaIco() {
        return bodyNaIco;
    }

    public void setBodyNaIco(Map<String, Integer> bodyNaIco) {
        this.bodyNaIco = bodyNaIco;
    }

    public Map<String, ZaznamCsv> getPolozky() {
        return polozky;
    }

    public void setPolozky(Map<String, ZaznamCsv> polozky) {
        this.polozky = polozky;
    }

}
