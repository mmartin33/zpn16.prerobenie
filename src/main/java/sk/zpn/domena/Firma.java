package sk.zpn.domena;

import javax.persistence.*;

@Entity(name = "firmy")
@NamedQueries(value = {
        @NamedQuery(name = "Firma.getPodlaIca", query = "SELECT f FROM firmy f WHERE f.ico =:ico"),
        @NamedQuery(name = "Firma.getAll", query = "SELECT f FROM firmy f")})

public class Firma extends Vseobecne {

    private String nazov;
    private String ico;
    private String dic;
    private String ic_dph;
    private String ulica;
    private String mesto;
    private String psc;
    private String telefon;


    private Firma firma;

    public Firma() {

    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public Firma getFirma() {
        return firma;
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

    public void setFirma(Firma firma) {this.firma = firma;}

    public boolean isNew() {return this.getId() == null;}

}





