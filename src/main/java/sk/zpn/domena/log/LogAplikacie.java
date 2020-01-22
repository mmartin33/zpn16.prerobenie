package sk.zpn.domena.log;

import sk.zpn.domena.Uzivatel;
import sk.zpn.domena.Vseobecne;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static javax.persistence.CascadeType.PERSIST;

@Entity(name = "log_aplikacie")
@NamedQueries(value = {
        @NamedQuery(name = "LogAplikacie.getAll", query = "SELECT l FROM log_aplikacie l")})

public class LogAplikacie extends Vseobecne {
    private String poznamka;


    @ManyToOne(fetch = FetchType.LAZY, cascade=PERSIST)
    @JoinColumn(nullable = true)
    private Uzivatel uzivatel;



    @Enumerated(EnumType.STRING)
    private TypLogovanejHodnoty typLogovanejHodnoty;

    @Enumerated(EnumType.STRING)
    private TypUkonu typUkonu;


    public LogAplikacie() {
    }

    public String getPoznamka() {
        return poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

    public TypLogovanejHodnoty getTypLogovanejHodnoty() {
        return typLogovanejHodnoty;
    }

    public void setTypLogovanejHodnoty(TypLogovanejHodnoty typLogovanejHodnoty) {
        this.typLogovanejHodnoty = typLogovanejHodnoty;
    }

    public TypUkonu getTypUkonu() {
        return typUkonu;
    }


    public void setTypUkonu(TypUkonu typUkonu) {
        this.typUkonu = typUkonu;
    }

    public Uzivatel getUzivatel() {
        return uzivatel;
    }
    public String getUzivatelMneno() {
        if (uzivatel==null)
            return "";
        return (uzivatel.getMeno()==null)?"":uzivatel.getMeno();
    }

    public void setUzivatel(Uzivatel uzivatel) {
        this.uzivatel = uzivatel;
    }

}





