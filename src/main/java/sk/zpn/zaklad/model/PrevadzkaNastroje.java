package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Prevadzka;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class PrevadzkaNastroje {
    public static List<Prevadzka> zoznamPrevadzka(){
        List<Prevadzka> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Prevadzka> q = em.createNamedQuery("Prevadzka.getAll", Prevadzka.class);

        u =  q.getResultList();

        return u;
    }

    public static List<Prevadzka> zoznamPrevadzka(Firma firma) {
        List<Prevadzka> u = null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<Prevadzka> q = em.createNamedQuery("Prevadzka.getPrevadzkyFirmy", Prevadzka.class);
        q.setParameter("firma", firma);
        u =  q.getResultList();

        return u;

    }

    public static Optional<Prevadzka> prvaPrevadzkaPodlaNazvu(String nazov){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Prevadzka> q = em.createNamedQuery("Prevadzka.getPodlaNazvu", Prevadzka.class)
                .setParameter("nazov", nazov);
        List<Prevadzka> prevadzka = q.getResultList();
        return prevadzka.size() > 0 ? Optional.of(q.getResultList().get(0)) : Optional.empty();
    }


    public static Prevadzka ulozPrevadzka(Prevadzka p){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        if (p.isNew()) {
            p.setId((long) 0);
            p.setKedy(new Date());
            p.setKto(UzivatelNastroje.getPrihlasenehoUzivatela().getId());
        }
        System.out.println("Ulozena prevadzka:"+p.getNazov());
        em.getTransaction().begin();
        em.merge(p);
        em.getTransaction().commit();
        return p;


    }

    public static Prevadzka zalozPrevadzku(Prevadzka p){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        if (p.isNew()) {

            p.setId(null);
            p.setKedy(new Date());
            p.setKto(UzivatelNastroje.getPrihlasenehoUzivatela().getId());
        }
        System.out.println("Ulozena prevadzka:"+p.getNazov());
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        return p;


    }

    public static void zmazPrevadzku(Prevadzka p){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Vymazana prevadzka:"+p.getNazov());
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }

    public static Prevadzka ulozPrvuPrevadzku(Firma firma,String nazovPrevadzky) {
        Prevadzka p=new Prevadzka();
        if (nazovPrevadzky==null)
            p.setNazov(firma.getNazov());
        else
            p.setNazov(nazovPrevadzky);
        p.setMesto(firma.getMesto());
        p.setUlica(firma.getUlica());
        p.setPsc(firma.getPsc());
        p.setFirma(firma);

        p.setPoberatel(PoberatelNastroje.ulozPrvehoPoberatela(p));
        zalozPrevadzku(p);


        System.out.println("Ulozena prevadzka:"+p.getNazov());
//        em.getTransaction().begin();
//        em.persist(p);
//        em.getTransaction().commit();


        return p;

    }

    public static Prevadzka najdiAleboZaloz(String ico, String nazovFirmy) {

        if (ico==null ||ico.isEmpty())
            return null;
        if (nazovFirmy==null ||nazovFirmy.isEmpty())
            nazovFirmy=ico;
        //hladame prevadzku podla ica a nazvu
        Prevadzka prevadzka =prevadzkaPodlaICOaNazvu(ico, nazovFirmy);
        if (prevadzka!=null)
            return prevadzka;
        //hladame firmu podla ica
        Firma firma =FirmaNastroje.firmaPodlaICO(ico);
        if (firma!=null) {
            //todo tu mozno zobrat prvu prevadzku aj s poberatelom
            prevadzka=zoberPrvuPrevadzku(firma);
            if (prevadzka.getPoberatel()==null)
                prevadzka.setPoberatel(PoberatelNastroje.ulozPrvehoPoberatela(prevadzka));
            if (prevadzka==null)
                prevadzka = ulozPrvuPrevadzku(firma,nazovFirmy);
            return prevadzka;
        }
        //zaklada sa firma
        firma=new Firma();
        firma.setIco(ico);
        firma.setNazov(nazovFirmy);
        Firma novaFirma=FirmaNastroje.ulozFirmu(firma);
        if (novaFirma==null)
            return null;
        //zaklada sa prevadzka
        prevadzka = ulozPrvuPrevadzku(firma,nazovFirmy);
        return prevadzka;

    }

    private static Prevadzka zoberPrvuPrevadzku(Firma firma) {
        if (firma==null)
            return null;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Prevadzka> q = em.createNamedQuery("Prevadzka.getPrevadzkaPodlaICO", Prevadzka.class)
                .setParameter("ico", firma.getIco());
        List results = q.getResultList();
        if (results.isEmpty()) return null;
        return (Prevadzka) results.get(0);
    }


    public static Prevadzka prevadzkaPodlaICOaNazvu(String ico, String nazov){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Prevadzka> q = em.createNamedQuery("Prevadzka.getPodlaICAaNazvu", Prevadzka.class)
                .setParameter("nazov", nazov.toUpperCase())
                .setParameter("ico", ico);

        List results = q.getResultList();
        if (results.isEmpty()) return null;
        return (Prevadzka) results.get(0);

    }


}
