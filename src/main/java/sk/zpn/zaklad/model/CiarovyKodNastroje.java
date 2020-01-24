package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.eclipse.persistence.jpa.rs.util.list.SingleResultQuery;
import sk.zpn.domena.CiarovyKod;
import sk.zpn.domena.Doklad;
import sk.zpn.domena.Produkt;
import sk.zpn.domena.StavDokladu;
import sk.zpn.domena.log.TypLogovanejHodnoty;
import sk.zpn.domena.log.TypUkonu;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class CiarovyKodNastroje {
    public static List<CiarovyKod> zoznam(Produkt produkt) {
        List<CiarovyKod> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        String sql = "SELECT c FROM ciarovyKod c " +
                (produkt != null ? " join c.produkt as p   where  p.id=:id_produktu " : "");

        TypedQuery<CiarovyKod> q = em.createQuery(sql, CiarovyKod.class);

        if (produkt != null)
            q.setParameter("id_produktu", produkt.getId());
        u = q.getResultList();

        return u;
    }

    public static void zmazCiarovyKod(CiarovyKod d) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        LogAplikacieNastroje.uloz(TypLogovanejHodnoty.CIAROVY_KOD, TypUkonu.VYMAZ,d.getTextLog());
        em.getTransaction().begin();
        em.remove(d);
        em.getTransaction().commit();


    }

    public static boolean existuje(String ciarovyKOd) {
        List<CiarovyKod> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        String sql = "SELECT c FROM ciarovyKod c " +
                    "  where  c.ciarovyKod=:ciarovy_kod ";

        TypedQuery<CiarovyKod> q = em.createQuery(sql, CiarovyKod.class);
        q.setParameter("ciarovy_kod", ciarovyKOd);
        u = q.getResultList();
        if (u.size() == 0)
            return false;
        return true;
    }

    public static CiarovyKod uloz(CiarovyKod c) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypUkonu tu = TypUkonu.OPRAVA;
        if (c.getId() == null) {
            tu = TypUkonu.PRIDANIE;
            c.setId((long) 0);
            c.setKedy(new Date());
            c.setKto(UzivatelNastroje.getPrihlasenehoUzivatela().getId());
            em.getTransaction().begin();
            em.persist(c);
        } else {
            em.getTransaction().begin();
            em.merge(c);
        }
        em.getTransaction().commit();
        LogAplikacieNastroje.uloz(TypLogovanejHodnoty.CIAROVY_KOD, tu, c.getTextLog());

        return c;

    }
}
