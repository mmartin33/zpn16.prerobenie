package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import sk.zpn.domena.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class FirmaProduktNastroje {

    private static final Logger logger = Logger.getLogger(FirmaProduktNastroje.class);

    public static List<FirmaProdukt> getListFirmaProduktPodlaNazvuFirmy(String nazov) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<FirmaProdukt> q = em.createNamedQuery("FirmaProdukt.getPodlaNazvuFirmy", FirmaProdukt.class)
            .setParameter("nazov", nazov);
        return q.getResultList();
    }

    public static FirmaProdukt getFirmaProduktPreImport(Firma firma, String rok, String kit,String ciarovyKod) {
        EntityManager em=null;
        List results=null;
        TypedQuery<FirmaProdukt> q=null;
        Long p=null;


        em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        q = em.createNamedQuery("FirmaProdukt.getMostikoveUdaje", FirmaProdukt.class)
            .setParameter("idFirmy", firma.getId())
            .setParameter("kit",kit)
            .setParameter("rok",rok);

        results = q.getResultList();
        if (results.isEmpty()) {
            //problem hovadsky
            p=CiarovyKodNastroje.productPodlaCiarovehoKodu(ciarovyKod);
            if (p!=null) {
                em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
                 q = em.createNamedQuery("FirmaProdukt.getMostikoveUdajePodlaProduktu", FirmaProdukt.class)
                        .setParameter("idFirmy", firma.getId())
                        .setParameter("idProduktu", p)
                        .setParameter("rok", rok);

                results = q.getResultList();
            }

            return null;
        }
        return (FirmaProdukt) results.get(0);

    }


    public static List<FirmaProdukt> getAll() {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<FirmaProdukt> q = em.createNamedQuery("FirmaProdukt.getAll", FirmaProdukt.class);
        return q.getResultList();
    }

    public static FirmaProdukt ulozFirmaProdukt(FirmaProdukt firmaProdukt) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");

        em.getTransaction().begin();
        em.merge(firmaProdukt);

        em.getTransaction().commit();
        return firmaProdukt;
    }

    public static boolean generateMissingFirmaProductItems(String nazovFirmy) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        List<Produkt> produkty = ProduktyNastroje.zoznamProduktovZaRok(null, TypProduktov.BODOVACI);
        Optional<Firma> firma = FirmaNastroje.prvaFirmaPodlaNazvu(nazovFirmy);
        List<FirmaProdukt> existujuceFirmaProduktZaznamy = FirmaProduktNastroje.getListFirmaProduktPodlaNazvuFirmy(nazovFirmy);
        produkty = filterNonYetAssignedProducts(produkty, existujuceFirmaProduktZaznamy);
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

    private static List<Produkt> filterNonYetAssignedProducts(List<Produkt> allProducts,
                            List<FirmaProdukt> existingFirmaProductItems) {
        List<Long> existingProductsIds = existingFirmaProductItems.stream()
            .map(firmaProdukt -> firmaProdukt.getProdukt().getId())
            .collect(Collectors.toList());
        return allProducts.stream()
            .filter(produkt -> !existingProductsIds.contains(produkt.getId()))
            .collect(Collectors.toList());
    }

    public static void zmazFirmaProdukt(FirmaProdukt f){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.getTransaction().begin();
        em.remove(f);
        em.getTransaction().commit();
    }

    /**
     * Returns items that do not have KIT or koeficient not filled in.
     * @param firmaProduktList input list
     * @return filtered list of FirmaProdukt
     */
    public static List<FirmaProdukt> filterInvalidRecords(List<FirmaProdukt> firmaProduktList) {
        return firmaProduktList.stream()
            .filter(firmaProdukt -> firmaProdukt.getKit().equals("") || firmaProdukt.getKoeficient().equals(0D))
            .collect(Collectors.toList());
    }

    public static void nastavAtributyPriNovej(FirmaProdukt fpEditovana) {
        fpEditovana.setRok(ParametreNastroje.nacitajParametre().getRok());
        fpEditovana.setFirma(UzivatelNastroje.getPrihlasenehoUzivatela().getFirma());
    }

    public static FirmaProdukt pridajFirmaProdukt(FirmaProdukt firmaProdukt) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");

        em.getTransaction().begin();
        em.persist(firmaProdukt);

        em.getTransaction().commit();
        return firmaProdukt;

    }

    public static void prepisKATdoKIT(List<FirmaProdukt> firmaProdukt) {
        if (firmaProdukt==null)
            return;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");

        for (FirmaProdukt fp : firmaProdukt) {

                em.getTransaction().begin();
                fp.setKit(fp.getProdukt().getKat());
                em.merge(fp);
                em.getTransaction().commit();

        }



    }



}
