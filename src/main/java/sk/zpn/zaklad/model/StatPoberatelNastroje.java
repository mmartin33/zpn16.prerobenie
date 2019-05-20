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
        EntityManager em1;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em1 = emf.createEntityManager();



        String sql="select pob.id, pob.meno as poberatel_nazov," +
                "(select sum(p.body) from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id" +
                "    where date(d.datum)<date('"+dod+"') and p.poberatel_id=pob.id  AND d.stavdokladu='POTVRDENY') as pociatocny_stav," +

                "(select sum(p.body) from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id" +
                "    where d.datum>=date('"+dod+"') and d.datum<=date('"+ddo+"') and p.poberatel_id=pob.id" +
                "    and d.typdokladu='DAVKA' " +
                "    and d.stavdokladu='POTVRDENY') " +
                " as body_za_predaj,"+
                "(select sum(p.body) from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id" +
                "    where d.datum>=date('"+dod+"') and d.datum<=date('"+ddo+"') and p.poberatel_id=pob.id  AND d.stavdokladu='POTVRDENY'" +
                "    and d.typdokladu<>'DAVKA')" +
                " as body_ine,"+

                "(select sum(p.body) from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id" +
                "    where d.datum=date('"+ddo+"') and p.poberatel_id=pob.id AND d.stavdokladu='POTVRDENY') as konecny_stav " +
                " from poberatelia as pob";
//            String sql = "select pob.id,pob.meno as poberatel_nazov, 1 as pociatocny_stav, 0  as body_za_predaj, 0 as body_ine, 0 as konecny_stav  from  poberatelia as pob" ;
            result1  = em1.createNativeQuery(sql,  "mapovanieVysledku").getResultList();
            em1.close();
            emf.close();

        return result1;

    }
}
