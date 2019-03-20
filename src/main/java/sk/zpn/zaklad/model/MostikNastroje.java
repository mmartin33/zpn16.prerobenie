package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.FirmaProdukt;
import sk.zpn.domena.Uzivatel;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import static sk.zpn.zaklad.model.UzivatelNastroje.getUzivatela;

public class MostikNastroje {
    public static List<FirmaProdukt> mostikZaRokAFirmu(){
        List<FirmaProdukt> u = null;
        Optional<Uzivatel> uzivatel = getUzivatela((Long) VaadinSession.getCurrent().getAttribute("id_uzivatela"));
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<FirmaProdukt> q = em.createNamedQuery("FirmaProdukt.getZaRokFirmu", FirmaProdukt.class);
        q.setParameter("rok", ParametreNastroje.nacitajParametre().getRok());
        q.setParameter("idFirmy", uzivatel.map(Uzivatel::getFirmaID))  ;
        u= q.getResultList();

        return u;


    }



}
