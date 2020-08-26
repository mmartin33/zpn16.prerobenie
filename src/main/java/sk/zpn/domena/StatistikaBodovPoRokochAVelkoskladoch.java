package sk.zpn.domena;



import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Map;


@SqlResultSetMapping(name="mapStatistikaBodovPoRokochAVelkoskladoch",
        entities={
                @EntityResult(entityClass= StatistikaBodovPoRokochAVelkoskladoch.class, fields={
                        @FieldResult(name="id", column="id"),
                        @FieldResult(name="ico", column="ico"),
                        @FieldResult(name="nazov", column="nazov"),
                        @FieldResult(name="rok", column="rok"),
                        @FieldResult(name="body", column="body"),
                        })}
)

@Entity
@Cacheable(false)

public class StatistikaBodovPoRokochAVelkoskladoch  {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "ico")
    private String ico;
    @Column(name = "nazov")
    private int rok;
    @Column(name = "rok")
    private String nazov;
    @Column(name = "body")
    private BigDecimal body;


    public StatistikaBodovPoRokochAVelkoskladoch()
    {
    }

    public String getIco() {
        return ico;
    }

    public int getRok() {
        return rok;
    }

    public String getNazov() {
        return nazov;
    }

    public BigDecimal getBody() {
        return body;
    }
}
