package sk.zpn.domena;

import javax.persistence.*;
import java.math.BigInteger;


@Entity(name = "uzivatelia")
@NamedQueries(value = {
@NamedQuery(name = "Uzivatel.getPodlaMena", query = "SELECT u FROM uzivatelia u WHERE u.meno =:meno")
})

public class Uzivatel extends Vseobecne {
    public static final int PREDAJCA = 1;
    public static final int SPRAVCA_ZPN = 2;
    public static final int ADMIN = 0;

    private String meno;

    private int typKonta;

    private String heslo;


    private Uzivatel uzivatel;

    public Uzivatel() {


    }



    public Uzivatel(String meno, String heslo) {
        //super(id);
        this.setMeno(meno);
        this.setHeslo(heslo);
    }
    public Uzivatel getUzivatel() {
        return this.uzivatel;
    }

    public void setUzivatel(Uzivatel uzivatel) {
        this.uzivatel = uzivatel;
    }


    public boolean jeAdmin() {
        return getUzivatel() != null && getUzivatel().getTypKonta() == Uzivatel.ADMIN;
    }


    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getHeslo() {
        return heslo;
    }

    public void setHeslo(String heslo) {
        this.heslo = heslo;
    }

    public int getTypKonta() {
        return typKonta;
    }

    public void setTypKonta(int typKonta) {
        this.typKonta = typKonta;
    }
}





