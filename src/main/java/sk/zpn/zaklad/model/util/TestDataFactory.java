package sk.zpn.zaklad.model.util;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.Produkt;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
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
            .setBody(BigDecimal.valueOf(1))
            .setKat("01kJG")
            .setKusy(BigDecimal.valueOf(1))
            .setNazov("baban")
            .setRok("2019")
            .setId(0L));
        products.add((Produkt) new Produkt()
            .setBody(BigDecimal.valueOf(1.3))
            .setKat("02YSH")
            .setKusy(BigDecimal.valueOf(5))
            .setNazov("zemiak")
            .setRok("2019")
            .setId(0L));
        products.add((Produkt) new Produkt()
            .setBody(BigDecimal.valueOf(4))
            .setKat("03UFE")
            .setKusy(BigDecimal.valueOf(1))
            .setNazov("ananas")
            .setRok("2019")
            .setId(0L));
        products.add((Produkt) new Produkt()
            .setBody(BigDecimal.valueOf(2))
            .setKat("04UHF")
            .setKusy(BigDecimal.valueOf(3))
            .setNazov("pomelo")
            .setRok("2019")
            .setId(0L));

        em.getTransaction().begin();
        products.forEach(em::persist);
        em.getTransaction().commit();
    }
}
