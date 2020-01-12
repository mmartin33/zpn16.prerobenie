package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.log4j.Logger;
import sk.zpn.domena.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class FirmaVelkoskladuNastroje {

    private static final Logger logger = Logger.getLogger(FirmaVelkoskladuNastroje.class);


    public static List<Firma> zoznamFiriem(Firma firma) {
        List<Firma> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<Firma> q = em.createNamedQuery("FirmaVelkoskladu.getFirmu", Firma.class);
        q.setParameter("id", firma.getId());
        u = q.getResultList();

        return u;
    }
    public static FirmaVelkoskladu firmaVelkoskladu(Firma velkosklad,Firma partner) {

        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<FirmaVelkoskladu> q = em.createNamedQuery("FirmaVelkoskladu.getId", FirmaVelkoskladu.class);
        q.setParameter("id_velkoskladu", velkosklad.getId());
        q.setParameter("id_odberatela", partner.getId());
        List results = q.getResultList();
        if (results.isEmpty()) return null;
        return (FirmaVelkoskladu) results.get(0);

    }

    public static FirmaVelkoskladu uloz(FirmaVelkoskladu fv) {

        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");

        fv.setId(null);
        fv.setKedy(new Date());
        fv.setKto(UzivatelNastroje.getPrihlasenehoUzivatela().getId());
        em.getTransaction().begin();
        em.persist(fv);
        em.getTransaction().commit();

        return fv;


    }

    public static FirmaVelkoskladu existuje(FirmaVelkoskladu fv) {


            EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
            TypedQuery<FirmaVelkoskladu> q = em.createNamedQuery("FirmaVelkoskladu.getId", FirmaVelkoskladu.class)
                    .setParameter("id_velkoskladu", fv.getVelkosklad().getId())
                    .setParameter("id_odberatela",fv.getOdberatel().getId());


            List<FirmaVelkoskladu> results = q.getResultList();
            if (results.isEmpty()) return null;
            return (FirmaVelkoskladu) results.get(0);

    }

    public static boolean existujeFirmaVelkoskladuPodlaIcoFirmy(Firma velkosklad,String icoFirmy ) {


            EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
            TypedQuery<FirmaVelkoskladu> q = em.createNamedQuery("FirmaVelkoskladu.getFirmuPodlaIco", FirmaVelkoskladu.class)
                    .setParameter("id", velkosklad.getId())
                    .setParameter("ico",icoFirmy);


            List<FirmaVelkoskladu> results = q.getResultList();
            if (results.isEmpty()) return false;
            return true;



    }


    public static void zmazFirmuVelkoskladu(Firma oznacenaFirma, Firma velkosklad) {
        FirmaVelkoskladu fb=firmaVelkoskladu(velkosklad,oznacenaFirma);
        if (fb==null)
            return;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");

        em.getTransaction().begin();
        em.remove(fb);
        em.getTransaction().commit();

    }



}
