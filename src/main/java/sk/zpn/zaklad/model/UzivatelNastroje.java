package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.log4j.Logger;
import sk.zpn.authentification.HesloNastroje;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.domena.Uzivatel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class UzivatelNastroje {

    private static final Logger logger = Logger.getLogger(UzivatelNastroje.class);

    public static boolean overUzivatela(String meno, String heslo){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Uzivatel> q = em.createNamedQuery("Uzivatel.getPodlaMena", Uzivatel.class);
        q.setParameter("meno", meno);
        Optional<Uzivatel> uzivatel = Optional.ofNullable(q.getSingleResult());

        try {
            if  (uzivatel.isPresent() && HesloNastroje.check(meno, uzivatel.get().getHeslo())) {
                VaadinSession.getCurrent().setAttribute("id_uzivatela", uzivatel.get().getId());
                VaadinSession.getCurrent().setAttribute("meno", uzivatel.get().getMeno());
                logger.info(String.format("Uzivatel %s bol overeny", uzivatel.get().getMeno()));
            }
        } catch (Exception e) {
            logger.error("Heslo nebolo mozne overit", e);
        }
        return false;
    }


    public static Boolean prazdnyUzivatelia(){
        boolean prazdny=true;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        Query query = em.createNativeQuery("SELECT  count(*) FROM  uzivatelia");
        long result = (long)query.getSingleResult();
        System.out.println("pocet uzivatelov:"+result);
        if (result>0) {
            System.out.println("nie je prazdny");
            prazdny = false;
        }

        return prazdny;
    }

    public static Optional<Uzivatel> getUzivatela(Long id){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Uzivatel> q = em.createNamedQuery("Uzivatel.get", Uzivatel.class);
        q.setParameter("id", id);
        return Optional.ofNullable(q.getSingleResult());
    }
    public static Optional<Uzivatel> getUzivatela(String login){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Uzivatel> q = em.createNamedQuery("Uzivatel.getPodlaMena", Uzivatel.class);
        q.setParameter("meno", login);
        return Optional.ofNullable(q.getSingleResult());
    }
    public static Uzivatel ulozUzivatela(Uzivatel u){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        if (u.isNew())
            u.setId((long)0);
        System.out.println("Ulozeny uzivate:"+u.getMeno());
        em.getTransaction().begin();
        em.persist(u);
        em.getTransaction().commit();
        return u;


    }
    public static void zmazUzivatela(Uzivatel u){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Vymazany uzivate:"+u.getMeno());
        em.getTransaction().begin();
        em.remove(u);
        em.getTransaction().commit();



    }
    public static Optional<TypUzivatela> TypUzivatela() {
        Optional<Uzivatel> uzivatel = getUzivatela((Long) VaadinSession.getCurrent().getAttribute("id_uzivatela"));
        return uzivatel.map(Uzivatel::getTypUzivatela);
    }
    public static List<Uzivatel> zoznamUzivatelov(){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Uzivatel> q = em.createNamedQuery("Uzivatel.getAll", Uzivatel.class);
        return q.getResultList();
    }
}
