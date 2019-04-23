package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.log4j.Logger;
import sk.zpn.domena.LogPrihlasenia;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class LogPrihlaseniaNastroje {

    private static final Logger logger = Logger.getLogger(LogPrihlaseniaNastroje.class);

    public static List<LogPrihlasenia> zoznam(){
        List<LogPrihlasenia> l = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<LogPrihlasenia> q = em.createNamedQuery("LogPrihlasenia.getAll", LogPrihlasenia.class);

        l =  q.getResultList();

        return l;
    }



    public static void uloz() {
        LogPrihlasenia log = new LogPrihlasenia();
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        log.setId((long) 0);
        log.setKedy(new Date());
        log.setKto(UzivatelNastroje.getPrihlasenehoUzivatela().getId());

        em.getTransaction().begin();
        em.merge(log);
        em.getTransaction().commit();

    }



}
