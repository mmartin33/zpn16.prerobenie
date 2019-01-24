package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.Uzivatel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;

public class UzivatelNastroje {
    public static Uzivatel overUzivatela(String meno, String heslo){



        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        //Query query = em.createNativeQuery("SELECT id FROM  uzivatelia  WHERE meno = ?1 and heslo = ?2");
        //TypedQuery<Uzivatel> q = em.createQuery("SELECT u from Uzivatel u WHERE u.meno =:meno and u.heslo =:heslo",Uzivatel.class);
        TypedQuery<Uzivatel> q = em.createNamedQuery("Uzivatel.getPodlaMenaHesla", Uzivatel.class);
       // TypedQuery<Long> q = em.createQuery("SELECT COUNT(u) FROM  Uzivatelia as u WHERE u.meno =:meno and u.heslo =:heslo", Long.class);

//        query.setParameter(1, meno);
//        query.setParameter(2, heslo);
        q.setParameter("meno", meno);
        q.setParameter("heslo", heslo);


//        long lu = (long) q.getSingleResult();
        Uzivatel lu = q.getSingleResult();
        if  (lu==null)
            return null;


        VaadinSession.getCurrent().setAttribute("id_uzivatela",lu.getId());
        VaadinSession.getCurrent().setAttribute("meno",lu.getMeno());
        System.out.println("uzivatel overeny"+VaadinSession.getCurrent().getAttribute("meno")+VaadinSession.getCurrent().getAttribute("id_uzivatela"));

        return lu;
    }
    public static Boolean prazdnyUzivatelia(){
        boolean prazdny=true;


        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        Query query = em.createNativeQuery("SELECT  count(*) FROM  uzivatelia");
        long result = (long)query.getSingleResult();
        System.out.println("pocet uzivatelov:"+result);
        if (result>0) {
            System.out.println("nie je prazdny");
            prazdny = false;
        }
        return prazdny;
    }

    public static Uzivatel getUzivatela(Long id){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Uzivatel> q = em.createNamedQuery("Uzivatel.get", Uzivatel.class);
        q.setParameter("id", id);
        Uzivatel lu = q.getSingleResult();
        if  (lu==null)
            return null;
        return lu;
    }
    public static int TypUzivatela() {
        if (VaadinSession.getCurrent().getAttribute("id_uzivatela")==null)
            return 99;
        Uzivatel u =getUzivatela((Long) VaadinSession.getCurrent().getAttribute("id_uzivatela"));
        return u.getTypKonta();
    }

}
