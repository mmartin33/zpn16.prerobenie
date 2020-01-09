package sk.zpn.domena.prostredie;

import org.apache.log4j.Logger;
import sk.zpn.domena.Uzivatel;
import sk.zpn.domena.Vseobecne;

import javax.persistence.*;

import static javax.persistence.CascadeType.PERSIST;

@Entity(name = "gridy")
@Cacheable(false)
@NamedQueries(value = {
        @NamedQuery(name = "UlozenyGrid.get", query = "SELECT g FROM gridy g " +
                                                            " join g.uzivatel as u" +
                                                            " WHERE g.kluc =:kluc" +
                                                            " and g.typHodnotyStlpca=:typ_hodnoty" +
                                                            " and u.id=:id_uzivatela" +
                                                            " order by g.hodnota " ),
        @NamedQuery(name = "UlozenyStlpecGridu.get", query = "SELECT g FROM gridy g " +
                                                            " join g.uzivatel as u" +
                                                            " WHERE g.kluc =:kluc" +
                                                            " and g.typHodnotyStlpca=:typ_hodnoty" +
                                                            " and g.nazovStlpca=:nazov_stlpca" +
                                                            " and u.id=:id_uzivatela" )})



public class UlozenyGrid extends Vseobecne {
    private static final Logger logger = Logger.getLogger(UlozenyGrid.class);
    @ManyToOne(fetch = FetchType.LAZY, cascade=PERSIST)
    @JoinColumn(nullable = true)

    private Uzivatel uzivatel;
    @Column(name = "kluc", nullable = false)
    private String kluc;

    @Column(name = "nazov_stlpca", nullable = false)
    private String nazovStlpca;

    @Column(name = "hodnota", nullable = false)
    private Double hodnota;

    @Enumerated(EnumType.STRING)
    private TypHodnotyStlpca typHodnotyStlpca;




    public UlozenyGrid() {
    }

    public Uzivatel getUzivatel() {
        return uzivatel;
    }

    public void setUzivatel(Uzivatel uzivatel) {
        this.uzivatel = uzivatel;
    }

    public String getKluc() {
        return kluc;
    }

    public void setKluc(String kluc) {
        this.kluc = kluc;
    }

    public String getNazovStlpca() {
        return nazovStlpca;
    }

    public void setNazovStlpca(String nazovStlpca) {
        this.nazovStlpca = nazovStlpca;
    }

    public Double getHodnota() {
        return hodnota;
    }

    public void setHodnota(Double hodnota) {
        this.hodnota = hodnota;
    }


    public TypHodnotyStlpca getTypHodnotyStlpca() {
        return typHodnotyStlpca;
    }

    public void setTypHodnotyStlpca(TypHodnotyStlpca typHodnotyStlpca) {
        this.typHodnotyStlpca = typHodnotyStlpca;
    }
}
