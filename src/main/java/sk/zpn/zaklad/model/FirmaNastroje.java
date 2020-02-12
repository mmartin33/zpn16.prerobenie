package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.Firma;
import sk.zpn.domena.log.TypLogovanejHodnoty;
import sk.zpn.domena.log.TypUkonu;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class FirmaNastroje {
    public static List<Firma> zoznamFiriem(){
        List<Firma> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<Firma> q = em.createNamedQuery("Firma.getAll", Firma.class);

        u =  q.getResultList();

        return u;
    }
    public static List<Firma> zoznamFiriemIbaVelkosklady(){
        List<Firma> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<Firma> q = em.createNamedQuery("Firma.getVelkosklady", Firma.class);

        u =  q.getResultList();

        return u;
    }
    public static List<Firma> zoznamFiriemIbaDodavatelia(){
        List<Firma> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<Firma> q = em.createNamedQuery("Firma.getDodavatelia", Firma.class);

        u =  q.getResultList();

        return u;
    }

    public static Optional<Firma> prvaFirmaPodlaNazvu(String nazov){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Firma> q = em.createNamedQuery("Firma.getPodlaNazvu", Firma.class)
                .setParameter("nazov", nazov);
        List<Firma> firmy = q.getResultList();
        return firmy.size() > 0 ? Optional.of(q.getResultList().get(0)) : Optional.empty();
    }



    public static Optional<Firma> firmaPodlaID(Long id){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Firma> q = em.createNamedQuery("Firma.getPodlaID", Firma.class)
                .setParameter("id", id);
        List<Firma> firmy = q.getResultList();
        return firmy.size() > 0 ? Optional.of(q.getResultList().get(0)) : Optional.empty();
    }



    public static Firma ulozFirmu(Firma f){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Ulozena firma:"+f.getNazov());
        em.getTransaction().begin();
        TypUkonu tu=TypUkonu.OPRAVA;
        if (f.isNew()) {
            tu=TypUkonu.PRIDANIE;
            f.setId((long) 0);
            f.setKedy(new Date());
            f.setKto(UzivatelNastroje.getPrihlasenehoUzivatela().getId());
            em.persist(f);
            }
        else
            em.merge(f);

        em.getTransaction().commit();
        LogAplikacieNastroje.uloz(TypLogovanejHodnoty.FIRMA, tu,f.getTextLog());

        return f;


    }
    public static void zmazFirmu(Firma f){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Vymazana firma:"+f.getNazov());
        if(!em.getTransaction().isActive())
            em.getTransaction().begin();
        if (!em.contains(f)) {
            f = em.merge(f);
        }
        em.remove(f);
        em.getTransaction().commit();
    }


    public static Firma firmaPodlaICOaNazvu(String ico, String nazovFirmy) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Firma> q = em.createNamedQuery("Firma.getPodlaICOaNazvu", Firma.class)
                .setParameter("nazov", nazovFirmy)
                .setParameter("ico", ico);
        Firma firma = q.getSingleResult();
        return  firma;
    }
    public static Firma firmaPodlaICO(String ico) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Firma> q = em.createNamedQuery("Firma.getPodlaICO", Firma.class)
                .setParameter("ico", ico);
        List results = q.getResultList();
        if (results.isEmpty()) return null;
        return (Firma) results.get(0);
    }
}
