package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.Uzivatel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;

public class UzivatelNastroje {
    public static Boolean overUzivatela(String meno, String heslo){
        boolean existuje=false;


        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        Query query = em.createNativeQuery("SELECT count(*) FROM  uzivatelia  WHERE meno = ?1 and heslo = ?2");
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(u) FROM  Uzivatel as u WHERE u.meno =:meno and u.heslo =:heslo", Long.class);

//        query.setParameter(1, meno);
//        query.setParameter(2, heslo);
        q.setParameter("meno", meno);
        q.setParameter("heslo", heslo);


        Long lu = q.getSingleResult();

        if (lu>0)
            existuje=true;
        return existuje;
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
}
