package sk.zpn.zaklad.model;

import com.google.common.collect.Maps;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.Produkt;
import sk.zpn.domena.StatistikaBodov;
import sk.zpn.domena.statistiky.Zaznam;
import sk.zpn.nastroje.NastrojePoli;
import sk.zpn.nastroje.XlsStatistikaBodov;
import sk.zpn.nastroje.XlsStatistikaBodovDodavatelProdukt;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    public static void load2(LocalDate dod, LocalDate ddo) {
        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);



        Map<String, Double> pociatocnyStav = Maps.newHashMap();
        Map<String, Double> bodyZaPredaj = Maps.newHashMap();
        Map<String, Double> bodyIne = Maps.newHashMap();
        Map<String, Double> konecnyStav = Maps.newHashMap();
        pociatocnyStav=vratPociatocnyStav(dod,ddo);
        bodyZaPredaj=vratBodyZaPredaj(dod,ddo);
        bodyIne=vratBodyIne(dod,ddo);
        konecnyStav=vratKonecnyStav(dod,ddo);
        List <Poberatel> poberatelia=PoberatelNastroje.zoznamPoberatelov();
        String nadpis="Vyhodnotenie poberatelov  od: "+simpleDateFormat.format(Date.valueOf(dod))+" dp: "+ simpleDateFormat.format(Date.valueOf(ddo));
        XlsStatistikaBodov.vytvorXLS2(poberatelia,pociatocnyStav,bodyZaPredaj,bodyIne,konecnyStav,nadpis);

    }

    private static Map<String, Double> vratKonecnyStav(LocalDate dod, LocalDate ddo) {
        EntityManager em1;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em1 = emf.createEntityManager();
        String sql="select CAST(p.poberatel_id as text) as kluc ,sum(p.body) as hodnota from polozkydokladu as p " +
                " join doklady as d on d.id=p.doklad_id " +
                " where date(d.datum)<=date('"+ddo+"') AND d.stavdokladu='POTVRDENY'" +
                "    group by CAST(p.poberatel_id as text) " +
                "    having sum(p.body)>0 ";

        Query query = em1.createNativeQuery(sql);

//        result1= em1.createNativeQuery(sql,Zaznam.class).getResultList();
        List result1 = query.getResultList();
        Map<String, Double> vysledok = NastrojePoli.<String, Double>prerobListNaMapu2(result1);
        emf.close();



        return vysledok;

    }

    private static Map<String, Double> vratBodyIne(LocalDate dod, LocalDate ddo) {
        EntityManager em1;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em1 = emf.createEntityManager();
        String sql="select CAST(p.poberatel_id as text) as kluc,sum(p.body) as hodnota from polozkydokladu as p " +
                " join doklady as d on d.id=p.doklad_id " +
                " where date(d.datum)>=date('"+dod+"') and date(d.datum)<=date('"+ddo+"') " +
                " and d.typdokladu='INTERNY_DOKLAD' " +
                " and d.stavdokladu='POTVRDENY' " +
                " group by CAST(p.poberatel_id as text) " +
                " having sum(p.body)>0";

        Query query = em1.createNativeQuery(sql);

//        result1= em1.createNativeQuery(sql,Zaznam.class).getResultList();
        List result1 = query.getResultList();
        Map<String, Double> vysledok = NastrojePoli.<String, Double>prerobListNaMapu2(result1);
        emf.close();

        return vysledok;


    }

    private static Map<String, Double> vratBodyZaPredaj(LocalDate dod, LocalDate ddo) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");

        EntityManager em1 = emf.createEntityManager();
        String sql="select CAST(p.poberatel_id as text) as kluc ,sum(p.body) as hodnota from polozkydokladu as p " +
                " join doklady as d on d.id=p.doklad_id " +
                " where d.datum>=? and d.datum<=? " +
                " and d.typdokladu='DAVKA' " +
                " and d.stavdokladu='POTVRDENY' " +
                " group by CAST(p.poberatel_id as text)  " +
                " having sum(p.body)>0";
//        String sql="select CAST(p.poberatel_id as text)as kluc ,sum(p.body) as hodnota from polozkydokladu as p " +
//                " join doklady as d on d.id=p.doklad_id " +
//                " where date(d.datum)>=date('"+dod+"') and date(d.datum)<=date('"+ddo+"') " +
//                " and d.typdokladu='DAVKA' " +
//                " and d.stavdokladu='POTVRDENY' " +
//                " group by p.poberatel_id " +
//                " having sum(p.body)>0";

        //Query query = em1.createNativeQuery(sql, Zaznam.class);
        Query query = em1.createNativeQuery(sql);
        query.setParameter(1,dod);
        query.setParameter(2,ddo);

//        result1= em1.createNativeQuery(sql,Zaznam.class).getResultList();
        List result1 = query.getResultList();
        Map<String, Double> vysledok = NastrojePoli.<String, Double>prerobListNaMapu2(result1);
        emf.close();
        return vysledok;

    }

    private static Map<String, Double> vratPociatocnyStav(LocalDate dod, LocalDate ddo) {
        EntityManager em1;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em1 = emf.createEntityManager();
        String sql="select CAST(p.poberatel_id as text) as kluc,sum(p.body) as hodnota from polozkydokladu as p " +
                " join doklady as d on d.id=p.doklad_id " +
                " where date(d.datum)<date('"+dod+"') AND d.stavdokladu='POTVRDENY'" +
                "    group by CAST(p.poberatel_id as text) " +
                "    having sum(p.body)>0 ";

//        result1= em1.createNativeQuery(sql,Zaznam.class).getResultList();
//        vysledok= NastrojePoli.prerobListNaMapu(result1);
        Query query = em1.createNativeQuery(sql);
        List result1 = query.getResultList();
        Map<String, Double> vysledok = NastrojePoli.<String, Double>prerobListNaMapu2(result1);

        emf.close();
        return vysledok;


    }
}
