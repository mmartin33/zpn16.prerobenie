package sk.zpn.domena;

import org.apache.log4j.Logger;

import javax.persistence.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import static javax.persistence.CascadeType.PERSIST;

@Entity(name = "polozkyDokladu")
@Cacheable(false)
@NamedQueries(value = {
        @NamedQuery(name = "PolozkaDokladu.getAll", query = "SELECT d FROM polozkyDokladu d"),
        @NamedQuery(name = "PolozkaDokladu.getPolozkyJednehoDokladu", query = "SELECT d FROM polozkyDokladu d where d.doklad=:doklad"),
        @NamedQuery(name = "PolozkaDokladu.getPolozkyPoberatela", query = "SELECT p,d,pob,prev,f,kat FROM polozkyDokladu p " +
                " join p.doklad as d " +
                " join p.poberatel as pob " +
                " join d.firma as f " +
                " left join p.prevadzka as prev " +
                " left join p.produkt as kat " +
                " where pob.id=:id " +
                " order by d.datum "),
        @NamedQuery(name = "PolozkaDokladu.getPoberateliaVelkoskladu", query = "SELECT pob FROM polozkyDokladu p " +
                " join p.doklad as d " +
                " join p.poberatel as pob " +
                " join d.firma as f " +
                " where  f.id=:id " +
                " and d.stavDokladu=sk.zpn.domena.StavDokladu.POTVRDENY "+
                "group by pob "),
        @NamedQuery(name = "PolozkaDokladu.get", query = "SELECT d FROM polozkyDokladu d WHERE d.id =:id")})

public class PolozkaDokladu extends Vseobecne {

    private static final Logger logger = Logger.getLogger(PolozkaDokladu.class);

    @OneToOne(fetch = FetchType.LAZY, cascade = PERSIST)
    @JoinColumn(nullable = false)
    private Doklad doklad;

    @ManyToOne(fetch = FetchType.LAZY, cascade = PERSIST)
    @JoinColumn(nullable = false)
    private Poberatel poberatel;

    @ManyToOne(fetch = FetchType.LAZY, cascade = PERSIST)
    @JoinColumn(nullable = true)
    private Prevadzka prevadzka;

    @ManyToOne(fetch = FetchType.LAZY, cascade = PERSIST)
    @JoinColumn(nullable = true)
    private Produkt produkt;

    private BigDecimal body;
    private BigDecimal mnozstvo;
    private BigDecimal mnozstvoPovodne;
    private String kit;

    private String poznamka;


    public PolozkaDokladu() {
        this.setBody(new BigDecimal(1));
        this.setMnozstvo(new BigDecimal(1));
    }


    public String getKit() {
        return kit;
    }

    public void setKit(String kit) {
        this.kit = kit;
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

    public BigDecimal getMnozstvoPovodne() {
        return mnozstvoPovodne;
    }

    public void setMnozstvoPovodne(BigDecimal mnozstvoPovodne) {
        if (mnozstvoPovodne == null)
            mnozstvoPovodne = this.mnozstvo;
        this.mnozstvoPovodne = mnozstvoPovodne;
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
            return poberatel.getMeno() +

                    " " + (poberatel.getMesto() == null ? "" : poberatel.getMesto()) +
                    " " + (poberatel.getPsc() == null ? "" : poberatel.getPsc()) +
                    " " + (poberatel.getUlica() == null ? "" : poberatel.getUlica());
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

    public BigDecimal getBody() {
        return body;
    }

    public void setBody(BigDecimal body) {
        this.body = body;
    }

    public BigInteger getBodyUpraveneBigInteger() {
        if (this != null) {
            if (this.getProdukt() != null) {
                if (this.getProdukt().getTypProduktov() == TypProduktov.ODMENA)
                    return body.negate().toBigInteger();
                    //                  return body.toBigInteger();
                else
                    return body.toBigInteger();
            }
        }
        return body.toBigInteger();
    }
public BigInteger getBodyBigInteger() {
        if (this != null) {
            if (this.getProdukt() != null) {
                    return body.toBigInteger();
            }
        }
        return body.toBigInteger();
    }


    public BigDecimal getMnozstvo() {
        return mnozstvo;
    }

    public BigInteger getMnozstvoBigInteger() {
        if (this != null) {
            if ((this.getMnozstvo() != null) && (this.getProdukt()!=null)){
                if (this.getProdukt().getTypProduktov() == TypProduktov.ODMENA)
                    return mnozstvo.negate().toBigInteger();
                else
                    return mnozstvo.toBigInteger();
            }
        }

        if (mnozstvo == null)
            return BigInteger.ZERO;
        else
            return mnozstvo.toBigInteger();
    }

    public void setMnozstvo(BigDecimal mnozstvo) {
        this.mnozstvo = mnozstvo;
    }

    public boolean isNew() {
        return this.getId() == null;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

    public static BigInteger getBodyZaProduktBigInteger(PolozkaDokladu polozkaDokladu) {
        return polozkaDokladu.getProdukt().getBodyBigInteger();
    }

    public static String getBodyZaProdukt(PolozkaDokladu polozkaDokladu) {
        if (polozkaDokladu.getProdukt() == null)
            return "-/-";
        else
            return polozkaDokladu.getProdukt().getBodyBigInteger().toString() +
                    "/" +
                    polozkaDokladu.getProdukt().getKusyBigInteger().toString();

    }
}





