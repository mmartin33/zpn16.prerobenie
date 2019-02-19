package sk.zpn.domena;



import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static javax.persistence.CascadeType.PERSIST;

@Entity(name = "mostik")
@NamedQueries(value = {
        @NamedQuery(name = "Mostik.getZaRokFirmu2", query = "SELECT m FROM mostik m "),//WHERE m.rok =:rok and m.firma_id =:idFirmy
        @NamedQuery(name = "Mostik.getZaRokFirmu", query = "SELECT m FROM mostik m "+
                                                                    "LEFT OUTER JOIN m.firma f "+
                                                                    "LEFT OUTER JOIN m.produkt p "+
                                                                        "where p.rok=:rok and m.firma.id=:IdFirmy " ),
        @NamedQuery(name = "Mostik.getAll", query = "SELECT m FROM mostik m ")})

//@Table(
//        uniqueConstraints=
//        @UniqueConstraint(columnNames={"rok", "kat"})
//)

public class Mostik extends Vseobecne {


    @NotNull
    @Pattern(regexp = "[a-z0-9._%+-]$", message = "Zly kod")
    @Column(name = "kit", nullable = false)
    private String kit;
    private String rok;


    @ManyToOne(fetch = FetchType.LAZY, cascade=PERSIST)
    @JoinColumn(nullable = true)
    private Firma firma;
    @ManyToOne(fetch = FetchType.LAZY, cascade=PERSIST)
    @JoinColumn(nullable = true)
    private Produkt produkt;




    private Mostik mostik;

    public Mostik() {

    }
    public Mostik(String rok) {
        this.setRok(rok);
    }

    public Mostik getMostik() {return mostik;}

    public void setMostik(Mostik mostik) {this.mostik = mostik;}

    public String getRok() {return rok;}

    public void setRok(String rok) {this.rok = rok;}

    public String getKit() {return kit;}

    public void setKit(String kat) {this.kit = kat;}

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
    public Produkt getProdukt() {return produkt;}

    public void setProdukt(Produkt produkt) {this.produkt = produkt;}



    public String getProduktKat() {
        if (produkt == null)
            return "";
        else
            return produkt.getKat();
    }


}





