package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.log4j.Logger;
import sk.zpn.domena.Doklad;
import sk.zpn.domena.PolozkaDokladu;
import sk.zpn.domena.TypDokladu;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class PolozkaDokladuNastroje {

    private static final Logger logger = Logger.getLogger(PolozkaDokladuNastroje.class);


    public static Optional<PolozkaDokladu> getPolozkaDokladu(Long id){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<PolozkaDokladu> q = em.createNamedQuery("PolozkaDokladu.get", PolozkaDokladu.class);
        q.setParameter("id", id);
        return Optional.ofNullable(q.getSingleResult());
    }
    public static PolozkaDokladu ulozpolozkuDokladu(PolozkaDokladu d){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        if (d.isNew())
            d.setId((long)0);
        System.out.println("Ulozena polozka");
        em.getTransaction().begin();
        em.persist(d);
        em.getTransaction().commit();
        return d;


    }
    public static void zmazPolozkyDoklady(PolozkaDokladu d){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Vymazaná položka");
        em.getTransaction().begin();
        em.remove(d);
        em.getTransaction().commit();



    }
    public static List<PolozkaDokladu> zoznamPoloziekDokladov(Doklad d){
        //todo doplnit filter na doklad aj na domene
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<PolozkaDokladu> q = em.createNamedQuery("PolozkaDokladu.getAll", PolozkaDokladu.class);
        return q.getResultList();
    }
}
