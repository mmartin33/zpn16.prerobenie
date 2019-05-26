package sk.zpn.domena.importy;

import sk.zpn.domena.Firma;

import java.util.Date;

public class ParametreImportu {
    private Firma firma;
    private Date datum;

    public ParametreImportu(Firma firma,Date datum) {
        this.firma = firma;
        this.datum=datum;
    }

    public Firma getFirma() {
        return firma;
    }

    public Date getDatum() {
        return datum;
    }
}
