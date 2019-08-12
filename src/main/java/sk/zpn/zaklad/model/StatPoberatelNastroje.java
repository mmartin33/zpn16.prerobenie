package sk.zpn.zaklad.model;

import sk.zpn.domena.StatistikaBodov;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

public class StatPoberatelNastroje {

    public static void exportDoXLS(List<StatistikaBodov> statList) {

    }

    public static List<StatistikaBodov> load(LocalDate dod, LocalDate ddo) {
        List<StatistikaBodov> result1=null;
        EntityManager em1;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em1 = emf.createEntityManager();



        String sql="select pob.id, pob.meno as nazov," +
                "(select sum(p.body) from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id" +
                "    where date(d.datum)<date('"+dod+"') and p.poberatel_id=pob.id  AND d.stavdokladu='POTVRDENY') as pociatocny_stav," +

                "(select sum(p.body) from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id" +
                "    where date(d.datum)>=date('"+dod+"') and date(d.datum)<=date('"+ddo+"') and p.poberatel_id=pob.id" +
                "    and d.typdokladu='DAVKA' " +
                "    and d.stavdokladu='POTVRDENY') " +
                " as body_za_predaj,"+
                "(select sum(p.body) from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id" +
                "    where date(d.datum)>=date('"+dod+"') and date(d.datum)<=date('"+ddo+"') and p.poberatel_id=pob.id  AND d.stavdokladu='POTVRDENY'" +
                "    and d.typdokladu='INTERNY_DOKLAD')" +
                " as body_ine,"+

                "(select sum(p.body) from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id" +
                "    where date(d.datum)<=date('"+ddo+"') and p.poberatel_id=pob.id AND d.stavdokladu='POTVRDENY') as konecny_stav " +
                " from poberatelia as pob " +
                " group by  pob.id, pob.meno"+
                " having "+
                "(coalesce((select sum(p.body) from polozkydokladu as p " +
                "                join doklady as d on d.id=p.doklad_id " +
                "                    where date(d.datum)>=date('"+dod+"') and date(d.datum)<=date('"+ddo+"') and p.poberatel_id=pob.id  AND d.stavdokladu='POTVRDENY'),0)<> 0 or" +
                " coalesce((select sum(abs(p.body)) from polozkydokladu as p " +
                "                join doklady as d on d.id=p.doklad_id " +
                "                    where  date(d.datum)<=date('"+dod+"') and p.poberatel_id=pob.id  AND d.stavdokladu='POTVRDENY'),0)<>0)";

//            String sql = "select pob.id,pob.meno as poberatel_nazov, 1 as pociatocny_stav, 0  as body_za_predaj, 0 as body_ine, 0 as konecny_stav  from  poberatelia as pob" ;
            result1  = em1.createNativeQuery(sql,  "mapovanieVysledkuBodov").getResultList();
            em1.close();
            emf.close();

        return result1;

    }
}
