package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.commons.lang.StringUtils;
import sk.zpn.domena.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ProduktyNastroje {
    public static List<Produkt> zoznamProduktov() {
        List<Produkt> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<Produkt> q = em.createNamedQuery("Produkt.getAll", Produkt.class);
        return q.getResultList();
    }

    public static List<Produkt> zoznamProduktovZaRok(String rok, TypProduktov typ) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Produkt> q;
        if (typ == TypProduktov.BODOVACI) {
            q = em.createNamedQuery("Produkt.getZaRok", Produkt.class);
            if (StringUtils.isEmpty(rok))
                q.setParameter("rok", ParametreNastroje.nacitajParametre().getRok());
            else
                q.setParameter("rok", rok);
        } else
            q = em.createNamedQuery("Odmena.getZoznam", Produkt.class);
        //q.setParameter("typ",typ);
        return q.getResultList();
    }

    public static List<Produkt> zoznamProduktovZaRokZaDodavatela(String rok, Firma f) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Produkt> q = em.createNamedQuery("Produkt.getZaRokZaDodavatela", Produkt.class);

        q.setParameter("rok", rok);
        q.setParameter("id", f.getId());
        return q.getResultList();
    }

    public static Optional<Produkt> prvyProduktPodlaNazvu(String nazov, TypProduktov typ) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Produkt> q;
        if (typ == TypProduktov.BODOVACI)
            q = em.createNamedQuery("Produkt.getPodlaNazvu", Produkt.class)
                    .setParameter("nazov", nazov)
                    .setParameter("rok", ParametreNastroje.nacitajParametre().getRok());
        else
            q = em.createNamedQuery("Odmena.getPodlaNazvu", Produkt.class)
                    .setParameter("nazov", nazov);

        List<Produkt> produkty = q.getResultList();
        return produkty.size() > 0 ? Optional.of(q.getResultList().get(0)) : Optional.empty();
    }

    public static Optional<Produkt> prvyProduktPodlaKat(String kat, TypProduktov typ) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Produkt> q;
        if (typ == TypProduktov.BODOVACI)
            q = em.createNamedQuery("Produkt.getPodlaKodu", Produkt.class)
                    .setParameter("kat", kat)
                    .setParameter("rok", ParametreNastroje.nacitajParametre().getRok());
        else
            q = em.createNamedQuery("Odmena.getPodlaKodu", Produkt.class)
                    .setParameter("kat", kat);
        List<Produkt> produkty = q.getResultList();
        return produkty.size() > 0 ? Optional.of(q.getResultList().get(0)) : Optional.empty();
    }

    public static boolean uzExistujeKat(String kat) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Produkt> q = em.createNamedQuery("Produkt.getPodlaKodu", Produkt.class)
                .setParameter("kat", kat);
        List<Produkt> produkty = q.getResultList();
        return (produkty.size() > 0);
    }


    public static Produkt ulozProdukt(Produkt f) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        em.getTransaction().begin();
        if (f.isNew()) {
            f.setId((long) 0);
            f.setKedy(new Date());
            f.setKto(UzivatelNastroje.getPrihlasenehoUzivatela().getId());
            em.persist(f);
        }
        else
            em.merge(f);

        em.getTransaction().commit();
        return f;
    }

    public static void zmazProdukt(Produkt f) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.getTransaction().begin();
        em.remove(f);
        em.getTransaction().commit();
    }


    public static String getMaxKIT(TypProduktov typProduktov) {

        boolean prazdny = true;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        String sql;
        String obdobie = null;
        Query query;
        if (typProduktov==TypProduktov.BODOVACI) {
            sql = "SELECT  max(kat) FROM  produkty where typproduktu=? and rok=?";
            query = em.createNativeQuery(sql)
                    .setParameter(1, "BODOVACI")
                    .setParameter(2, ParametreNastroje.nacitajParametre().getRok());
        }
           else{
                sql = "SELECT  max(kat) FROM  produkty where typproduktu=? ";
                query = em.createNativeQuery(sql)
                    .setParameter(1, "ODMENA");}

        String result = (String) query.getSingleResult();

        if (result != null)
            if (typProduktov==TypProduktov.BODOVACI)
                return Long.toString(Long.parseLong(result) + 1);
            else
                return Long.toString(Long.parseLong(result) + 10);
        else
        if (typProduktov==TypProduktov.BODOVACI)
            return "00001";
        else
            return "000000010";
    }
    public static boolean existujePohybNaOdmene(Produkt produkt) {
        List<PolozkaDokladu> pocet = null;
        if (produkt.getTypProduktov()!=TypProduktov.ODMENA)
            return false;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        String sql = "SELECT pd FROM polozkyDokladu pd " +
                " join pd.produkt as p"+
                " where  p.id=:id_produktu ";

        TypedQuery<PolozkaDokladu> q = em.createQuery(sql, PolozkaDokladu.class).setMaxResults(1);
        q.setParameter("id_produktu", produkt.getId());
        pocet = q.getResultList();





        if (pocet.size()==0)
            return false;
        return true;
    }

    public static boolean kontrolujZmenuBodov(Produkt produktEditovany, String value) {
        if (produktEditovany.getId()==null)
            return true;
        if (produktEditovany.getBody().compareTo(new BigDecimal(value))==0)
            return true;
        if (!existujePohybNaOdmene(produktEditovany))
            return true;
        return false;
    }

    public static boolean kontrolujZmenuKusov(Produkt produktEditovany, String value) {
        if (produktEditovany.getTypProduktov()==TypProduktov.BODOVACI)
            return true;
        if (produktEditovany.getBody().compareTo(new BigDecimal(1))==0  )
            return true;

        return false;
    }
}
