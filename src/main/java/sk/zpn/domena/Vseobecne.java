package sk.zpn.domena;

import javax.persistence.*;
import java.math.BigInteger;

@MappedSuperclass
public abstract class Vseobecne {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
