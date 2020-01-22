package sk.zpn.zaklad.model;

import com.google.common.collect.Maps;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.StatistikaBodov;
import sk.zpn.domena.statistiky.ZoznamBodov;

import sk.zpn.nastroje.NastrojePoli;
import sk.zpn.nastroje.XlsStatistikaBodov;

import javax.persistence.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class StatPoberatelNastroje {

    public static void exportDoXLS(List<StatistikaBodov> statList) {

    }

    public static List<ZoznamBodov> zoznamBodovZaPoberatela(Long id_poberatela) {
        List<ZoznamBodov> result1 = null;

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");

        EntityManager em1 = emf.createEntityManager();
        String sql = "select d.datum||d.typdokladu||d.poznamka as kluc,d.datum as datum,d.typdokladu as typ_dokladu,d.poznamka as poznamka,sum(p.body)as body " +
                "from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id " +
                "join poberatelia as pob on pob.id=p.poberatel_id " +
                "where pob.id=? " +
                "group by d.datum||d.typdokladu||d.poznamka  ,d.datum,d.typdokladu,d.poznamka " +
                "order by d.datum ";

        Query query = em1.createNativeQuery(sql, "mapovanieZoznamuBodov");

        query.setParameter(1, id_poberatela);


        result1 = query.getResultList();
        em1.close();
        emf.close();

        return result1;

    }

    public static Double bodyZaPoberatela(Long id_poberatela) {
        Double result1 = null;

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");

        EntityManager em1 = emf.createEntityManager();
        String sql = "select sum(p.body)as body " +
                "from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id " +
                "join poberatelia as pob on pob.id=p.poberatel_id " +
                "where pob.id=? ";

        Query query = em1.createNativeQuery(sql);

        query.setParameter(1, id_poberatela);
        result1 = (Double) query.getSingleResult();
        em1.close();
        emf.close();

        return result1;

    }

    public static void bilanciaPoberatelov(LocalDate dod, LocalDate ddo, Firma velkosklad) {
        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);


        Map<String, Double> pociatocnyStav = Maps.newHashMap();
        Map<String, Double> bodyZaPredaj = Maps.newHashMap();
        Map<String, Double> bodyIne = Maps.newHashMap();
        Map<String, Double> bodyRegistracia = Maps.newHashMap();
        Map<String, Double> bodyOdmeny = Maps.newHashMap();
        Map<String, Double> bodyPrevod = Maps.newHashMap();
        Map<String, Double> konecnyStav = Maps.newHashMap();
        Map<String, String> icaFiriem = Maps.newHashMap();
        Map<String, String> poberatelPopis = Maps.newHashMap();
        Map<String, String> prevPopis = Maps.newHashMap();
        Map<String, String> icaFiriemZPob = Maps.newHashMap();
        Map<String, String> poberatelPopisZPob = Maps.newHashMap();
        Map<String, String> prevPopisZPob = Maps.newHashMap();
        Map<String, Double> poberateliaVelkoskladu = Maps.newHashMap();
        pociatocnyStav = vratPociatocnyStav(dod, ddo);
        bodyZaPredaj = vratBodyZaPredaj(dod, ddo);
        bodyIne = vratBodyIne(dod, ddo);
        bodyRegistracia = vratBodyRegistracia(dod, ddo);
        bodyOdmeny = vratBodyOdmeny(dod, ddo);
        bodyPrevod = vratBodyPrevod(dod, ddo);
        konecnyStav = vratKonecnyStav(dod, ddo);
        icaFiriem = vratIcaFiriem(dod, ddo);
        prevPopis=vratPrevadzkyPopisy(dod, ddo);
        poberatelPopis=vratPoberatelPopisy(dod, ddo);
        icaFiriemZPob = vratIcaFiriemZPob();
        prevPopisZPob=vratPrevadzkyPopisyZPob();
        poberatelPopisZPob=vratPoberatelPopisyZPob();
        if (velkosklad != null)
            poberateliaVelkoskladu = PoberatelNastroje.vratPoberatelovVelkoskladu(velkosklad);

        List<Poberatel> poberatelia = PoberatelNastroje.zoznamPoberatelov(null);
        String nadpis = "Vyhodnotenie poberatelov  od: " + simpleDateFormat.format(Date.valueOf(dod)) + " dp: " + simpleDateFormat.format(Date.valueOf(ddo));
        XlsStatistikaBodov.vytvorXLS2(poberatelia,
                                    pociatocnyStav,
                                    bodyZaPredaj,
                                    bodyIne,
                                    konecnyStav,
                                    nadpis,
                                    poberateliaVelkoskladu,
                                    bodyRegistracia,
                                    bodyOdmeny,
                                    bodyPrevod,
                                    icaFiriem,
                                    prevPopis,
                                    poberatelPopis,
                                    icaFiriemZPob,
                                    prevPopisZPob,
                                    poberatelPopisZPob);


    }

    private static Map<String, String> vratIcaFiriem(LocalDate dod, LocalDate ddo) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");

        EntityManager em1 = emf.createEntityManager();
        String sql = "select CAST(p.poberatel_id as text) as kluc ,COALESCE(firm.ico ||' '||firm.nazov,'')    as hodnota from polozkydokladu as p " +
                " join doklady as d on d.id=p.doklad_id " +
                " join prevadzky as prev on prev.id=p.prevadzka_id " +
                " join firmy as firm on firm.id=prev.firma_id " +
                " where d.datum>=? and d.datum<=? " +
                " and d.typdokladu='DAVKA' " +
                " and d.stavdokladu='POTVRDENY' " +
                " group by CAST(p.poberatel_id as text),COALESCE(firm.ico ||' '||firm.nazov,'')  " ;

        Query query = em1.createNativeQuery(sql);
        query.setParameter(1, dod);
        query.setParameter(2, ddo);

        List result1 = query.getResultList();
        Map<String, String> vysledok = NastrojePoli.<String, Double>prerobListNaMapu3(result1);
        emf.close();
        return vysledok;

    }
    private static Map<String, String> vratIcaFiriemZPob() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");

        EntityManager em1 = emf.createEntityManager();
        String sql = "select CAST(p.id as text) as kluc ,COALESCE(firm.ico ||' '||firm.nazov,'')    as hodnota from poberatelia as p " +
                " left join prevadzky as prev on prev.poberatel_id=p.id " +
                " left join firmy as firm on firm.id=prev.firma_id " +
                " group by CAST(p.id as text),COALESCE(firm.ico ||' '||firm.nazov,'')  " ;

        Query query = em1.createNativeQuery(sql);

        List result1 = query.getResultList();
        Map<String, String> vysledok = NastrojePoli.<String, Double>prerobListNaMapu3(result1);
        emf.close();
        return vysledok;

    }
    private static Map<String, String> vratPrevadzkyPopisy(LocalDate dod, LocalDate ddo) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");

        EntityManager em1 = emf.createEntityManager();
        String sql = "select CAST(p.id as text) as kluc ,COALESCE(prev.nazov ||' '||prev.mesto,'')    as hodnota from polozkydokladu as p " +
                " join doklady as d on d.id=p.doklad_id " +
                " join prevadzky as prev on prev.id=p.prevadzka_id " +
                " join firmy as firm on firm.id=prev.firma_id " +
                " where d.datum>=? and d.datum<=? " +
                " and d.typdokladu='DAVKA' " +
                " and d.stavdokladu='POTVRDENY' " +
                " group by CAST(p.id as text),COALESCE(prev.nazov ||' '||prev.mesto,'')  " ;

        Query query = em1.createNativeQuery(sql);
        query.setParameter(1, dod);
        query.setParameter(2, ddo);

        List result1 = query.getResultList();
        Map<String, String> vysledok = NastrojePoli.<String, Double>prerobListNaMapu3(result1);
        emf.close();
        return vysledok;

    }
    private static Map<String, String> vratPrevadzkyPopisyZPob() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");

        EntityManager em1 = emf.createEntityManager();
        String sql = "select CAST(p.id as text) as kluc ,COALESCE(prev.nazov ||' '||prev.mesto,'')    as hodnota from poberatelia as p " +
                " left join prevadzky as prev on prev.poberatel_id=p.id " +
                " left join firmy as firm on firm.id=prev.firma_id " +
                " group by CAST(p.id as text),COALESCE(prev.nazov ||' '||prev.mesto,'')  " ;

        Query query = em1.createNativeQuery(sql);

        List result1 = query.getResultList();
        Map<String, String> vysledok = NastrojePoli.<String, Double>prerobListNaMapu3(result1);
        emf.close();
        return vysledok;

    }
    private static Map<String, String> vratPoberatelPopisy(LocalDate dod, LocalDate ddo) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");

        EntityManager em1 = emf.createEntityManager();
        String sql = "select CAST(p.poberatel_id as text) as kluc ,COALESCE(pob.mesto,'')    as hodnota from polozkydokladu as p " +
                " join doklady as d on d.id=p.doklad_id " +
                " join poberatelia as pob on pob.id=p.poberatel_id " +
                " where d.datum>=? and d.datum<=? " +
                " and d.typdokladu='DAVKA' " +
                " and d.stavdokladu='POTVRDENY' " +
                " group by CAST(p.poberatel_id as text),COALESCE(pob.mesto,'')  " ;

        Query query = em1.createNativeQuery(sql);
        query.setParameter(1, dod);
        query.setParameter(2, ddo);

        List result1 = query.getResultList();
        Map<String, String> vysledok = NastrojePoli.<String, Double>prerobListNaMapu3(result1);
        emf.close();
        return vysledok;

    }
    private static Map<String, String> vratPoberatelPopisyZPob() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");

        EntityManager em1 = emf.createEntityManager();
        String sql = "select CAST(p.id as text) as kluc ,COALESCE(p.mesto,'')    as hodnota from poberatelia as p " +
                " group by CAST(p.id as text),COALESCE(p.mesto,'')  " ;

        Query query = em1.createNativeQuery(sql);

        List result1 = query.getResultList();
        Map<String, String> vysledok = NastrojePoli.<String, Double>prerobListNaMapu3(result1);
        emf.close();
        return vysledok;

    }

    private static Map<String, Double> vratKonecnyStav(LocalDate dod, LocalDate ddo) {
        EntityManager em1;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em1 = emf.createEntityManager();
        String sql = "select CAST(p.poberatel_id as text) as kluc ,sum(p.body) as hodnota from polozkydokladu as p " +
                " join doklady as d on d.id=p.doklad_id " +
                " where date(d.datum)<=date('" + ddo + "') AND d.stavdokladu='POTVRDENY'" +
                "    group by CAST(p.poberatel_id as text) " +
                "    having sum(p.body)<>0 ";

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
        String sql = "select CAST(p.poberatel_id as text) as kluc,sum(p.body) as hodnota from polozkydokladu as p " +
                " join doklady as d on d.id=p.doklad_id " +
                " where date(d.datum)>=date('" + dod + "') and date(d.datum)<=date('" + ddo + "') " +
                " and d.typdokladu='INTERNY_DOKLAD' " +
                " and d.stavdokladu='POTVRDENY' " +
                " group by CAST(p.poberatel_id as text) " +
                " having sum(p.body)<>0";

        Query query = em1.createNativeQuery(sql);

//        result1= em1.createNativeQuery(sql,Zaznam.class).getResultList();
        List result1 = query.getResultList();
        Map<String, Double> vysledok = NastrojePoli.<String, Double>prerobListNaMapu2(result1);
        emf.close();

        return vysledok;


    }

    private static Map<String, Double> vratBodyRegistracia(LocalDate dod, LocalDate ddo) {
        EntityManager em1;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em1 = emf.createEntityManager();
        String sql = "select CAST(p.poberatel_id as text) as kluc,sum(p.body) as hodnota from polozkydokladu as p " +
                " join doklady as d on d.id=p.doklad_id " +
                " where date(d.datum)>=date('" + dod + "') and date(d.datum)<=date('" + ddo + "') " +
                " and d.typdokladu='REGISTRACIA' " +
                " and d.stavdokladu='POTVRDENY' " +
                " group by CAST(p.poberatel_id as text) " +
                " having sum(p.body)<>0";

        Query query = em1.createNativeQuery(sql);

//        result1= em1.createNativeQuery(sql,Zaznam.class).getResultList();
        List result1 = query.getResultList();
        Map<String, Double> vysledok = NastrojePoli.<String, Double>prerobListNaMapu2(result1);
        emf.close();

        return vysledok;


    }

    private static Map<String, Double> vratBodyPrevod(LocalDate dod, LocalDate ddo) {
        EntityManager em1;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em1 = emf.createEntityManager();
        String sql = "select CAST(p.poberatel_id as text) as kluc,sum(p.body) as hodnota from polozkydokladu as p " +
                " join doklady as d on d.id=p.doklad_id " +
                " where date(d.datum)>=date('" + dod + "') and date(d.datum)<=date('" + ddo + "') " +
                " and d.typdokladu='PREVOD' " +
                " and d.stavdokladu='POTVRDENY' " +
                " group by CAST(p.poberatel_id as text) " +
                " having sum(p.body)<>0";

        Query query = em1.createNativeQuery(sql);

//        result1= em1.createNativeQuery(sql,Zaznam.class).getResultList();
        List result1 = query.getResultList();
        Map<String, Double> vysledok = NastrojePoli.<String, Double>prerobListNaMapu2(result1);
        emf.close();

        return vysledok;
    }

    private static Map<String, Double> vratBodyOdmeny(LocalDate dod, LocalDate ddo) {
        EntityManager em1;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em1 = emf.createEntityManager();
        String sql = "select CAST(p.poberatel_id as text) as kluc,sum(p.body) as hodnota from polozkydokladu as p " +
                " join doklady as d on d.id=p.doklad_id " +
                " where date(d.datum)>=date('" + dod + "') and date(d.datum)<=date('" + ddo + "') " +
                " and d.typdokladu='ODMENY' " +
                " and d.stavdokladu='POTVRDENY' " +
                " group by CAST(p.poberatel_id as text) " +
                " having sum(p.body)<>0";

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
        String sql = "select CAST(p.poberatel_id as text) as kluc ,sum(p.body) as hodnota from polozkydokladu as p " +
                " join doklady as d on d.id=p.doklad_id " +
                " where d.datum>=? and d.datum<=? " +
                " and d.typdokladu='DAVKA' " +
                " and d.stavdokladu='POTVRDENY' " +
                " group by CAST(p.poberatel_id as text)  " +
                " having sum(p.body)<>0";
//        String sql="select CAST(p.poberatel_id as text)as kluc ,sum(p.body) as hodnota from polozkydokladu as p " +
//                " join doklady as d on d.id=p.doklad_id " +
//                " where date(d.datum)>=date('"+dod+"') and date(d.datum)<=date('"+ddo+"') " +
//                " and d.typdokladu='DAVKA' " +
//                " and d.stavdokladu='POTVRDENY' " +
//                " group by p.poberatel_id " +
//                " having sum(p.body)>0";

        //Query query = em1.createNativeQuery(sql, Zaznam.class);
        Query query = em1.createNativeQuery(sql);
        query.setParameter(1, dod);
        query.setParameter(2, ddo);

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
        String sql = "select CAST(p.poberatel_id as text) as kluc,sum(p.body) as hodnota from polozkydokladu as p " +
                " join doklady as d on d.id=p.doklad_id " +
                " where date(d.datum)<date('" + dod + "') AND d.stavdokladu='POTVRDENY'" +
                "    group by CAST(p.poberatel_id as text) " +
                "    having sum(p.body)<>0 ";

//        result1= em1.createNativeQuery(sql,Zaznam.class).getResultList();
//        vysledok= NastrojePoli.prerobListNaMapu(result1);
        Query query = em1.createNativeQuery(sql);
        List result1 = query.getResultList();
        Map<String, Double> vysledok = NastrojePoli.<String, Double>prerobListNaMapu2(result1);

        emf.close();
        return vysledok;


    }


}
