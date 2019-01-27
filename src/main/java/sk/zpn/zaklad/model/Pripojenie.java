package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.Uzivatel;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Pripojenie {
    private static EntityManager em;
    public Pripojenie(){

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
    em = emf.createEntityManager();
    VaadinSession.getCurrent().setAttribute("createEntityManager",em);
    if (UzivatelNastroje.prazdnyUzivatelia())
         Pripojenie.vytvorUzivatela(1,"m","m");
    }

    public static void vytvorUzivatela(int id, String meno, String heslo) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.getTransaction().begin();
        Uzivatel emp = new Uzivatel(meno, heslo);
        em.persist(emp);
        em.getTransaction().commit();
    }
}
