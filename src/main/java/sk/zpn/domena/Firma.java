package sk.zpn.domena;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity(name = "firmy")
@Cacheable(false)
@NamedQueries(value = {
        @NamedQuery(name = "Firma.getPodlaIca", query = "SELECT f FROM firmy f WHERE f.ico =:ico order by f.nazov"),
        @NamedQuery(name = "Firma.getPodlaNazvu", query = "SELECT f FROM firmy f WHERE f.nazov =:nazov order by f.nazov"),
        @NamedQuery(name = "Firma.getPodlaICOaNazvu", query = "SELECT f FROM firmy f " +
                "WHERE f.ico =:ico " +
                " and f.nazov =:nazov order by f.nazov"),
        @NamedQuery(name = "Firma.getPodlaICO", query = "SELECT f FROM firmy f " +
                "WHERE f.ico =:ico " ),
        @NamedQuery(name = "Firma.getVelkosklady", query = "SELECT distinct f FROM uzivatelia u " +
                " JOIN u.firma f  " +
                " where u.typUzivatela=sk.zpn.domena.TypUzivatela.PREDAJCA" +
                " order by f.nazov "),
        @NamedQuery(name = "Firma.getDodavatelia", query = "SELECT DISTINCT f FROM produkty p " +
                "JOIN p.firma f  order by f.nazov"),
        @NamedQuery(name = "Firma.getPodlaID", query = "SELECT f FROM firmy f WHERE f.id =:id"),
        @NamedQuery(name = "Firma.getAll", query = "SELECT f FROM firmy f order by f.nazov")})




public class Firma extends Vseobecne {

    private String nazov;
    @Column(name = "ico", nullable = false, unique = true)
    private String ico;
    private String dic;
    private String ic_dph;
    private String ulica;
    private String mesto;
    private String psc;
    private String telefon;
    private BigDecimal pociatocnyStav;



    @OneToMany(mappedBy = "firma")
    private List<FirmaProdukt> produktMostik;

    public Firma() {

    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getIc_dph() {
        return ic_dph;
    }

    public void setIc_dph(String ic_dph) {
        this.ic_dph = ic_dph;
    }

    public String getDic() {
        return dic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getUlica() {return ulica; }

    public void setUlica(String ulica) {this.ulica = ulica;}

    public String getMesto() {return mesto;}

    public void setMesto(String mesto) {this.mesto = mesto;}

    public String getPsc() {return psc;}

    public void setPsc(String psc) {this.psc = psc;}

    public String getTelefon() {return telefon; }

    public void setTelefon(String telefon) {this.telefon = telefon;}

    public BigDecimal getPociatocnyStav() {return pociatocnyStav;}

    public void setPociatocnyStav(BigDecimal pociatocnyStav) {this.pociatocnyStav = pociatocnyStav;}

    public boolean isNew() {return this.getId() == null;}



    public List<FirmaProdukt> getProduktMostik() {
        return produktMostik;
    }

    public void setProduktMostik(List<FirmaProdukt> produktMostik) {
        this.produktMostik = produktMostik;
    }


    public String getTextLog() {
        String text=null;
        text=this.getId()+"nazov"+this.getNazov()+
                " ico:"+this.getIco()+
                " mesto:"+this.getMesto()+
                " ulica:"+this.getUlica()+
                " dis:"+this.getDic();

        return text;

    }
}





