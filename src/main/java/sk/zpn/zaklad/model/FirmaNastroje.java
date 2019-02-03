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
}
