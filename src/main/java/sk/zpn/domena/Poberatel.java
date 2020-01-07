package sk.zpn.domena;

import org.apache.commons.lang.StringUtils;
import sk.zpn.nastroje.RandomString;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Entity(name = "poberatelia")
@Cacheable(false)
@NamedQueries(value = {
        @NamedQuery(name = "Poberatel.getPodlaMena", query = "SELECT p FROM poberatelia p WHERE p.meno =:meno order by p.meno"),
        @NamedQuery(name = "Poberatel.get", query = "SELECT p FROM poberatelia p WHERE p.id =:id order by p.meno"),
        @NamedQuery(name = "Poberatel.getPodlaMenaLike", query = "SELECT p FROM poberatelia p where upper(p.meno) like upper(:meno) order by p.meno"),
        @NamedQuery(name = "Poberatel.getPodlaKoduAhesla", query = "SELECT p FROM poberatelia p WHERE trim(p.kod) =:kod and trim(p.heslo) =:heslo"),
        @NamedQuery(name = "Poberatel.getPodlaEmailuAhesla", query = "SELECT p FROM poberatelia p WHERE trim(p.email) =:kod and trim(p.heslo) =:heslo"),
        @NamedQuery(name = "Poberatel.getAllZaVelkosklad", query = "SELECT p FROM poberatelia " +
                "   " +
                " p order by p.meno "),
        @NamedQuery(name = "Poberatel.getAll", query = "SELECT p FROM poberatelia p order by p.meno")})
//e.firstName LIKE 'A%' OR e.firstName NOT LIKE '%._%' ESCAPE '.'
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
    private String poznamkaVelkoskladu;
    private BigDecimal pociatocnyStav;


    public Poberatel() {
        RandomString gen =new RandomString(8, ThreadLocalRandom.current());
        this.setKod(gen.nextString());
        this.setHeslo(gen.nextString());

    }

    public String getMeno() {return meno;}

    public void setMeno(String meno) {this.meno = meno;}



    public String getPoberatelMenoAdresa() {
        if (this == null)
            return "";
        else
            return this.getMeno()+" "+
                    (StringUtils.isEmpty(this.getMesto())?"":this.getMesto().toString())+" "+
                    (StringUtils.isEmpty(this.getPsc())?"":this.getPsc().toString())+" "+
                    (StringUtils.isEmpty(this.getUlica())?"":this.getUlica().toString());

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

    public BigDecimal getPociatocnyStav() {return pociatocnyStav;}

    public void setPociatocnyStav(BigDecimal pociatocnyStav) {this.pociatocnyStav = pociatocnyStav;}

    public boolean isNew() {
        return this.getId() == null;
    }

    public String getPoznamkaVelkoskladu() {
        return poznamkaVelkoskladu;
    }

    public void setPoznamkaVelkoskladu(String poznamkaVelkoskladu) {
        this.poznamkaVelkoskladu = poznamkaVelkoskladu;
    }
}





