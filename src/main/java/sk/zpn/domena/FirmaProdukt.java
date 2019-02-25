package sk.zpn.domena;



import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity(name = "firma_produkt")
@NamedQueries(value = {
    @NamedQuery(name = "FirmaProdukt.getPodlaNazvuFirmy", query = "SELECT fp FROM firma_produkt fp " +
        "JOIN fp.firma firmy " +
        "JOIN fp.produkt produkt " +
        "WHERE firmy.nazov =:nazov")
})
@IdClass(FirmaProduktId.class)
public class FirmaProdukt {

    @Id
    @ManyToOne
    @JoinColumn(name = "firma_id", referencedColumnName = "id", nullable = true)
    private Firma firma;

    @Id
    @ManyToOne
    @JoinColumn(name = "produkt_id", referencedColumnName = "id", nullable = true)
    private Produkt produkt;

    @Column(name = "kit")
    @Pattern(regexp = "[a-z0-9._%+-]$", message = "Zly kod")
    private String kit;

    @Column(name = "rok")
    private String rok;

    @Column(name = "koeficient")
    private String koeficient;

    public Firma getFirma() {
        return firma;
    }

    public void setFirma(Firma firma) {
        this.firma = firma;
    }

    public Produkt getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    public String getKit() {
        return kit;
    }

    public void setKit(String kit) {
        this.kit = kit;
    }

    public String getRok() {
        return rok;
    }

    public void setRok(String rok) {
        this.rok = rok;
    }

    public String getKoeficient() {
        return koeficient;
    }

    public void setKoeficient(String koeficient) {
        this.koeficient = koeficient;
    }
}





