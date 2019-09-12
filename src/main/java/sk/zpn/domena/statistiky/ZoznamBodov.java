package sk.zpn.domena.statistiky;



import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.text.SimpleDateFormat;


@SqlResultSetMapping(name="mapovanieZoznamuBodov",
        entities={
                @EntityResult(entityClass= ZoznamBodov.class, fields={
                        @FieldResult(name="kluc", column="kluc"),
                        @FieldResult(name="datum", column="datum"),
                        @FieldResult(name="typDokladu", column="typ_dokladu"),
                        @FieldResult(name="poznamka", column="poznamka"),
                        @FieldResult(name="body", column="body"),
                        })}
)

@Entity

public class ZoznamBodov {
    @Id
    @Column(name = "kluc")
    private String kluc;

    @Column(name = "datum")
    private Date datum;
    @Column(name = "poznamka")
    private String poznamka;
    @Column(name = "typ_dokladu")
    private String typDokladu;
    @Column(name = "body")
    private BigDecimal body;



    public ZoznamBodov(String kluc,
                            Date datum,
                           String typDokladu,
                           String poznamka,
                           BigDecimal body)
        {

            this.kluc=kluc;
            this.datum=datum;
            this.poznamka=poznamka;
            this.body=body;
        }

    public ZoznamBodov()   {
    }

    public Date getDatum() {        return datum;    }

    public String getFormatovanyDatum() {
        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(datum);
    }


    public void setDatum(Date datum) {        this.datum = datum;    }

    public String getPoznamka() {        return poznamka;    }

    public void setPoznamka(String poznamka) {        this.poznamka = poznamka;    }

    public String getTypDokladu() {        return typDokladu;    }

    public void setTypDokladu(String typDokladu) {        this.typDokladu = typDokladu;    }

    public BigDecimal getBody() {        return body;    }


    public String getHtmlBody() {
    String text=null;
    if (getBody().signum()==-1)
        return  "<font size=\"4\" color=\"red\"> <b> "+getBody()+" <b> </font>";
    else
        return "<font size=\"4\" color=\"green\"> <b> "+getBody()+" <b> </font>";
    }

    public BigInteger getBodyBigInteger() {return this.body.toBigInteger();}

    public void setBody(BigDecimal body) {        this.body = body;    }
}
