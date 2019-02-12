package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.Firma;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class FirmaNastroje {
    public static List<Firma> zoznamFiriem(){
        List<Firma> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Firma> q = em.createNamedQuery("Firma.getAll", Firma.class);

        u =  q.getResultList();

        return u;
    }

    public static Firma ulozFirmu(Firma f){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        if (f.isNew())
            f.setId((long)0);
        System.out.println("Ulozena firma:"+f.getNazov());
        em.getTransaction().begin();
        em.persist(f);
        em.getTransaction().commit();
        return f;


    }
    public static void zmazFirmu(Firma f){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Vymazana firma:"+f.getNazov());
        em.getTransaction().begin();
        em.remove(f);
        em.getTransaction().commit();



    }



}
