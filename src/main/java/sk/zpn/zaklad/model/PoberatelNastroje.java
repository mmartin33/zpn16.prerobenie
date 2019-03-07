package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.Poberatel;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

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

    public static Optional<Poberatel> prvyPoberatelPodlaMena(String meno){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Poberatel> q = em.createNamedQuery("Poberatel.getPodlaMena", Poberatel.class)
                .setParameter("meno", meno);
        List<Poberatel> poberatel = q.getResultList();
        return poberatel.size() > 0 ? Optional.of(q.getResultList().get(0)) : Optional.empty();
    }

    public static Optional<Poberatel> PoberatelPodlaId(Long id){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Poberatel> q = em.createNamedQuery("Poberatel.get", Poberatel.class);
        q.setParameter("id", id);
        List<Poberatel> poberatel = q.getResultList();
        System.out.println("Vybraty poberate"+ (poberatel.size() > 0 ? Optional.of(q.getResultList().get(0).getPoberatelMenoAdresa()) : Optional.empty()));
        return poberatel.size() > 0 ? Optional.of(q.getResultList().get(0)) : Optional.empty();
    }

    public static void zmazPoberatela(Poberatel f){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Vymazany poberatel:"+f.getMeno());
        em.getTransaction().begin();
        em.remove(f);
        em.getTransaction().commit();



    }



}
