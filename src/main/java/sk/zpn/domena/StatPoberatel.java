package sk.zpn.domena;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@SqlResultSetMapping(name="MapovanieVysledku",
        classes={
                @ConstructorResult(
                        targetClass= StatPoberatel.class,
                        columns={

                                @ColumnResult(name="poberatelNazov", type=String.class),
                                @ColumnResult(name="pociatocnyStav", type=BigDecimal.class),
                                @ColumnResult(name="bodyZaPredaj", type= BigDecimal.class),
                                @ColumnResult(name="bodyIne", type=BigDecimal.class),
                                @ColumnResult(name="konecnyStav", type=BigDecimal.class)

                        }
                )
        }
)

public class StatPoberatel implements Serializable {


        @Id
        @Column(name = "poberatelNazov")
        private String poberatelNazov;
        @Column(name = "pociatocnyStav")
        private BigDecimal pociatocnyStav;
        @Column(name = "bodyZaPredaj")
        private BigDecimal bodyZaPredaj;
        @Column(name = "bodyIne")
        private BigDecimal bodyIne;
        @Column(name = "konecnyStav")
        private BigDecimal konecnyStav;

        private StatPoberatel(String poberatelNazov,
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

    public String getPoberatelNazov() {
        return poberatelNazov;
    }
}
