package sk.zpn.domena;


import sk.zpn.zaklad.model.FirmaProduktNastroje;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Entity(name = "firma_velkoskladu")
@Cacheable(false)
@NamedQueries(value = {
        @NamedQuery(name = "FirmaVelkoskladu.getAll", query = "SELECT fv FROM firma_velkoskladu fv " +
                "JOIN fv.velkosklad v " +
                "JOIN fv.odberatel f " +
                " WHERE v.id =:id "),
        @NamedQuery(name = "FirmaVelkoskladu.getFirmu", query = "SELECT f FROM firma_velkoskladu fv " +
                "JOIN fv.velkosklad v " +
                "JOIN fv.odberatel f " +
                " WHERE v.id =:id "),
        @NamedQuery(name = "FirmaVelkoskladu.getFirmuPodlaIco", query = "SELECT f FROM firma_velkoskladu fv " +
                "JOIN fv.velkosklad v " +
                "JOIN fv.odberatel f " +
                " WHERE v.id =:id " +
                " and f.ico =:ico "),
        @NamedQuery(name = "FirmaVelkoskladu.getId", query = "SELECT fv FROM firma_velkoskladu fv " +
                "JOIN fv.velkosklad v " +
                "JOIN fv.odberatel f " +
                " WHERE v.id =:id_velkoskladu " +
                " and f.id =:id_odberatela ")
})

//@IdClass(FirmaVelkoskladuId.class)
public class FirmaVelkoskladu extends Vseobecne {


    @Id
    @ManyToOne
    @JoinColumn(name = "firma_id", referencedColumnName = "id", nullable = false)
    private Firma odberatel;

    @Id
    @ManyToOne
    @JoinColumn(name = "velkosklad_id", referencedColumnName = "id", nullable = false)
    private Firma velkosklad;


    public FirmaVelkoskladu() {
    }

    public FirmaVelkoskladu(Firma velkosklad, Firma odberatel) {
        this.odberatel = odberatel;
        this.velkosklad = velkosklad;
    }

    public Firma getVelkosklad() {
        return velkosklad;
    }

    public void setVelkosklad(Firma velkosklad) {
        this.velkosklad = velkosklad;
    }

    public Firma getOdberatel() {
        return odberatel;
    }

    public void setOdberatel(Firma odberatel) {
        this.odberatel = odberatel;
    }

    public boolean isNew() {
        return this.getId() == null;
    }
}





