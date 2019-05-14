package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.StatPoberatel;

import javax.persistence.*;
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

public class StatPoberatelNastroje {
        public  List<StatPoberatel> load(){
            EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");;
//            String sql="select pob.meno as poberatelNazov," +
//                    "(select sum(p.body) from polozkydokladu as p" +
//                    "join doklady as d on d.id=p.doklad_id" +
//                    "    where date(d.datum)<date('01.01.2019') and p.poberatel_id=pob.id) as pociatocnyStav," +
//
//                    "(select sum(p.body) from polozkydokladu as p" +
//                    "join doklady as d on d.id=p.doklad_id" +
//                    "    where date(d.datum)>=date('01.01.2019') and date(d.datum)<=date('01.01.2020') and p.poberatel_id=pob.id) as bodyZaPredaj," +
//                    "0 as bodyZaPredaj,"+
//                    "(select sum(p.body) from polozkydokladu as p" +
//                    "join doklady as d on d.id=p.doklad_id" +
//                    "    where date(d.datum)<=date('01.31.2020') and p.poberatel_id=pob.id) as konecnyStav" +
//                    " from poberatelia as pob";
            String sql="select pob.meno as poberatelNazov, 0 as pociatocnyStav, 0  as bodyZaPredaj, 0 as bodyIne, 0 as konecnyStav  " +
                    " from poberatelia as pob";
            Query query = em.createNativeQuery(sql,  "StatPoberatel"    );
            List<StatPoberatel> result = query.getResultList();



            return result;
        }


}
