package sk.zpn.domena;

import javax.persistence.*;

import static javax.persistence.CascadeType.PERSIST;

@Entity(name = "ciarovyKod")
@Cacheable(false)


public class CiarovyKod extends Vseobecne {

    //@ManyToOne(fetch = FetchType.LAZY, cascade = PERSIST)
    @JoinColumn(nullable = false)
    private Produkt produkt;

    @JoinColumn(nullable = false)
    private String ciarovyKod;

    public Produkt getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    public String getCiarovyKod() {
        return ciarovyKod;
    }

    public void setCiarovyKod(String ciarovyKod) {
        this.ciarovyKod = ciarovyKod;
    }

    public String getTextLog() {
        String text=null;
        text=this.getId()+"Cd:"+this.getProdukt().getKat()+
                " Velkosklad:"+this.getProdukt().getNazov()+
                " Poberatel:"+this.getCiarovyKod();
        return text;

    }

}
