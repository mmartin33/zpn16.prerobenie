package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.Produkt;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class ProduktyNastroje {
    public static List<Produkt> zoznamProduktov(){
        List<Produkt> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Produkt> q = em.createNamedQuery("Produkt.getAll", Produkt.class);

        u =  q.getResultList();

        return u;
    }
    public static List<Produkt> zoznamProduktovZaRok(){
        List<Produkt> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Produkt> q = em.createNamedQuery("Produkt.getZaRok", Produkt.class);
        q.setParameter("rok", ParametreNastroje.nacitajParametre().getRok());
        u =  q.getResultList();

        return u;
    }

    public static Produkt ulozProdukt(Produkt f){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        if (f.isNew())
            f.setId((long)0);
        em.getTransaction().begin();
        em.persist(f);
        em.getTransaction().commit();
        return f;


    }
    public static void zmazProdukt(Produkt f){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Vymazany produkt:"+f.getProdukt());
        em.getTransaction().begin();
        em.remove(f);
        em.getTransaction().commit();



    }



}
