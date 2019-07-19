package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.log4j.Logger;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.Prevadzka;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class PoberatelNastroje {

    private static final Logger logger = Logger.getLogger(PoberatelNastroje.class);

    public static List<Poberatel> zoznamPoberatelov(){
        List<Poberatel> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<Poberatel> q = em.createNamedQuery("Poberatel.getAll", Poberatel.class);

        u =  q.getResultList();

        return u;
    }

    public static Poberatel ulozPrvehoPoberatela(Prevadzka prevadzka) {
        Poberatel poberatel=new Poberatel();
        poberatel.setMeno(prevadzka.getNazov());

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


    public static Poberatel ulozPoberatela(Poberatel f){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
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

    public static Optional<Poberatel> prvyPoberatelPodlaMena(String meno){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Poberatel> q = em.createNamedQuery("Poberatel.getPodlaMena", Poberatel.class)
                .setParameter("meno", meno);
        List<Poberatel> poberatel = q.getResultList();
        return poberatel.size() > 0 ? Optional.of(q.getResultList().get(0)) : Optional.empty();
    }

    public static Poberatel overPoberatela(String kod){
        logger.info("Poberatel query by kod" + kod);
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Poberatel> q = em.createNamedQuery("Poberatel.getPodlaKodu", Poberatel.class);
        q.setParameter("kod", kod);
        List<Poberatel> poberatel = q.getResultList();
        if (poberatel.size()==1){
            VaadinSession.getCurrent().setAttribute("id_uzivatela", poberatel.get(0).getId());
            VaadinSession.getCurrent().setAttribute("meno", poberatel.get(0).getMeno());
            logger.info(String.format("Poberate %s bol overeny", poberatel.get(0).getKod()));
        }
        return poberatel.size() > 0 ? poberatel.get(0) : null;
}

    public static Optional<Poberatel> poberatelPodlaId(Long id){
        logger.info("Poberatel query by id" + id.toString());
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Poberatel> q = em.createNamedQuery("Poberatel.get", Poberatel.class);
        q.setParameter("id", id);
        List<Poberatel> poberatel = q.getResultList();
        System.out.println("Vybraty poberate"+ (poberatel.size() > 0 ? Optional.of(q.getResultList().get(0).getPoberatelMenoAdresa()) : Optional.empty()));
        return poberatel.size() > 0 ? Optional.of(q.getResultList().get(0)) : Optional.empty();
    }

    public static void zmazPoberatela(Poberatel f){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Vymazany poberatel:"+f.getMeno());
        em.getTransaction().begin();

        if (!em.contains(f)) {
            f = em.merge(f);
        }



        em.remove(f);
        em.getTransaction().commit();



    }



}
