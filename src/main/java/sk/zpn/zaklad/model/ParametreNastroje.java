package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.apache.log4j.Logger;
import sk.zpn.MyUI;
import sk.zpn.domena.Parametre;

import javax.persistence.*;

public class ParametreNastroje {

    private static final Logger logger = Logger.getLogger(Parametre.class);

    public static Parametre ulozParametre(Parametre parametre) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");

        em.getTransaction().begin();
        em.merge(parametre);
        em.getTransaction().commit();
        return parametre;
    }

    public static Parametre nacitajParametre() {
        EntityManager em = null;

        if (((MyUI) UI.getCurrent()).getGlobalneParametre().getParametre()!=null){
            return ((MyUI) UI.getCurrent()).getGlobalneParametre().getParametre();
        }
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
            em = emf.createEntityManager();
            TypedQuery<Parametre> q = em.createNamedQuery("Parametre.get", Parametre.class);

            return q.getSingleResult();
        } catch (NoResultException e){
            logger.error("No parameters found", e);
            return createDefaultParameters();
        }
        finally {
            em.close();

        }
    }

    private static Parametre createDefaultParameters() {
        return new Parametre().setRok("2019");
    }



}
