package sk.zpn.domena;



import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static javax.persistence.CascadeType.PERSIST;

@Entity(name = "produkty")
@NamedQueries(value = {
        @NamedQuery(name = "Produkt.getZaRok", query = "SELECT p FROM produkty p WHERE p.rok =:rok"),
        @NamedQuery(name = "Produkt.getPodlaNazvu", query = "SELECT p FROM produkty p WHERE p.nazov =:nazov"),
        @NamedQuery(name = "Produkt.getPodlaKodu", query = "SELECT p FROM produkty p WHERE p.kat =:kat"),

        @NamedQuery(name = "Produkt.getAll", query = "SELECT p FROM produkty p ")})

@Table(
        uniqueConstraints=
        @UniqueConstraint(columnNames={"rok", "kat"})
)

public class Produkt extends Vseobecne {


    @NotNull
    @Pattern(regexp = "[a-z0-9._%+-]$", message = "Zly kod")
    @Column(name = "kat", nullable = false,unique = true)

    private String kat;
    private String nazov;
    private Double body;
    private Double kusy;
    private String rok;




    @ManyToOne(fetch = FetchType.LAZY, cascade=PERSIST)
    @JoinColumn(nullable = true)





    private Firma firma;



    private Produkt produkt;

    public Produkt() {

    }
    public Produkt(String rok) {
        this.setRok(rok);
        this.setBody(new Double(1));
        this.setKusy(new Double(1));
    }

    public Produkt getProdukt() {return produkt;}

    public void setProdukt(Produkt produkt) {this.produkt = produkt;}

    public String getRok() {return rok;}

    public void setRok(String rok) {this.rok = rok;}

    public Double getKusy() {return kusy;}

    public void setKusy(Double kusy) {this.kusy = kusy;}

    public Double getBody() {return body;}

    public void setBody(Double body) {this.body = body;}

    public String getNazov() {return nazov;}

    public void setNazov(String nazov) {this.nazov = nazov;}

    public String getKat() {return kat;}

    public void setKat(String kat) {this.kat = kat;}

    public Firma getFirma() {return firma;}

    public void setFirma(Firma firma) {this.firma = firma;}

    public boolean isNew() {
        return this.getId() == null;
    }

    public String getFirmaNazov() {
        if (firma == null)
            return "";
        else
            return firma.getNazov();
    }


}





