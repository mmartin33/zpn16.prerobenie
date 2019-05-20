package sk.zpn.domena;



import javax.persistence.*;
import java.math.BigDecimal;


@SqlResultSetMapping(name="mapovanieVysledku",
        entities={
                @EntityResult(entityClass=StatPoberatel.class, fields={
                        @FieldResult(name="id", column="id"),
                        @FieldResult(name="poberatelNazov", column="poberatel_nazov"),
                        @FieldResult(name="pociatocnyStav", column="pociatocny_stav"),
                        @FieldResult(name="bodyZaPredaj", column="body_za_predaj"),
                        @FieldResult(name="bodyIne", column="body_ine"),
                        @FieldResult(name="konecnyStav", column="konecny_stav"),
                        })}
)

@Entity

public class StatPoberatel  {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "poberatel_nazov")
    private String poberatelNazov;
    @Column(name = "pociatocny_stav")
    private BigDecimal pociatocnyStav;
    @Column(name = "body_za_predaj")
    private BigDecimal bodyZaPredaj;
    @Column(name = "body_ine")
    private BigDecimal bodyIne;
    @Column(name = "konecny_stav")
    private BigDecimal konecnyStav;

        public StatPoberatel(Long id,
                             String poberatelNazov,
                             BigDecimal pociatocnyStav,
                             BigDecimal bodyZaPredaj,
                             BigDecimal bodyIne,
                             BigDecimal konecnyStav)
        {
            this.poberatelNazov=poberatelNazov;
            this.pociatocnyStav=pociatocnyStav;
            this.bodyZaPredaj=bodyZaPredaj;
            this.bodyIne=bodyIne;
            this.konecnyStav=konecnyStav;
        }

    public StatPoberatel()
    {

    }

    public String getPoberatelNazov() {
        return poberatelNazov;
    }

    public BigDecimal getPociatocnyStav() {   return pociatocnyStav;
    }

    public BigDecimal getBodyZaPredaj() {
        return bodyZaPredaj;
    }

    public BigDecimal getBodyIne() {
        return bodyIne;
    }

    public BigDecimal getKonecnyStav() {
        return konecnyStav;
    }
}
