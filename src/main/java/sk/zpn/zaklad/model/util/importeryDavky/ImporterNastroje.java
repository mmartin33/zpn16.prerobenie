package sk.zpn.zaklad.model.util.importeryDavky;

import org.apache.commons.lang.StringUtils;
import sk.zpn.domena.Firma;
import sk.zpn.domena.FirmaProdukt;
import sk.zpn.zaklad.model.FirmaProduktNastroje;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.model.VypoctyUtil;

import java.math.BigDecimal;
import java.util.Map;

public class ImporterNastroje {

    public static int vratBodyZaIcoAKit(String ico, String kit, BigDecimal mnozstvo, Firma velkosklad, String ciarovyKod, Map<String, FirmaProdukt> katKit) {
        int body = 0;
        FirmaProdukt fp = null;
        BigDecimal koeficientMostikovy = BigDecimal.ONE;
        BigDecimal kusyProduktove = BigDecimal.ZERO;
        BigDecimal bodyProduktove = BigDecimal.ZERO;
        boolean nasloSaNieco=false;
        if (katKit == null) {
            fp = FirmaProduktNastroje.getFirmaProduktPreImport(velkosklad,
                    ParametreNastroje.nacitajParametre().getRok(),
                    kit,
                    ciarovyKod);
            if (fp != null) {
                nasloSaNieco=true;
                koeficientMostikovy = fp.getKoeficient();
                kusyProduktove = fp.getProdukt().getKusy();
                bodyProduktove = fp.getProdukt().getBody();

            }
        } else {
            FirmaProdukt hodnota = katKit.get(kit + "-" + ParametreNastroje.nacitajParametre().getRok());
            if (hodnota!=null) {


                koeficientMostikovy = hodnota.getKoeficient();
                kusyProduktove = hodnota.getProdukt().getKusy();
                bodyProduktove = hodnota.getProdukt().getBody();
                nasloSaNieco=true;
            }

        }
        if ((nasloSaNieco)&&(StringUtils.isNotBlank(ico))) {
            //BigDecimal mnozstvoPreBody = zaznam.getMnozstvo().multiply(fp.getKoeficient());
            body = VypoctyUtil.vypocitajBody(
                    mnozstvo,
                    koeficientMostikovy,
                    kusyProduktove,
                    bodyProduktove);
        }
        return body;
    }
}

