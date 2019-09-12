package sk.zpn.nastroje;

import com.google.common.collect.Maps;
import org.apache.commons.lang.math.NumberUtils;
import sk.zpn.domena.statistiky.Zaznam;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class NastrojePoli {
    public static Map<String, BigDecimal> prerobListNaMapu(List<Zaznam> list) {
        Map<String, BigDecimal> map = Maps.newHashMap();
        for (Zaznam zaznam : list) {
            map.put(zaznam.getKluc(), zaznam.getHodnota() );

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
