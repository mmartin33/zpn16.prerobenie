package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.Produkt;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ProduktyNastroje {
    public static List<Produkt> zoznamProduktov(){
        List<Produkt> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<Produkt> q = em.createNamedQuery("Produkt.getAll", Produkt.class);
        return q.getResultList();
    }

    public static List<Produkt> zoznamProduktovZaRok(){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Produkt> q = em.createNamedQuery("Produkt.getZaRok", Produkt.class);
        q.setParameter("rok", ParametreNastroje.nacitajParametre().getRok());
        return q.getResultList();
    }

    public static Optional<Produkt> prvyProduktPodlaNazvu(String nazov){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Produkt> q = em.createNamedQuery("Produkt.getPodlaNazvu", Produkt.class)
                .setParameter("nazov", nazov);
        List<Produkt> produkty = q.getResultList();
        return produkty.size() > 0 ? Optional.of(q.getResultList().get(0)) : Optional.empty();
    }
    public static boolean uzExistujeKat(String kat){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Produkt> q = em.createNamedQuery("Produkt.getPodlaKodu", Produkt.class)
                .setParameter("kat", kat);
        List<Produkt> produkty = q.getResultList();
        return (produkty.size() > 0) ;
    }



    public static Produkt ulozProdukt(Produkt f){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        if (f.isNew()) {
            f.setId((long) 0);
            f.setKedy(new Date());
            f.setKto(UzivatelNastroje.getPrihlasenehoUzivatela());
        }
        em.getTransaction().begin();
        em.persist(f);
        em.getTransaction().commit();
        return f;
    }

    public static void zmazProdukt(Produkt f){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.getTransaction().begin();
        em.remove(f);
        em.getTransaction().commit();
    }



}
