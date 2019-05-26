package sk.zpn.domena;

import com.sun.org.apache.bcel.internal.classfile.Constant;

public class NavratovaHodnota{
    PolozkaDokladu polozkaDokladu;
    int chyba;
    public static int NENAJEDENY_KIT=1;
    public static int NENAJEDENA_FIRMA=2;
    public static int NIC=0;


    public NavratovaHodnota(PolozkaDokladu polozkaDokladu,int chyba) {
        this.polozkaDokladu = polozkaDokladu;
        this.chyba = chyba;
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
