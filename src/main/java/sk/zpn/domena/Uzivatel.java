package sk.zpn.domena;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static javax.persistence.CascadeType.PERSIST;

@Entity(name = "uzivatelia")
@NamedQueries(value = {
        @NamedQuery(name = "Uzivatel.getPodlaMenaHesla", query = "SELECT u FROM uzivatelia u WHERE u.meno =:meno and u.heslo =:heslo"),
        @NamedQuery(name = "Uzivatel.getAll", query = "SELECT u FROM uzivatelia u"),
        @NamedQuery(name = "Uzivatel.get", query = "SELECT u FROM uzivatelia u WHERE u.id =:id")})

public class Uzivatel extends Vseobecne {

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


    public Uzivatel() {
        this.typUzivatela = TypUzivatela.PREDAJCA;
    }


    public Uzivatel(String meno, String heslo, TypUzivatela typUzivatela) {
        this.setTypUzivatela(typUzivatela);
        this.setMeno(meno);
        this.setHeslo(heslo);
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
}





