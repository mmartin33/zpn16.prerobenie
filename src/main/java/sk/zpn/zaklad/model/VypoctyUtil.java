package sk.zpn.zaklad.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class VypoctyUtil {
    public static BigDecimal vypocitajBody(BigDecimal mnozstvo, BigDecimal koeficient, BigDecimal kusy, BigDecimal body) {
        //System.out.println( "1"+mnozstvo+"2"+  koeficient+"3"+ kusy+ "4"+  body);
        if ((mnozstvo==null)  || (mnozstvo.signum()==0))
            return new BigDecimal(0);
        if ((koeficient==null)  || (koeficient.signum()==0))
            return new BigDecimal(0);
        if ((kusy==null)  || (kusy.signum()==0))
            return new BigDecimal(0);
        if ((body==null)  || (body.signum()==0))
            return new BigDecimal(0);

        return mnozstvo
                .multiply(koeficient)
                .divide(kusy,2,  RoundingMode.HALF_UP)
                .multiply(body);
    }



}
