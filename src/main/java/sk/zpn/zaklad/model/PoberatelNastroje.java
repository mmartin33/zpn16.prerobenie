package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.Poberatel;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class PoberatelNastroje {
    public static List<Poberatel> zoznamPoberatelov(){
        List<Poberatel> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Poberatel> q = em.createNamedQuery("Poberatel.getAll", Poberatel.class);

        u =  q.getResultList();

        return u;
    }

    public static Poberatel ulozPoberatela(Poberatel f){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        if (f.isNew())
            f.setId((long)0);
        em.getTransaction().begin();
        em.persist(f);
        em.getTransaction().commit();
        return f;


    }
    public static void zmazPoberatela(Poberatel f){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Vymazany poberatel:"+f.getPriezvisko());
        em.getTransaction().begin();
        em.remove(f);
        em.getTransaction().commit();



    }



}
