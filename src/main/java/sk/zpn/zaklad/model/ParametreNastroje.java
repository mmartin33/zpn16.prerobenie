package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.log4j.Logger;
import sk.zpn.domena.Parametre;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class ParametreNastroje {

    private static final Logger logger = Logger.getLogger(Parametre.class);

    public static Parametre ulozParametre(Parametre parametre) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");

        em.getTransaction().begin();
        em.persist(parametre);
        em.getTransaction().commit();
        return parametre;
    }

    public static Parametre nacitajParametre() {
        try {
            EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
            TypedQuery<Parametre> q = em.createNamedQuery("Parametre.get", Parametre.class);
            return q.getSingleResult();
        } catch (NoResultException e){
            logger.error("No parameters found", e);
            return createDefaultParameters();
        }
    }

    private static Parametre createDefaultParameters() {
        return new Parametre().setRok("2019");
    }



}
