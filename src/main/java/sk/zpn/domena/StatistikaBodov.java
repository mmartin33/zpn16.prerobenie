package sk.zpn.domena;



import javax.persistence.*;
import java.math.BigDecimal;


@SqlResultSetMapping(name="mapovanieVysledku",
        entities={
                @EntityResult(entityClass=StatistikaBodov.class, fields={
                        @FieldResult(name="id", column="id"),
                        @FieldResult(name="nazov", column="nazov"),
                        @FieldResult(name="pociatocnyStav", column="pociatocny_stav"),
                        @FieldResult(name="bodyZaPredaj", column="body_za_predaj"),
                        @FieldResult(name="bodyIne", column="body_ine"),
                        @FieldResult(name="konecnyStav", column="konecny_stav"),
                        })}
)

@Entity

public class StatistikaBodov  {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "nazov")
    private String nazov;
    @Column(name = "pociatocny_stav")
    private BigDecimal pociatocnyStav;
    @Column(name = "body_za_predaj")
    private BigDecimal bodyZaPredaj;
    @Column(name = "body_ine")
    private BigDecimal bodyIne;
    @Column(name = "konecny_stav")
    private BigDecimal konecnyStav;

        public StatistikaBodov(Long id,
                             String nazov,
                             BigDecimal pociatocnyStav,
                             BigDecimal bodyZaPredaj,
                             BigDecimal bodyIne,
                             BigDecimal konecnyStav)
        {
            this.nazov = nazov;
            this.pociatocnyStav=pociatocnyStav;
            this.bodyZaPredaj=bodyZaPredaj;
            this.bodyIne=bodyIne;
            this.konecnyStav=konecnyStav;
        }

    public StatistikaBodov()
    {

    }

    public String getNazov() {
        return nazov;
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
