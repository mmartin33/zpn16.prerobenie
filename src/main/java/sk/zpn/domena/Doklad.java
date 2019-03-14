package sk.zpn.domena;

import org.apache.log4j.Logger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.text.SimpleDateFormat;
import java.util.Date;

import static javax.persistence.CascadeType.PERSIST;

@Entity(name = "doklady")
@NamedQueries(value = {
        @NamedQuery(name = "Doklad.getAll", query = "SELECT d FROM doklady d"),

        @NamedQuery(name = "Doklad.get", query = "SELECT d FROM doklady d WHERE d.id =:id")})

public class Doklad extends Vseobecne {

    private static final Logger logger = Logger.getLogger(Doklad.class);

    @ManyToOne(fetch = FetchType.LAZY, cascade=PERSIST)
    @JoinColumn(nullable = true)
    private Firma firma;


    @NotNull
    @Pattern(regexp = "[a-z0-9._%+-]$", message = "Zle číslo")
    @Column(name = "cisloDokladu", nullable = false, unique = true)
    private String cisloDokladu;
    private Date datum;

    @Enumerated(EnumType.STRING)
    private TypDokladu typDokladu;

    private String poznamka;


    public Doklad() {
        this.typDokladu = TypDokladu.INTERNY_DOKLAD;
        this.datum=new Date();


    }


//    public Doklad(String cislo, String heslo, TypDokladu typDokladu) {
//        this.setTypUzivatela(typUzivatela);
//        this.setMeno(meno);
//        this.setHeslo(heslo);
//        this.setStatusUzivatela(StatusUzivatela.ACTIVE);
//    }


    public Firma getFirma() {
        return firma;
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

    public void setFirma(Firma firma) {
        this.firma = firma;
    }

    public boolean isNew() {
        return this.getId() == null;
    }

    public TypDokladu getTypDokladu() {
        return typDokladu;
    }

    public void setTypDokladu(TypDokladu typDokladu) {
        this.typDokladu = typDokladu;
    }

    public String getCisloDokladu() {return cisloDokladu;}

    public void setCisloDokladu(String cisloDokladu) {this.cisloDokladu = cisloDokladu;}

    public Date getDatum() {return datum;}

    public String getFormatovanyDatum() {
        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(datum);
    }

    public void setDatum(Date datum) {this.datum = datum; }

    public String getPoznamka() {return poznamka;}

    public void setPoznamka(String poznamka) {this.poznamka = poznamka;}
}





