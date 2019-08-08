package sk.zpn.nastroje;

import com.google.common.collect.Maps;
import sk.zpn.domena.statistiky.Zaznam;

import javax.persistence.*;
import java.math.BigDecimal;
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

}
