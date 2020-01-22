package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.Prevadzka;
import sk.zpn.nastroje.NastrojePoli;
import sk.zpn.nastroje.RandomString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class PoberatelNastroje {

    private static final Logger logger = Logger.getLogger(PoberatelNastroje.class);

    public static List<Poberatel> zoznamPoberatelov(Prevadzka prevadzka) {
        List<Poberatel> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        //TypedQuery<Poberatel> q = em.createNamedQuery("PolozkaDokladu.getPoberateliaVelkoskladu", Poberatel.class);
        String sql = "SELECT pob FROM polozkyDokladu p " +
                " join p.doklad as d " +
                " join p.poberatel as pob " +
                (prevadzka != null ? " join p.prevadzka as prev " : "") +
                " where  d.stavDokladu=sk.zpn.domena.StavDokladu.POTVRDENY " +
                (prevadzka != null ? " and prev.id=:id_prevadzky " : " ") +
                "group by pob ";
        TypedQuery<Poberatel> q = em.createQuery(sql, Poberatel.class);

        if (prevadzka != null)
            q.setParameter("id_prevadzky", prevadzka.getId());
        u = q.getResultList();

        return u;
    }

    public static List<Poberatel> zoznamPoberatelovPodlaMena(String meno) {
        List<Poberatel> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<Poberatel> q = em.createNamedQuery("Poberatel.getPodlaMenaLike", Poberatel.class).
                setParameter("meno", "%" + meno + "%");

        u = q.getResultList();

        return u;
    }

    public static Poberatel ulozPrvehoPoberatela(Prevadzka prevadzka) {
        Poberatel poberatel = new Poberatel();
        poberatel.setMeno("NR " + prevadzka.getNazov());

        ulozPoberatela(poberatel);
//        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
//        if (poberatel.isNew())
//            poberatel.setId((long)0);
//
//        em.getTransaction().begin();
//        em.persist(poberatel);
//        em.getTransaction().commit();
        return poberatel;

    }


    public static Poberatel ulozPoberatela(Poberatel f) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.getTransaction().begin();
        RandomString gen = new RandomString(8, ThreadLocalRandom.current());
        if (f.isNew()) {
            f.setId((long) 0);
            f.setKedy(new Date());
            if (StringUtils.isEmpty(f.getKod()))
                f.setKod(gen.nextString());
            if (StringUtils.isEmpty(f.getHeslo()))
                f.setHeslo(gen.nextString());
            f.setKto(UzivatelNastroje.getPrihlasenehoUzivatela().getId());
            em.persist(f);
        } else
            em.merge(f);
        em.getTransaction().commit();

        return f;


    }

    public static Optional<Poberatel> prvyPoberatelPodlaMena(String meno) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Poberatel> q = em.createNamedQuery("Poberatel.getPodlaMena", Poberatel.class)
                .setParameter("meno", meno);
        List<Poberatel> poberatel = q.getResultList();
        return poberatel.size() > 0 ? Optional.of(q.getResultList().get(0)) : Optional.empty();
    }

    public static Poberatel overPoberatela(String kod, String pass) {
        logger.info("Poberatel query by kod" + kod);

        TypedQuery<Poberatel> q;
        if (StringUtils.contains(kod, '@')) {
            EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
            q = em.createNamedQuery("Poberatel.getPodlaEmailuAhesla", Poberatel.class);
        } else {
            EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
            q = em.createNamedQuery("Poberatel.getPodlaKoduAhesla", Poberatel.class);
        }
        q.setParameter("kod", StringUtils.trim(kod));
        q.setParameter("heslo", StringUtils.trim(pass));
        List<Poberatel> poberatel = q.getResultList();
        if (poberatel.size() == 1) {
            VaadinSession.getCurrent().setAttribute("id_uzivatela", poberatel.get(0).getId());
            VaadinSession.getCurrent().setAttribute("meno", poberatel.get(0).getMeno());
            logger.info(String.format("Poberate %s bol overeny", poberatel.get(0).getKod()));
        }
        return poberatel.size() > 0 ? poberatel.get(0) : null;
    }

    public static Optional<Poberatel> poberatelPodlaId(Long id) {
        logger.info("Poberatel query by id" + id.toString());
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Poberatel> q = em.createNamedQuery("Poberatel.get", Poberatel.class);
        q.setParameter("id", id);
        List<Poberatel> poberatel = q.getResultList();
        System.out.println("Vybraty poberate" + (poberatel.size() > 0 ? Optional.of(q.getResultList().get(0).getPoberatelMenoAdresa()) : Optional.empty()));
        return poberatel.size() > 0 ? Optional.of(q.getResultList().get(0)) : Optional.empty();
    }

    public static void zmazPoberatela(Poberatel f) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Vymazany poberatel:" + f.getMeno());
        em.getTransaction().begin();

        if (!em.contains(f)) {
            f = em.merge(f);
        }


        em.remove(f);
        em.getTransaction().commit();


    }


    public static Map<String, Double> vratPoberatelovVelkoskladu(Firma velkosklad) {
        EntityManager em1;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em1 = emf.createEntityManager();
        String sql = "select CAST(p.poberatel_id as text)as kluc ,0 as hodnota from polozkydokladu as p  " +
                "join doklady as d on d.id=p.doklad_id  " +
                "join firmy as f on f.id=d.firma_id " +
                "where  f.id=? " +
                " and d.typdokladu='DAVKA' " +
                " and d.stavdokladu='POTVRDENY' " +
                "group by p.poberatel_id ";


        Query query = em1.createNativeQuery(sql);

        query.setParameter(1, velkosklad.getId());

        List result1 = query.getResultList();
        Map<String, Double> vysledok = NastrojePoli.<String, Double>prerobListNaMapu2(result1);
        emf.close();

        return vysledok;


    }

    public static Firma getPrvyVelkosklad(Poberatel poberatel) {
        EntityManager em1;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em1 = emf.createEntityManager();
        String sql = "select d.firma_id from polozkydokladu as pol " +
                "join doklady as d on d.id=pol.doklad_id " +
                "where pol.poberatel_id=? " +
                "group by d.firma_id,d.datum " +
                "order by d.datum " +
                "limit 1";


        Query query = em1.createNativeQuery(sql);

        query.setParameter(1, poberatel.getId());

        Long result1 = (Long) query.getSingleResult();

        emf.close();
        if (result1 == null)
            return null;
        else
            return FirmaNastroje.firmaPodlaID(result1).get();


    }

    public static List<Poberatel> zoznamPoberatelovVelkoskladu(Firma velkosklad, Prevadzka prevadzka) {
        List<Poberatel> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        //TypedQuery<Poberatel> q = em.createNamedQuery("PolozkaDokladu.getPoberateliaVelkoskladu", Poberatel.class);
        String sql = "SELECT pob FROM polozkyDokladu p " +
                " join p.doklad as d " +
                " join p.poberatel as pob " +
                (prevadzka != null ? " join p.prevadzka as prev " : "") +
                " join d.firma as f " +
                " where  f.id=:id " +
                " and d.stavDokladu=sk.zpn.domena.StavDokladu.POTVRDENY " +
                (prevadzka != null ? " and prev.id=:id_prevadzky " : " ") +
                "group by pob ";
        TypedQuery<Poberatel> q = em.createQuery(sql, Poberatel.class);
        q.setParameter("id", velkosklad.getId());
        if (prevadzka != null)
            q.setParameter("id_prevadzky", prevadzka.getId());
        u = q.getResultList();


        return u;
    }

}
