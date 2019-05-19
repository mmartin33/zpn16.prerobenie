package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.StatPoberatel;

import javax.persistence.*;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class StatPoberatelNastroje {

    public static void exportDoXLS(List<StatPoberatel> statList) {

    }

    public static List<StatPoberatel> load(LocalDate dod, LocalDate ddo) {
        List<StatPoberatel> result1=null;
        EntityManager em1=null;
        em1 = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");

        String sql="select pob.meno as poberatel_nazov," +
                "(select sum(p.body) from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id" +
                "    where date(d.datum)<'"+dod+"' and p.poberatel_id=pob.id) as pociatocny_stav," +

                "(select sum(p.body) from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id" +
                "    where d.datum>=date('"+dod+"') and d.datum<=date('"+ddo+"') and p.poberatel_id=pob.id" +
                "    and d.typdokladu='DAVKA') " +
                " as body_za_predaj,"+
                "(select sum(p.body) from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id" +
                "    where d.datum>=date('"+dod+"') and d.datum<=date('"+ddo+"') and p.poberatel_id=pob.id" +
                "    and d.typdokladu<>'DAVKA')" +
                " as body_ine,"+

                "(select sum(p.body) from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id" +
                "    where d.datum=date('"+ddo+"') and p.poberatel_id=pob.id) as konecny_stav" +
                " from poberatelia as pob";
//            String sql = "select pob.meno as poberatel_nazov, 0 as pociatocny_stav, 0  as body_za_predaj, 0 as body_ine, 0 as konecny_stav  from  poberatelia as pob" ;


            result1  = em1.createNativeQuery(sql,  "mapovanieVysledku").getResultList();

//        List result  = em.createNativeQuery(sql  ).getResultList();



        return result1;


    }


    public  List<StatPoberatel> load() {
            EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");

            String sql="select pob.meno as poberatel_nazov," +
                    "(select sum(p.body) from polozkydokladu as p " +
                    "join doklady as d on d.id=p.doklad_id" +
                    "    where date(d.datum)<date('01.01.2019') and p.poberatel_id=pob.id) as pociatocny_stav," +

                    "(select sum(p.body) from polozkydokladu as p " +
                    "join doklady as d on d.id=p.doklad_id" +
                    "    where date(d.datum)>=date('01.01.2019') and date(d.datum)<=date('01.01.2020') and p.poberatel_id=pob.id" +
                    "    and d.typdokladu='DAVKA') " +
                    " as body_za_predaj,"+
                    "(select sum(p.body) from polozkydokladu as p " +
                    "join doklady as d on d.id=p.doklad_id" +
                    "    where date(d.datum)>=date('01.01.2019') and date(d.datum)<=date('01.01.2020') and p.poberatel_id=pob.id" +
                    "    and d.typdokladu<>'DAVKA')" +
                    " as body_ine,"+

                    "(select sum(p.body) from polozkydokladu as p " +
                    "join doklady as d on d.id=p.doklad_id" +
                    "    where date(d.datum)<=date('31.12.2020') and p.poberatel_id=pob.id) as konecny_stav" +
                    " from poberatelia as pob";
//            String sql = "select pob.meno as poberatel_nazov, 0 as pociatocny_stav, 0  as body_za_predaj, 0 as body_ine, 0 as konecny_stav  from  poberatelia as pob" ;


            List<StatPoberatel> result  = em.createNativeQuery(sql,  "mapovanieVysledku").getResultList();



            return result;
        }


}
