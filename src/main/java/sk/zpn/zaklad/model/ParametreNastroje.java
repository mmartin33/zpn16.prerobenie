package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.Parametre;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class ParametreNastroje {

    public static Parametre ulozParametre(Parametre parametre) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");

        em.getTransaction().begin();
        em.persist(parametre);
        em.getTransaction().commit();
        return parametre;
    }

    public static Parametre nacitajParametre() {
        Parametre  p=null;
        try{
            EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
            TypedQuery<Parametre> q = em.createNamedQuery("Parametre.get", Parametre.class);
            p=q.getSingleResult();
            return p;
        } catch(NoResultException e) {
            return new Parametre();
        }

    }
}
