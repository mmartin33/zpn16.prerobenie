package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.log4j.Logger;
import sk.zpn.domena.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class PolozkaDokladuNastroje {

    private static final Logger logger = Logger.getLogger(PolozkaDokladuNastroje.class);


    public static Optional<PolozkaDokladu> getPolozkaDokladu(Long id) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<PolozkaDokladu> q = em.createNamedQuery("PolozkaDokladu.get", PolozkaDokladu.class);
        q.setParameter("id", id);
        return Optional.ofNullable(q.getSingleResult());
    }

    public static PolozkaDokladu ulozPolozkuDokladu(PolozkaDokladu d) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        if (d.isNew())
            d.setId((long) 0);
        System.out.println("Ulozena polozka");
        em.getTransaction().begin();
        em.persist(d);
        em.getTransaction().commit();
        return d;


    }

    public static void zmazPolozkyDoklady(PolozkaDokladu d) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Vymazaná položka");
        em.getTransaction().begin();
        em.remove(d);
        em.getTransaction().commit();


    }

    public static List<PolozkaDokladu> zoznamPoloziekDokladov(Doklad d) {

        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<PolozkaDokladu> q = em.createNamedQuery("PolozkaDokladu.getPolozkyJednehoDokladu", PolozkaDokladu.class);
        q.setParameter("doklad", d);
        return q.getResultList();
    }

    public static PolozkaDokladu vytvorPolozkuZoZaznamuCSV(ZaznamCsv zaznam, Doklad doklad) {
        PolozkaDokladu pd = new PolozkaDokladu();
        FirmaProdukt fp = new FirmaProdukt();

        fp=FirmaProduktNastroje.getFirmaProduktPreImport(doklad.getFirma(),
                                                                      ParametreNastroje.nacitajParametre().getRok(),
                                                                               zaznam.getKit());

        if (fp==null)
            return null;
        pd.setDoklad(doklad);

        if (fp.getKoeficient()!=null && fp.getKoeficient().compareTo(BigDecimal.valueOf(0))!=0 )
                pd.setMnozstvo(zaznam.getMnozstvo().multiply(fp.getKoeficient()));
        else
            pd.setMnozstvo(zaznam.getMnozstvo());
        pd.setBody(pd.getBody().multiply(pd.getMnozstvo()));

        Prevadzka prevadzka=PrevadzkaNastroje.najdiAleboZaloz(zaznam.getIco(), zaznam.getNazvFirmy());
        if (prevadzka==null)
            return null;
        pd.setPrevadzka(prevadzka);
        pd.setPoberatel(prevadzka.getPoberatel());

        pd.setPoznamka(zaznam.getMtzDoklad());
        pd.setProdukt(fp.getProdukt());


        return pd;
    }

}
