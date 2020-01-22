package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.log4j.Logger;
import sk.zpn.domena.log.LogAplikacie;
import sk.zpn.domena.log.TypLogovanejHodnoty;
import sk.zpn.domena.log.TypUkonu;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class LogAplikacieNastroje {

    private static final Logger logger = Logger.getLogger(LogAplikacieNastroje.class);

    public static List<LogAplikacie> zoznam(){
        List<LogAplikacie> l = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<LogAplikacie> q = em.createNamedQuery("LogAplikacie.getAll", LogAplikacie.class);

        l =  q.getResultList();

        return l;
    }



    public static void uloz(TypLogovanejHodnoty typLogovanejHodnoty, TypUkonu typUkonu, String poznamka) {
        LogAplikacie log = new LogAplikacie();
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        log.setId((long) 0);
        log.setKedy(new Date());
        log.setKto(UzivatelNastroje.getPrihlasenehoUzivatela().getId());
        log.setPoznamka(poznamka);;
        log.setTypLogovanejHodnoty(typLogovanejHodnoty);
        log.setTypUkonu(typUkonu);
        em.getTransaction().begin();
        em.merge(log);
        em.getTransaction().commit();

    }



}
