package sk.zpn.domena;



import sk.zpn.zaklad.model.FirmaProduktNastroje;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity(name = "firma_produkt")
@NamedQueries(value = {
    @NamedQuery(name = "FirmaProdukt.getPodlaNazvuFirmy", query = "SELECT fp FROM firma_produkt fp " +
        "JOIN fp.firma firmy " +
        "JOIN fp.produkt produkt " +
        "WHERE firmy.nazov =:nazov"),
    @NamedQuery(name = "FirmaProdukt.getAll", query = "SELECT fp FROM firma_produkt fp " +
        "JOIN fp.firma firmy " +
        "JOIN fp.produkt produkt")
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
    private Double koeficient;

    public FirmaProdukt() {}

    public FirmaProdukt(String rok, Produkt produkt, Firma firma){
        this.kit = "";
        this.koeficient = 0D;
        this.rok =  rok;
        this.produkt = produkt;
        this.firma = firma;
    }

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

    public void setKitAndPersist(String kit) {
        this.kit = kit;
        FirmaProduktNastroje.ulozFirmaProdukt(this);
    }

    public String getRok() {
        return rok;
    }

    public void setRok(String rok) {
        this.rok = rok;
    }

    public Double getKoeficient() {
        return koeficient;
    }

    public void setKoeficient(Double koeficient) {
        this.koeficient = koeficient;
    }

    public void setKoeficientAndPersist(Double koeficient) {
        this.koeficient = koeficient;
        FirmaProduktNastroje.ulozFirmaProdukt(this);
    }
}





