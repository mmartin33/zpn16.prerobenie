package sk.zpn.domena.statistiky;


import sk.zpn.domena.*;
import sk.zpn.zaklad.model.PolozkaDokladuNastroje;

import javax.persistence.*;
import java.math.BigInteger;


@SqlResultSetMapping(name = "mapovanieZoznamupohybov",
        entities = {
                @EntityResult(entityClass = ZoznamPohybov.class, fields = {
                        @FieldResult(name = "kluc", column = "kluc"),
                })}
)

@Entity

public class ZoznamPohybov {
    @Id
    @Column(name = "kluc")
    private Long kluc;
    private Doklad doklad;
    private Poberatel poberatel;
    private Prevadzka prevadzka;
    private Firma firma;
    private PolozkaDokladu polozkaDokladu;

    public ZoznamPohybov(Long kluc) {
        this.polozkaDokladu= PolozkaDokladuNastroje.getPolozkaDokladu(kluc).get();
        this.kluc = kluc;
        this.poberatel = polozkaDokladu.getPoberatel();
        this.prevadzka = polozkaDokladu.getPrevadzka();
        this.firma = prevadzka.getFirma();
        this.doklad = polozkaDokladu.getDoklad();

    }

    public ZoznamPohybov() {
    }

    public Long getKluc() {
        return kluc;
    }

    public void setKluc(Long kluc) {
        this.kluc = kluc;
    }

    public Doklad getDoklad() {
        return doklad;
    }
    public String getDokladCislo() {
        return doklad.getCisloDokladu();
    }
    public String getFromatovanyDatum() {
        return doklad.getFormatovanyDatum();
    }


    public String getDokladTyp() {
        return String.valueOf(doklad.getTypDokladu());
    }

    public void setDoklad(Doklad doklad) {
        this.doklad = doklad;
    }

    public Poberatel getPoberatel() {
        return poberatel;
    }
    public String getPoberatelMeno() {
        return poberatel.getPoberatelMenoAdresa();
    }

    public void setPoberatel(Poberatel poberatel) {
        this.poberatel = poberatel;
    }

    public Prevadzka getPrevadzka() {
        return prevadzka;
    }
    public String getPrevadzkaPopisne() {
        return prevadzka.getPrevadzkaPopisne();
    }

    public void setPrevadzka(Prevadzka prevadzka) {
        this.prevadzka = prevadzka;
    }

    public Firma getFirma() {
        return firma;
    }
    public String getFirmaNazov() {
        return firma.getNazov();
    }

    public void setFirma(Firma firma) {
        this.firma = firma;
    }

    public  BigInteger getBodyBigInteger(ZoznamPohybov zoznamPohybov) {
        return polozkaDokladu.getBody().toBigInteger() ;
    }
    public String getHtmlBody() {
    String text=null;
    if (polozkaDokladu.getBody().signum()==-1)
            return  "<font size=\"4\" color=\"red\"> <b> "+polozkaDokladu.getBody()+" <b> </font>";
    else
            return "<font size=\"4\" color=\"green\"> <b> "+polozkaDokladu.getBody()+" <b> </font>";
}
}
