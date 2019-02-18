package sk.zpn.domena;

import org.apache.log4j.Logger;
import sk.zpn.authentification.HesloNastroje;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static javax.persistence.CascadeType.PERSIST;

@Entity(name = "uzivatelia")
@NamedQueries(value = {
        @NamedQuery(name = "Uzivatel.getPodlaMena", query = "SELECT u FROM uzivatelia u WHERE u.meno =:meno"),
        @NamedQuery(name = "Uzivatel.getAll", query = "SELECT u FROM uzivatelia u"),
        @NamedQuery(name = "Uzivatel.get", query = "SELECT u FROM uzivatelia u WHERE u.id =:id")})

public class Uzivatel extends Vseobecne {

    private static final Logger logger = Logger.getLogger(Uzivatel.class);

    @ManyToOne(fetch = FetchType.LAZY, cascade=PERSIST)
    @JoinColumn(nullable = true)
    private Firma firma;


    @NotNull
    @Pattern(regexp = "[a-z0-9._%+-]$", message = "Zle meno")
    @Column(name = "meno", nullable = false, unique = true)
    private String meno;
    private String heslo;

    @Enumerated(EnumType.STRING)
    private TypUzivatela typUzivatela;

    @Enumerated(EnumType.STRING)
    private StatusUzivatela statusUzivatela;


    public Uzivatel() {
        this.typUzivatela = TypUzivatela.PREDAJCA;
        this.statusUzivatela = StatusUzivatela.ACTIVE;
        this.heslo = "";
    }


    public Uzivatel(String meno, String heslo, TypUzivatela typUzivatela) {
        this.setTypUzivatela(typUzivatela);
        this.setMeno(meno);
        this.setHeslo(heslo);
        this.setStatusUzivatela(StatusUzivatela.ACTIVE);
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
        try {
            this.heslo = HesloNastroje.getSaltedHash(heslo);
        } catch (Exception e) {
            logger.error("Hashing of password failed", e);
        }
    }

    public Firma getFirma() {
        return firma;
    }

    public String getFirmaNazov() {
        if (firma == null)
            return "";
        else
            return firma.getNazov();
    }

    public void setFirma(Firma firma) {
        this.firma = firma;
    }

    public boolean isNew() {
        return this.getId() == null;
    }

    public TypUzivatela getTypUzivatela() {
        return typUzivatela;
    }

    public void setTypUzivatela(TypUzivatela typUzivatela) {
        this.typUzivatela = typUzivatela;
    }

    public StatusUzivatela getStatusUzivatela() {
        return statusUzivatela;
    }

    public void setStatusUzivatela(StatusUzivatela statusUzivatela) {
        this.statusUzivatela = statusUzivatela;
    }
}





