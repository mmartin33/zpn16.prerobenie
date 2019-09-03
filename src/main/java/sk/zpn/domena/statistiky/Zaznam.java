package sk.zpn.domena.statistiky;

import org.eclipse.persistence.config.CacheType;

import javax.persistence.*;
import java.math.BigDecimal;


@SqlResultSetMapping(name = "mapovanieZaznamu",
        entities = {
                @EntityResult(entityClass = Zaznam.class, fields = {
                        @FieldResult(name = "kluc", column = "kluc"),
                        @FieldResult(name = "hodnota", column = "hodnota")
                })}
)


@Entity


public class Zaznam {

    @Id
    @Column(name = "kluc")
    private String kluc;
    @Column(name = "hodnota")
    private BigDecimal hodnota;

    public Zaznam(String kluc, BigDecimal hodnota) {
        this.kluc = kluc;
        this.hodnota = hodnota;
    }
    public Zaznam() {
    }

    public String getKluc() {
        return kluc;
    }

    public BigDecimal getHodnota() {
        return hodnota;
    }
}


