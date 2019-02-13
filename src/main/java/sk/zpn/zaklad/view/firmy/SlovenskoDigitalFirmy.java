package sk.zpn.zaklad.view.firmy;

import sk.zpn.domena.FirmaRegistra;
import sk.zpn.domena.FirmySlovenskoDigital;

import java.util.List;

public class SlovenskoDigitalFirmy {
    List<FirmaRegistra> zaznamy;
    String nazovFirmy;
    String ico;

    public SlovenskoDigitalFirmy() {
    }

    private void naplnData() {
        FirmySlovenskoDigital sd = new FirmySlovenskoDigital();
        this.zaznamy = sd.nacitanieDat(nazovFirmy, ico, true);

    }

}
