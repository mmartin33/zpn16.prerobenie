package sk.zpn.domena;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

@Entity(name = "produkty")
@Cacheable(false)
@NamedQueries(value = {
        @NamedQuery(name = "Produkt.getZaRok", query = "SELECT p FROM produkty p " +
                "WHERE p.rok =:rok " +
                "and p.typproduktu=sk.zpn.domena.TypProduktov.BODOVACI " +
                "order by p.nazov"),
        @NamedQuery(name = "Odmena.getZoznam", query = "SELECT p FROM produkty p " +
                "WHERE " +
                " p.typproduktu=sk.zpn.domena.TypProduktov.ODMENA " +
                "order by p.nazov"),
        @NamedQuery(name = "Produkt.getZaRokZaDodavatela", query = "SELECT p FROM produkty p " +
                " join p.firma as f" +
                " where f.id=:id " +
                " and p.rok =:rok " +
                " order by p.kat"),
        @NamedQuery(name = "Produkt.getPodlaNazvu", query = "SELECT p FROM produkty p " +
                " WHERE p.nazov =:nazov" +
                " and p.typproduktu=sk.zpn.domena.TypProduktov.BODOVACI " +
                " and p.rok =:rok"),
        @NamedQuery(name = "Produkt.getPodlaKodu", query = "SELECT p FROM produkty p " +
                " WHERE p.kat =:kat" +
                " and p.typproduktu=sk.zpn.domena.TypProduktov.BODOVACI " +
                " and p.rok =:rok"),
        @NamedQuery(name = "Odmena.getPodlaNazvu", query = "SELECT p FROM produkty p " +
                " WHERE p.nazov =:nazov" +
                " and p.typproduktu=sk.zpn.domena.TypProduktov.ODMENA"),
        @NamedQuery(name = "Odmena.getPodlaKodu", query = "SELECT p FROM produkty p " +
                " WHERE p.kat =:kat" +
                " and p.typproduktu=sk.zpn.domena.TypProduktov.ODMENA"),
        @NamedQuery(name = "Produkt.getPodlaID", query = "SELECT p FROM produkty p WHERE p.id =:id"),
        @NamedQuery(name = "Produkt.getAll", query = "SELECT p FROM produkty p ")})

@Table(
        uniqueConstraints=
        @UniqueConstraint(columnNames={"rok", "kat"})
)

public class Produkt extends Vseobecne {

    @NotNull
    @Pattern(regexp = "[a-z0-9._%+-]$", message = "Zly kod")
    @Column(name = "kat", nullable = false)
    private String kat;

    @OneToMany(mappedBy = "produkt")
    private List<FirmaProdukt> firmaMostik;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Firma firma;


    private String nazov;
    private BigDecimal body;
    private BigDecimal kusy;
    private BigDecimal cena;
    private String rok;

    @Enumerated(EnumType.STRING)
    private TypProduktov typproduktu;


    public Produkt() {}

    public String getRok() {return rok;}

    public Produkt setRok(String rok) {
        this.rok = rok;
        return this;
    }

    public BigDecimal getKusy() {return kusy;}

    public BigInteger getKusyBigInteger() {return kusy.toBigInteger();}

    public Produkt setKusy(BigDecimal kusy) {
        this.kusy = kusy;
        return this;
    }

    public BigDecimal getBody() {return body;}

    public BigInteger getBodyBigInteger() {
        if (body==null)
            return BigInteger.ZERO;
        else
            return body.toBigInteger();}

    public Produkt setBody(BigDecimal body) {
        this.body = body;
        return this;
    }

    public String getNazov() {
        if (this==null)
            return "";
        return nazov;}

    public Produkt setNazov(String nazov) {
        this.nazov = nazov;
        return this;
    }

    public String getKat() {
        if (this==null)
            return "";

        return kat;}

    public Produkt setKat(String kat) {
        this.kat = kat;
        return this;
    }

    public boolean isNew() {
        return this.getId() == null;
    }


    public static String getToolTip(Produkt produkt) {
         {
            if (produkt.getKedy()==null)
                return "";
            Format formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String s = formatter.format(produkt.getKedy());
            return s;
        }




//        return produkt.getKedy().toString();
    }

    public Firma getFirma() {
        return firma;
    }

    public void setFirma(Firma firma) {
        this.firma = firma;
    }

    public String  getFirmaNazov() {
        if (firma == null)
            return "";
        else
            return firma.getNazov();

    }

    public TypProduktov getTypProduktov() {
        return typproduktu;
    }

    public Produkt setTypProduktov(TypProduktov typProduktov) {
        this.typproduktu = typProduktov;
        return this;
    }

    public BigDecimal getCena() {

        return (cena==null?new BigDecimal(0) :cena);
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }
}





