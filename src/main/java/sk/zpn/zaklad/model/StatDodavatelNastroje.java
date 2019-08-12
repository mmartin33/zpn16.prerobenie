package sk.zpn.zaklad.model;

import sk.zpn.domena.StatistikaBodov;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;

public class StatDodavatelNastroje {

    public static void exportDoXLS(List<StatistikaBodov> statList) {

    }

    public static List<StatistikaBodov> load(LocalDate dod, LocalDate ddo,int rok) {
        List<StatistikaBodov> result1=null;
        EntityManager em1;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em1 = emf.createEntityManager();



        String sql="SELECT fir.id,fir.nazov AS nazov,"+
                "(SELECT Sum(p.body)"+
        "FROM   polozkydokladu AS p "+
        "JOIN doklady AS d ON d.id = p.doklad_id "+
        "join produkty as pr on p.produkt_id=pr.id "+
        "join firmy as fi on fi.id=pr.firma_id "+
        "WHERE  Date(d.datum) < Date('"+dod+"') " +
        "AND fi.id = fir.id "+
        "AND d.stavdokladu = 'POTVRDENY') AS pociatocny_stav,"+
        "(SELECT Sum(p.body) "+
        "FROM   polozkydokladu AS p "+
        "JOIN doklady AS d ON d.id = p.doklad_id "+
        "join produkty as pr on p.produkt_id=pr.id "+
        "join firmy as fi on fi.id=pr.firma_id "+

        "WHERE  Date(d.datum) >= Date('"+dod+"') "+
        "AND Date(d.datum) <= Date('"+ddo+"') "+
        "AND fi.id = fir.id "+

        "AND d.stavdokladu = 'POTVRDENY') AS body_za_predaj,"+
        "(SELECT Sum(p.body) "+
        "FROM   polozkydokladu AS p "+
        "JOIN doklady AS d ON d.id = p.doklad_id " +
        "join produkty as pr on p.produkt_id=pr.id "+
        "join firmy as fi on fi.id=pr.firma_id "+
        "WHERE  Date(d.datum) <= Date('"+ddo+"') "+
        "AND fi.id = fir.id "+

        "AND d.stavdokladu = 'POTVRDENY')     AS konecny_stav "+

        "FROM produkty AS prod "+
        "join firmy as fir on fir.id=prod.firma_id "+
        "where prod.rok='"+rok+"' "+
        "GROUP  BY fir.id,"+
        "fir.nazov";




            result1  = em1.createNativeQuery(sql,  "mapovanieVysledkuBodov").getResultList();
            em1.close();
            emf.close();

        return result1;

    }
}
