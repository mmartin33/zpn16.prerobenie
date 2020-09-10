package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.log4j.Logger;
import sk.zpn.authentification.HesloNastroje;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.domena.Uzivatel;
import sk.zpn.nastroje.RandomString;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class UzivatelNastroje {

    private static final Logger logger = Logger.getLogger(UzivatelNastroje.class);

    public static boolean overUzivatela(String meno, String heslo){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Uzivatel> q = em.createNamedQuery("Uzivatel.getPodlaMena", Uzivatel.class);
        q.setParameter("meno", meno);
        //Optional<Uzivatel> uzivatel = Optional.ofNullable(q.getSingleResult());
        List<Uzivatel> uzivatel = q.getResultList();
        if (uzivatel.size()==1){
            Uzivatel najdenyUzivatel=uzivatel.get(0);
            try {
                if  (HesloNastroje.check(heslo, najdenyUzivatel.getHeslo())){
                    VaadinSession.getCurrent().setAttribute("id_uzivatela", najdenyUzivatel.getId());
                    VaadinSession.getCurrent().setAttribute("meno", najdenyUzivatel.getMeno());
                    logger.info(String.format("Uzivatel %s bol overeny", najdenyUzivatel.getMeno()));
                    return true;

                }
            } catch (Exception e) {
                logger.error("Heslo nebolo mozne overit", e);
            }
        }

        return false;
    }


    public static Boolean prazdnyUzivatelia(){
        boolean prazdny=true;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        Query query = em.createNativeQuery("SELECT  count(*) FROM  uzivatelia");
        long result = (long)query.getSingleResult();
        System.out.println("pocet uzivatelov:"+result);
        if (result>0) {
            System.out.println("nie je prazdny");
            prazdny = false;
        }

        return prazdny;
    }

    public static Optional<Uzivatel> getUzivatela(Long id){
        if (id==null)
            return null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Uzivatel> q = em.createNamedQuery("Uzivatel.get", Uzivatel.class);
        q.setParameter("id", id);
        return Optional.ofNullable(q.getSingleResult());
    }
    public static Uzivatel getPrihlasenehoUzivatela(){
        Optional<Uzivatel> uzivatel = getUzivatela((Long) VaadinSession.getCurrent().getAttribute("id_uzivatela"));
        return uzivatel.get();


    }
    public static Optional<Uzivatel> getUzivatela(String login){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Uzivatel> q = em.createNamedQuery("Uzivatel.getPodlaMena", Uzivatel.class);
        q.setParameter("meno", login);
        return Optional.ofNullable(q.getSingleResult());
    }
    public static Uzivatel ulozUzivatela(Uzivatel u){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.getTransaction().begin();
        if (u.isNew())
            u.setId((long)0);
//            em.persist(u);}
//        else
            em.merge(u);
        em.getTransaction().commit();
        return u;


    }
    public static boolean prihlasenyUzivatelJePoberatel(){
        TypUzivatela typUzivatela=getPrihlasenehoUzivatela().getTypUzivatela();
        if ((typUzivatela==TypUzivatela.ADMIN)||
             (typUzivatela==TypUzivatela.SPRAVCA_ZPN)||
             (typUzivatela==TypUzivatela.PREDAJCA))
            return false;
        else
            return true;
    }

    public static void zmazUzivatela(Uzivatel u){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Vymazany uzivate:"+u.getMeno());
        em.getTransaction().begin();
        em.remove(u);
        em.getTransaction().commit();



    }
    public static Optional<TypUzivatela> TypUzivatela() {

        Optional<Uzivatel> uzivatel = getUzivatela((Long) VaadinSession.getCurrent().getAttribute("id_uzivatela"));
        return uzivatel.map(Uzivatel::getTypUzivatela);
    }
    public static List<Uzivatel> zoznamUzivatelov(){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<Uzivatel> q = em.createNamedQuery("Uzivatel.getAll", Uzivatel.class);
        return q.getResultList();
    }

    public static String getIcoVlastnejFirmyPrihlasenehoUzivala() {
        Uzivatel u =getUzivatela((Long) VaadinSession.getCurrent().getAttribute("id_uzivatela")).get();
        return u.getFirma().getIco();
    }
    public static Firma getVlastnuFirmuPrihlasenehoUzivala() {
        Uzivatel u =getUzivatela((Long) VaadinSession.getCurrent().getAttribute("id_uzivatela")).get();
        return u.getFirma();
    }
    public static boolean jeUzivatelAdminAleboSpravca() {
        return (getPrihlasenehoUzivatela().getTypUzivatela()==TypUzivatela.ADMIN ||
                getPrihlasenehoUzivatela().getTypUzivatela()==TypUzivatela.SPRAVCA_ZPN)?true:false;
    }

    public static void generujNoveMenaAHesla() {
        List<Poberatel> poberatelia=PoberatelNastroje.zoznamPoberatelov(null, false);
        RandomString gen =new RandomString(8, ThreadLocalRandom.current());

        for (Poberatel p : poberatelia) {
            String kod = gen.nextString() ;
            p.setKod(kod);
            p.setHeslo(gen.nextString());
            PoberatelNastroje.ulozPoberatela(p);
        }
    }

    public static Uzivatel getUzivatelVelkoskladu(Firma velkosklad) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Uzivatel> q = em.createNamedQuery("Uzivatel.getPodlaVelkoskladu", Uzivatel.class)
                .setParameter("id", velkosklad.getId());
        q.setMaxResults(1);

        List<Uzivatel> results = q.getResultList();
        return results.get(0);

    }
}
