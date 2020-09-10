package sk.zpn.zaklad.model;

import com.google.common.collect.Maps;
import sk.zpn.domena.Firma;

import sk.zpn.domena.statistiky.Zaznam;
import sk.zpn.nastroje.NastrojePoli;
import sk.zpn.nastroje.XlsStatPoRokochAVelkoskladoch;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class StatistikyNastroje {


    public static  Map<String, BigDecimal> vratStatistikaBodovPoRokochAVelkoskladoch(String rokOd, String rokDo) {
        Map<String, BigDecimal> predaje =Maps.newHashMap();
        List<Zaznam> result1=null;
        EntityManager em1;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em1 = emf.createEntityManager();
        String sql="select v.ico||'*'||EXTRACT(YEAR FROM d.datum) as kluc,sum(p.body) as hodnota from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id " +
                "join firmy as v on d.firma_id=v.id " +
                "where d.typdokladu='DAVKA' " +
                "and d.stavdokladu='POTVRDENY' " +
                "and EXTRACT(YEAR FROM d.datum)>='"+rokOd+"'  " +
                "and EXTRACT(YEAR FROM d.datum)<='"+rokDo+"' " +
                "group by  v.ico||'*'||EXTRACT(YEAR FROM d.datum) " +
                "order by v.ico||'*'||EXTRACT(YEAR FROM d.datum) ";


        result1= em1.createNativeQuery(sql,Zaznam.class).getResultList();

        predaje= NastrojePoli.prerobListNaMapu(result1);
        em1.close();
        emf.close();

        return predaje;


    }

    public static void statPoRokochAVelkoskladoch(String rokOd, String rokDo) {


        Map<String, BigDecimal> statistika = Maps.newHashMap();
        List<Firma> velkosklady = FirmaNastroje.zoznamFiriemIbaVelkosklady();
        statistika = vratStatistikaBodovPoRokochAVelkoskladoch(rokOd,rokDo);
        String nadpis = "Vyhodnotenie velkoskladov  za rozsah rokov od:" + rokOd + " do: " + rokDo;


        XlsStatPoRokochAVelkoskladoch.vytvorXLSBilancie(statistika,velkosklady,nadpis,rokOd,rokDo);


    }
}
