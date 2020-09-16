package sk.zpn.nastroje;

import com.google.common.collect.Maps;
import org.apache.commons.lang.math.NumberUtils;
import sk.zpn.domena.FirmaProdukt;
import sk.zpn.domena.statistiky.Zaznam;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.model.ProduktyNastroje;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public abstract class NastrojePoli {
    public static Map<String, BigDecimal> prerobListNaMapu(List<Zaznam> list) {
        Map<String, BigDecimal> map = Maps.newHashMap();
        for (Zaznam zaznam : list) {
            map.put(zaznam.getKluc(), zaznam.getHodnota() );

        }
        return map;
    }



    public static Map<String, String> prerobListNaMapu3(List<Object[]> list) {
        Map<String, String> map = Maps.newHashMap();
        String text;
        for (Object[] i : list) {
                text=(map.get((String) i[0])!=null?map.get((String) i[0]):"");

                map.put((String) i[0], text+"\r\n"+(String) i[1]);

        }
        return map;
    }
    public static Map<String, String> prerobListNaMapuStringovu(List<Object[]> list) {
        Map<String, String> map = Maps.newHashMap();

        for (Object[] i : list) {


                map.put((String) i[0], (String) i[1]);

        }
        return map;
    }
    public static Map<String, FirmaProdukt> prerobListNaMapuFrimaProdukt(List<Object[]> list) {
        Map<String, FirmaProdukt> map = Maps.newHashMap();
        FirmaProdukt fp=null;
        for (Object[] i : list) {

                String hodnota = (String) i[1];

                if (hodnota!=null) {
                    String[] poleHodnot = hodnota.split("-", 3);
                    fp=new FirmaProdukt();
                    fp.setKoeficient(new BigDecimal(poleHodnot[0]));
                    fp.setProdukt(ProduktyNastroje.getProduktPodlaID(new Long(poleHodnot[1])));


                    map.put((String) i[0], fp);
                }

        }
        return map;
    }
    public static <K, V> Map<K, V> prerobListNaMapu2(List<Object[]> list) {
        Map<K, V> map = Maps.newHashMap();
        int hodnota;
        BigDecimal bgHodnota;
        for (Object[] i : list) {
            //hodnota= ((V) i[1]);

            map.put((K) i[0], (V) i[1]);


        }
        return map;
    }

}
