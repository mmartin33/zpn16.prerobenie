package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.Uzivatel;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Pripojenie {
    private static EntityManager em;
    public Pripojenie(){

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("bodovySystem");
    em = emf.createEntityManager();
    VaadinSession.getCurrent().setAttribute("createEntityManager",em);
//    createDog(1, "Spot", "Welsh Corgi");
//    createDog(2, "Fluffy", "Poodle");
//    createDog(3, "Clifford", "Golden Retriever");
//
    }

    private static void VytvorUzivatela(int id, String meno, String heslo) {
        em.getTransaction().begin();
        Uzivatel emp = new Uzivatel(id, "aaa", "bb");
        em.persist(emp);
        em.getTransaction().commit();
    }
}
