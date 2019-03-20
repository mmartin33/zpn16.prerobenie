package sk.zpn.zaklad.model.util;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.Produkt;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class TestDataFactory {

    EntityManager em;

    public TestDataFactory() {
        em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
    }

    public void createProdukty() {
        List<Produkt> products = new ArrayList<>();
        products.add((Produkt) new Produkt()
            .setBody(1D)
            .setKat("01kJG")
            .setKusy(1D)
            .setNazov("baban")
            .setRok("2019")
            .setId(0L));
        products.add((Produkt) new Produkt()
            .setBody(1.3D)
            .setKat("02YSH")
            .setKusy(5D)
            .setNazov("zemiak")
            .setRok("2019")
            .setId(0L));
        products.add((Produkt) new Produkt()
            .setBody(4D)
            .setKat("03UFE")
            .setKusy(1D)
            .setNazov("ananas")
            .setRok("2019")
            .setId(0L));
        products.add((Produkt) new Produkt()
            .setBody(2D)
            .setKat("04UHF")
            .setKusy(3D)
            .setNazov("pomelo")
            .setRok("2019")
            .setId(0L));

        em.getTransaction().begin();
        products.forEach(em::persist);
        em.getTransaction().commit();
    }
}
