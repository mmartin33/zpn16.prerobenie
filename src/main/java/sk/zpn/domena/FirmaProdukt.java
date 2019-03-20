package sk.zpn.domena;



import javax.persistence.*;
import javax.validation.constraints.Pattern;


@Entity
@Table(name = "firma_produkt")
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
}





