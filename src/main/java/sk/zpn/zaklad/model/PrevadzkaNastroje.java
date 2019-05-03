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

    public static void zmazPrevadzku(Prevadzka p){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Vymazana prevadzka:"+p.getNazov());
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }

    public static Prevadzka ulozPrvuPrevadzku(Firma firma) {
        Prevadzka p=new Prevadzka();
        p.setNazov(firma.getNazov());
        p.setMesto(firma.getMesto());
        p.setUlica(firma.getUlica());
        p.setPsc(firma.getPsc());
        p.setFirma(firma);
        p.setPoberatel(PoberatelNastroje.ulozPrvehoPoberatela(p));
        ulozPrevadzka(p);


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
            return null;
        //hladame prevadzku podla ica a nazvu
        Prevadzka prevadzka =prevadzkaPodlaICOaNazvu(ico, nazovFirmy);
        if (prevadzka!=null)
            return prevadzka;
        //hladame firmu podla ica
        Firma firma =FirmaNastroje.firmaPodlaICO(ico);
        if (firma!=null) {
            prevadzka = ulozPrvuPrevadzku(firma);
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
        prevadzka = ulozPrvuPrevadzku(firma);
        return prevadzka;

    }


    public static Prevadzka prevadzkaPodlaICOaNazvu(String ico, String nazov){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Prevadzka> q = em.createNamedQuery("Prevadzka.getPodlaICAaNazvu", Prevadzka.class)
                .setParameter("nazov", nazov)
                .setParameter("ico", ico);

        List results = q.getResultList();
        if (results.isEmpty()) return null;
        return (Prevadzka) results.get(0);

    }


}
