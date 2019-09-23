package sk.zpn.domena;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;

@Entity(name = "prevadzky")
@NamedQueries(value = {
        @NamedQuery(name = "Prevadzka.getPodlaNazvu", query = "SELECT p FROM prevadzky p WHERE p.nazov =:nazov order by p.nazov"),
        @NamedQuery(name = "Prevadzka.getPodlaNazvuLike", query = "SELECT p FROM prevadzky p where upper(p.nazov) like upper(:nazov) order by p.nazov"),
        @NamedQuery(name = "Prevadzka.getPodlaICAaNazvu", query = "SELECT p FROM prevadzky p " +
                "JOIN p.firma f " +
                "WHERE f.ico =:ico " +
                    " and upper(p.nazov) =:nazov"),
        @NamedQuery(name = "Prevadzka.getPodlaMenaPoberatela", query = "SELECT p FROM prevadzky p " +
                "JOIN p.poberatel pob " +
                "WHERE upper(pob.meno) =upper(:meno)"),
        @NamedQuery(name = "Prevadzka.getPrevadzkaPodlaICO", query = "SELECT p FROM prevadzky p " +
                "JOIN p.firma f " +
                "WHERE f.ico =:ico "),
        @NamedQuery(name = "Prevadzka.getPrevadzkyFirmy", query = "SELECT p FROM prevadzky p WHERE  p.firma=:firma order by p.nazov"),
        @NamedQuery(name = "Prevadzka.getAll", query = "SELECT p FROM prevadzky p order by p.nazov")})

public class Prevadzka extends Vseobecne {

    private String nazov;
    private String ulica;
    private String mesto;
    private String psc;


    @OneToOne(fetch = FetchType.LAZY, cascade = PERSIST)
    @JoinColumn(nullable = false)
    private Firma firma;

    @ManyToOne(fetch = FetchType.LAZY, cascade = PERSIST)
    @JoinColumn(nullable = true)
    private Poberatel poberatel;


    public Prevadzka() {

    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getPsc() {
        return psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public boolean isNew() {
        return this.getId() == null;
    }

    public Firma getFirma() {
        return firma;
    }

    public void setFirma(Firma firma) {
        this.firma = firma;
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



    public Poberatel getPoberatel() {
        return poberatel;
    }

    public void setPoberatel(Poberatel poberatel) {
        this.poberatel = poberatel;
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
            return poberatel.getMeno()+" "
                    +((poberatel.getMesto()==null)?"":poberatel.getMesto())+" "
                    +((poberatel.getPsc()==null)?"":poberatel.getPsc())+" "
                    +((poberatel.getUlica()==null)?"":poberatel.getUlica());
    }

    public Long getPoberatel_ID() {
        if (poberatel == null)
            return new Long(0);
        else
            return poberatel.getId();
    }


}


