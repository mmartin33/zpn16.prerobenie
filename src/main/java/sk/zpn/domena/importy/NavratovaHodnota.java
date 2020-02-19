package sk.zpn.domena.importy;

import com.sun.org.apache.bcel.internal.classfile.Constant;
import sk.zpn.domena.PolozkaDokladu;

public class NavratovaHodnota{
    PolozkaDokladu polozkaDokladu;
    int chyba;
    String kit;
    public static int NENAJEDENY_KIT=1;
    public static int NENAJEDENA_FIRMA=2;
    public static int PRAZDNE_ICO=3;
    public static int MALO_BODOV=4;
    public static int NEPOVOLENA_FIRMA=5;
    public static int NEURCENY_KOEFICIENT=6;
    public static int NIC=0;


    public NavratovaHodnota(PolozkaDokladu polozkaDokladu,int chyba) {
        this.polozkaDokladu = polozkaDokladu;
        this.chyba = chyba;
    }
    public NavratovaHodnota(PolozkaDokladu polozkaDokladu,int chyba,String kit) {
        this.polozkaDokladu = polozkaDokladu;
        this.chyba = chyba;
        this.kit=kit;
    }

    public NavratovaHodnota() {
        this.polozkaDokladu = null;
        this.chyba=this.NIC;
    }

    public PolozkaDokladu getPolozkaDokladu() {
        return polozkaDokladu;
    }

    public int getChyba() {
        return chyba;
    }
}
