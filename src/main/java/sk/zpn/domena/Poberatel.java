package sk.zpn.domena;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.Date;

@Entity(name = "poberatelia")
@NamedQueries(value = {
        @NamedQuery(name = "Poberatel.getPodlaMena", query = "SELECT p FROM poberatelia p WHERE p.meno =:meno"),
        @NamedQuery(name = "Poberatel.get", query = "SELECT p FROM poberatelia p WHERE p.id =:id"),
        @NamedQuery(name = "Poberatel.getAll", query = "SELECT p FROM poberatelia p")})

public class Poberatel extends Vseobecne {

    private String meno;
//    private String priezvisko;
    private String titul;
    private Date vyznamnyDatum;
    private String ulica;
    private String heslo;
    private String kod;
    private String psc;
    private String mesto;
    private String mobil;
    private String telefon;
    private String email;


    private Poberatel firma;

    public Poberatel() {


    }

    public String getMeno() {return meno;}

    public void setMeno(String meno) {this.meno = meno;}



    public String getPoberatelMenoAdresa() {
        if (this == null)
            return "";
        else
            return this.getMeno()+" "+this.getMesto()+" "+this.getPsc()+" "+this.getUlica();
    }



    //    public String getPriezvisko() {return priezvisko;}
//
//    public void setPriezvisko(String priezvisko) {this.priezvisko = priezvisko;}
//
    public String getTitul() {return titul;}

    public void setTitul(String titul) {this.titul = titul;}

    public Date getVyznamnyDatum() {return vyznamnyDatum;}

    public void setVyznamnyDatum(Date vyznamnyDatum) {this.vyznamnyDatum = vyznamnyDatum;}

    public String getUlica() {return ulica;}

    public void setUlica(String ulica) {this.ulica = ulica;}

    public String getHeslo() {return heslo;}

    public void setHeslo(String heslo) {this.heslo = heslo;}

    public String getKod() {return kod;}

    public void setKod(String kod) {this.kod = kod;}

    public String getPsc() {return psc;}

    public void setPsc(String psc) {this.psc = psc;}

    public String getMesto() {return mesto;}

    public void setMesto(String mesto) {this.mesto = mesto;}

    public String getMobil() {return mobil;}

    public void setMobil(String mobil) {this.mobil = mobil;}


    public String getTelefon() {return telefon;}

    public void setTelefon(String telefon) {this.telefon = telefon;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public Poberatel getFirma() {return firma;}

    public void setFirma(Poberatel firma) {this.firma = firma;}

    public boolean isNew() {
        return this.getId() == null;
    }


}





