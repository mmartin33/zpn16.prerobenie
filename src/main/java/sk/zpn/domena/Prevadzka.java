package sk.zpn.domena;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;

@Entity(name = "prevadzky")
@NamedQueries(value = {
        @NamedQuery(name = "Prevadzka.getPodlaNazvu", query = "SELECT p FROM prevadzky p WHERE p.nazov =:nazov"),
        @NamedQuery(name = "Prevadzka.getPrevadzkyFirmy", query = "SELECT p FROM prevadzky p WHERE  p.firma=:firma"),
        @NamedQuery(name = "Prevadzka.getAll", query = "SELECT p FROM prevadzky p")})

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
            return poberatel.getMeno()+" "+poberatel.getMesto()+" "+poberatel.getPsc()+" "+poberatel.getUlica();
    }

    public Long getPoberatel_ID() {
        if (poberatel == null)
            return new Long(0);
        else
            return poberatel.getId();
    }


}

