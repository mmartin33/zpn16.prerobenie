package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.StatPoberatel;

import javax.persistence.*;
import java.math.BigDecimal;

import java.util.List;

public class StatPoberatelNastroje {

        public  List<StatPoberatel> load() {
            EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
            ;
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
            String sql = "select pob.meno as poberatel_nazov, 0 as pociatocny_stav, 0  as body_za_predaj, 0 as body_ine, 0 as konecny_stav  " ;

            List<StatPoberatel> result  = em.createNativeQuery(sql,  "mapovanieVysledku").getResultList();



            return result;
        }


}
