package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.domena.Uzivatel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class UzivatelNastroje {
    public static Uzivatel overUzivatela(String meno, String heslo){
        System.out.println("Overenie uzivatela:" +meno+" heslo "+heslo);
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Uzivatel> q = em.createNamedQuery("Uzivatel.getPodlaMenaHesla", Uzivatel.class);
        q.setParameter("meno", meno);
        q.setParameter("heslo", heslo);
        Uzivatel lu = q.getSingleResult();

        if  (lu==null)
            return null;
        VaadinSession.getCurrent().setAttribute("id_uzivatela",lu.getId());
        VaadinSession.getCurrent().setAttribute("meno",lu.getMeno());
        System.out.println("uzivatel overeny"+VaadinSession.getCurrent().getAttribute("meno")+VaadinSession.getCurrent().getAttribute("id_uzivatela"));
        return lu;
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
