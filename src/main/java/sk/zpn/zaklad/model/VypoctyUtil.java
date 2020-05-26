package sk.zpn.zaklad.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class VypoctyUtil {
    public static int vypocitajBody(BigDecimal mnozstvo, BigDecimal koeficient, BigDecimal kusy, BigDecimal body) {
        //System.out.println( "mnozstvo zadane:"+mnozstvo+" koeficient velkoskladu "+  koeficient+" kusy na produkte"+ kusy+ " body na produkte"+  body);
        if ((mnozstvo==null)  || (mnozstvo.signum()==0))
            return 0;
        if ((koeficient==null)  || (koeficient.signum()==0))
            return 0;
        if ((kusy==null)  || (kusy.signum()==0))
            return 0;
        if ((body==null)  || (body.signum()==0))
            return 0;

        int  upraveneMnozstvo=mnozstvo
                .multiply(koeficient)
                .divide(kusy,2,  RoundingMode.HALF_UP)
                .intValue();
        System.out.println( "vysledok:"+  upraveneMnozstvo*body.intValue());
        return upraveneMnozstvo*body.intValue();

    }



}
