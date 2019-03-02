package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.log4j.Logger;
import sk.zpn.domena.Firma;
import sk.zpn.domena.FirmaProdukt;
import sk.zpn.domena.Produkt;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class FirmaProduktNastroje {

    private static final Logger logger = Logger.getLogger(FirmaProduktNastroje.class);

    public static List<FirmaProdukt> getFirmaProduktPodlaNazvuFirmy(String nazov) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<FirmaProdukt> q = em.createNamedQuery("FirmaProdukt.getPodlaNazvuFirmy", FirmaProdukt.class)
            .setParameter("nazov", nazov);
        return q.getResultList();
    }

    public static FirmaProdukt ulozFirmaProdukt(FirmaProdukt firmaProdukt) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");

        em.getTransaction().begin();
        em.persist(firmaProdukt);
        em.getTransaction().commit();
        return firmaProdukt;
    }

    public boolean vygenerujSablonu(String nazovFirmy) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        List<Produkt> produkty = ProduktyNastroje.zoznamProduktovZaRok();
        Optional<Firma> firma = FirmaNastroje.prvaFirmaPodlaNazvu(nazovFirmy);
        if(!firma.isPresent()) {
            logger.error(String.format("Firma s nazvom %s neexistuje", nazovFirmy));
            return false;
        }
        String rok = ParametreNastroje.nacitajParametre().getRok();
        em.getTransaction().begin();
        produkty.forEach(produkt -> {
            em.persist(new FirmaProdukt(rok, produkt, firma.get()));
        });
        em.getTransaction().commit();
        return true;
    }

}
