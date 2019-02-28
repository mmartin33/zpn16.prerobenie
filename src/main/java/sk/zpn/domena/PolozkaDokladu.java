package sk.zpn.domena;

import org.apache.log4j.Logger;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

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
    private Firma firma;

    @ManyToOne(fetch = FetchType.LAZY, cascade=PERSIST)
    @JoinColumn(nullable = false)
    private Produkt produkt;

    private Double  body;
    private Double  mnozstvo;

    private String poznamka;


    public PolozkaDokladu() {
        this.setBody(new Double(1));
        this.setMnozstvo(new Double(1));
    }




    public Firma getFirma() {
        return firma;
    }

    public String getFirmaNazov() {
        if (firma == null)
            return "";
        else
            return firma.getNazov();
    }
    public Long getFirmaID() {
        if (firma == null)
            return new Long(0);
        else
            return firma.getId();
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
    public Long getPoberatelID() {
        if (poberatel == null)
            return new Long(0);
        else
            return poberatel.getId();
    }

    public void setFirma(Firma firma) {
        this.firma = firma;
    }

    public Double getBody() {return body;}

    public void setBody(Double body) {this.body = body;}

    public Double getMnozstvo() {return mnozstvo;}

    public void setMnozstvo(Double mnozstvo) {this.mnozstvo = mnozstvo;}

    public boolean isNew() {
        return this.getId() == null;
    }

    public String getPoznamka() {return poznamka;}

    public void setPoznamka(String poznamka) {this.poznamka = poznamka;}
}





