package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Parametre;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.model.util.TestDataFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Pripojenie {
    private static EntityManager em;
    public Pripojenie(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em = emf.createEntityManager();
        VaadinSession.getCurrent().setAttribute("createEntityManager",em);
        if (UzivatelNastroje.prazdnyUzivatelia()) {
            Pripojenie.vytvorUzivatela(1, "m", "m", TypUzivatela.ADMIN);
            TestDataFactory testDataFactory = new TestDataFactory();
            testDataFactory.createProdukty();
            ParametreNastroje.ulozParametre(new Parametre().setRok("2019"));
        }
    }

    public static void vytvorUzivatela(int id, String meno, String heslo, TypUzivatela typUzivatela) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.getTransaction().begin();
        Uzivatel emp = new Uzivatel(meno, heslo, typUzivatela);
        emp.setFirma(vytvorDummyFirmu());
        em.persist(emp);
        em.getTransaction().commit();
    }

    private static Firma vytvorDummyFirmu() {
        Firma firma = new Firma();
        firma.setNazov("DummyNazov");
        firma.setDic("DummyDIC");
        firma.setIco("DummyICO");
        firma.setIc_dph("DummyIC_DPH");
        firma.setUlica("DummyUlica");
        firma.setMesto("DummyUlica");
        firma.setPsc("DummyPsc");
        firma.setTelefon("DummyTelefon");
        return firma;
    }
}
