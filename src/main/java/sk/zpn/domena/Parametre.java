package sk.zpn.domena;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity(name = "parametre")
@NamedQueries(value = {
        @NamedQuery(name = "Parametre.get", query = "SELECT p FROM parametre p ")})

public class Parametre extends Vseobecne {
    @NotNull
    @Pattern(regexp = "[0-9]$", message = "Zlý rok")
    @Column(name = "rok", nullable = false)
    private String rok;

    public Parametre() {

    }
    public String getRok() {return rok;
    }
    public Parametre setRok(String rok) {
        this.rok = rok;
        return this;
    }
}
