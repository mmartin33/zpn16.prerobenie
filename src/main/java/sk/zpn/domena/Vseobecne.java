package sk.zpn.domena;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@MappedSuperclass
public abstract class Vseobecne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private Date kedy;

    private Long kto_id;
    public Long getId() {
        return id;
    }

    public Vseobecne setId(Long id) {
        this.id = id;
        return this;
    }

    public Date getKedy() {
        return kedy;
    }



    public void setKedy(Date kedy) {
        this.kedy = kedy;
    }

    public Long getKto() {
        return kto_id;
    }

    public void setKto(Long kto) {
        this.kto_id = kto;
    }


}



//    ALTER TABLE public.doklady
//        drop COLUMN kedy ;
//
//        ALTER TABLE public.doklady
//        add COLUMN kedy  TIMESTAMP(0);
//
//
//
//
//        ALTER TABLE public.firmy
//        drop COLUMN kedy ;
//
//        ALTER TABLE public.firmy
//        add COLUMN kedy  TIMESTAMP(0);
//
//
//
//
//        ALTER TABLE public.poberatelia
//        drop COLUMN kedy ;
//
//        ALTER TABLE public.poberatelia
//        add COLUMN kedy  TIMESTAMP(0);
//
//
//        ALTER TABLE public.polozkydokladu
//        drop COLUMN kedy ;
//
//        ALTER TABLE public.polozkydokladu
//        add COLUMN kedy  TIMESTAMP(0);
//
//
//        ALTER TABLE public.prevadzky
//        drop COLUMN kedy ;
//
//        ALTER TABLE public.prevadzky
//        add COLUMN kedy  TIMESTAMP(0);
//
//
//        ALTER TABLE public.produkty
//        drop COLUMN kedy ;
//
//        ALTER TABLE public.produkty
//        add COLUMN kedy  TIMESTAMP(0);
//
//
//        ALTER TABLE public.uzivatelia
//        drop COLUMN kedy ;
//
//        ALTER TABLE public.uzivatelia
//        add COLUMN kedy  TIMESTAMP(0);
//
//
//
