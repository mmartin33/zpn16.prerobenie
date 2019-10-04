package sk.zpn.zaklad.model.util.importeryDavky;

import org.apache.commons.lang.StringUtils;
import sk.zpn.domena.Firma;
import sk.zpn.domena.FirmaProdukt;
import sk.zpn.zaklad.model.FirmaProduktNastroje;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.model.VypoctyUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ImporterNastroje {

    public static int vratBodyZaIcoAKit(String ico, String kit, BigDecimal mnozstvo, Firma velkosklad) {
        int body=0;
        FirmaProdukt fp = null;
        fp = FirmaProduktNastroje.getFirmaProduktPreImport(velkosklad,
                ParametreNastroje.nacitajParametre().getRok(),
                kit);

        if ((fp != null) && (StringUtils.isNotBlank(ico))){
            //BigDecimal mnozstvoPreBody = zaznam.getMnozstvo().multiply(fp.getKoeficient());
            body = VypoctyUtil.vypocitajBody(
                    mnozstvo,
                    fp.getKoeficient(),
                    fp.getProdukt().getKusy(),
                    fp.getProdukt().getBody());
        }
        return body;
    }
}

