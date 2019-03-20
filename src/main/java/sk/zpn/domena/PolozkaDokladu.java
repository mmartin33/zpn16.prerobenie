package sk.zpn.domena;

import org.apache.log4j.Logger;

import javax.persistence.*;

import java.math.BigDecimal;

import static javax.persistence.CascadeType.PERSIST;

@Entity(name = "polozkyDokladu")
@NamedQueries(value = {
        @NamedQuery(name = "PolozkaDokladu.getAll", query = "SELECT d FROM polozkyDokladu d"),
        @NamedQuery(name = "PolozkaDokladu.getPolozkyJednehoDokladu", query = "SELECT d FROM polozkyDokladu d where d.doklad=:doklad"),
        @NamedQuery(name = "PolozkaDokladu.get", query = "SELECT d FROM polozkyDokladu d WHERE d.id =:id")})

public class PolozkaDokladu extends Vseobecne {

    private static final Logger logger = Logger.getLogger(PolozkaDokladu.class);

    @OneToOne(fetch = FetchType.LAZY, cascade=PERSIST)
    @JoinColumn(nullable = false)
    private Doklad doklad;

    @ManyToOne(fetch = FetchType.LAZY, cascade=PERSIST)
    @JoinColumn(nullable = false)
    private Poberatel poberatel;

    @ManyToOne(fetch = FetchType.LAZY, cascade=PERSIST)
    @JoinColumn(nullable = false)
    private Prevadzka prevadzka;

    @ManyToOne(fetch = FetchType.LAZY, cascade=PERSIST)
    @JoinColumn(nullable = false)
    private Produkt produkt;

    private BigDecimal body;
    private BigDecimal  mnozstvo;

    private String poznamka;


    public PolozkaDokladu() {
        this.setBody(new BigDecimal(1));
        this.setMnozstvo(new BigDecimal(1));
    }




    public Prevadzka getPrevadzka() {
        return prevadzka;
    }

    public String getPrevadzkaNazov() {
        if (prevadzka == null)
            return "";
        else
            return prevadzka.getNazov();
    }
    public Long getPrevadzkaID() {
        if (prevadzka == null)
            return new Long(0);
        else
            return prevadzka.getId();
    }


    public Doklad getDoklad() {
        return doklad;
    }

    public void setDoklad(Doklad doklad) {
        this.doklad = doklad;
    }

    public String getDokladCislo() {
        if (doklad == null)
            return "";
        else
            return doklad.getCisloDokladu();
    }
    public Long getDokladID() {
        if (doklad == null)
            return new Long(0);
        else
            return doklad.getId();
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }
    public Produkt getProdukt() {
        return produkt;
    }


    public String getProduktNazov() {
        if (produkt == null)
            return "";
        else
            return produkt.getNazov();
    }
    public String getProduktKod() {
        if (produkt == null)
            return "";
        else
            return produkt.getKat();
    }
    public Long getProduktID() {
        if (produkt == null)
            return new Long(0);
        else
            return produkt.getId();
    }




    public void setPoberatel(Poberatel poberatel) {
        this.poberatel = poberatel;
    }
    public Poberatel getPoberatel() {
        return poberatel;
    }


    public String getPoberatelMeno() {
        if (poberatel == null)
            return "";
        else
            return poberatel.getMeno();
    }

    public String getPoberatelMenoAdresa() {
        if (poberatel == null)
            return "";
        else
            return poberatel.getMeno()+" "+poberatel.getMesto()+" "+poberatel.getPsc()+" "+poberatel.getUlica();
    }

    public Long getPoberatelID() {
        if (poberatel == null)
            return new Long(0);
        else
            return poberatel.getId();
    }

    public void setPrevadzka(Prevadzka prevadzka) {
        this.prevadzka = prevadzka;
    }

    public BigDecimal getBody() {return body;}

    public void setBody(BigDecimal body) {this.body = body;}

    public BigDecimal getMnozstvo() {return mnozstvo;}

    public void setMnozstvo(BigDecimal mnozstvo) {this.mnozstvo = mnozstvo;}

    public boolean isNew() {
        return this.getId() == null;
    }

    public String getPoznamka() {return poznamka;}

    public void setPoznamka(String poznamka) {this.poznamka = poznamka;}
}





