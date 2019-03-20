package sk.zpn.domena;

import java.io.Serializable;

public class FirmaProduktId implements Serializable {

    private Long firma;
    private Long produkt;

    public Long getFirma() {
        return firma;
    }

    public void setFirma(Long firma) {
        this.firma = firma;
    }

    public Long getProdukt() {
        return produkt;
    }

    public void setProdukt(Long produkt) {
        this.produkt = produkt;
    }
}
