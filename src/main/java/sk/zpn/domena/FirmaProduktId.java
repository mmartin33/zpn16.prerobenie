package sk.zpn.domena;

import java.io.Serializable;

public class FirmaProduktId implements Serializable {

    private Long firma;
    private Long produkt;
    private String kit;

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

    public String getKit() {return kit;  }

    public void setKit(String kit) {this.kit = kit;}
}
