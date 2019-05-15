package sk.zpn.domena;

import com.vaadin.server.VaadinSession;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@SqlResultSetMapping(name="mapovanieVysledku",
        entities={
                @EntityResult(entityClass=StatPoberatel.class, fields={
                        @FieldResult(name="poberatelNazov", column="poberatel_nazov"),
                        @FieldResult(name="pociatocnyStav", column="pociatocny_stav"),
                        @FieldResult(name="bodyZaPredaj", column="body_za_predaj"),
                        @FieldResult(name="bodyIne", column="body_ine"),
                        @FieldResult(name="konecnyStav", column="konecny_stav"),
                        })}
)

@Entity
public class StatPoberatel  {
        private String poberatelNazov;
        private BigDecimal pociatocnyStav;
        private BigDecimal bodyZaPredaj;
        private BigDecimal bodyIne;
        private BigDecimal konecnyStav;

        public StatPoberatel(String poberatelNazov,
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
}