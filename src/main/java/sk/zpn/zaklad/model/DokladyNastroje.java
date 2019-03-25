package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.log4j.Logger;

import sk.zpn.domena.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DokladyNastroje {

    private static final Logger logger = Logger.getLogger(DokladyNastroje.class);


    public static Optional<Doklad> getDoklad(Long id){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Doklad> q = em.createNamedQuery("Doklad.get", Doklad.class);
        q.setParameter("id", id);
        return Optional.ofNullable(q.getSingleResult());
    }
    public static Doklad ulozDoklad(Doklad d){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        if (d.isNew()) {
            d.setId((long) 0);
            d.setKedy(new Date());
            d.setKto(UzivatelNastroje.getPrihlasenehoUzivatela());
        }
        System.out.println("Ulozeny dokald"+d.getCisloDokladu());
        em.getTransaction().begin();
        em.persist(d);
        em.getTransaction().commit();
        return d;


    }
    public static void zmazDoklad(Doklad d){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Vymazany doklad:"+d.getCisloDokladu());
        em.getTransaction().begin();
        em.remove(d);
        em.getTransaction().commit();



    }
    public static Optional<TypDokladu> TypDokladu() {
        Optional<Doklad> doklad = getDoklad((Long) VaadinSession.getCurrent().getAttribute("id_dokladu"));
        return doklad.map(Doklad::getTypDokladu);
    }


    public static List<Doklad> zoznamDokladov(){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Doklad> q = em.createNamedQuery("Doklad.getAll", Doklad.class);
        return q.getResultList();
    }
    public static String noveCisloDokladu(){
        boolean prazdny=true;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        Query query = em.createNativeQuery("SELECT  max(cisloDokladu) FROM  doklady");
        String result = (String)query.getSingleResult();

        if (result !=null)
            return Long.toString(Long.parseLong(result)+1);
        else
            return null;
    }

    public static VysledokImportu zalozDokladovuDavku(List<ZaznamCsv> zaznam, String file) {
        List <ChybaImportu> chyby = new ArrayList<>();;
        VysledokImportu vysledok=new VysledokImportu();

        Doklad hlavickaDokladu=new Doklad();

        String noveCisloDokladu = noveCisloDokladu();

        if   (noveCisloDokladu==null || noveCisloDokladu.isEmpty()){
            chyby.add(new ChybaImportu(
                    "",
                    "",
                    "",
                    "Nepodarilo sa vygenerovat cislo dokladu"));
            return vysledok;
        }

        hlavickaDokladu.setCisloDokladu(noveCisloDokladu);
        hlavickaDokladu.setTypDokladu(TypDokladu.DAVKA);
        hlavickaDokladu.setDatum(new Date());
        hlavickaDokladu.setPoznamka(file);

        hlavickaDokladu.setFirma(UzivatelNastroje.getVlastnuFirmuPrihlasenehoUzivala());



        List<PolozkaDokladu> polozkyDokladu = new ArrayList<>();;
        for (ZaznamCsv z: zaznam){
            PolozkaDokladu pd=PolozkaDokladuNastroje.vytvorPolozkuZoZaznamuCSV(z,hlavickaDokladu);
            if (pd!=null)
                polozkyDokladu.add(pd);
            else
                chyby.add(new ChybaImportu(
                    z.getNazvFirmy(),
                    z.getIco(),
                    z.getKit(),
                    "Nepodarilo sa zalozit polozku dokladu"));

        }
        DokladyNastroje.ulozDokladDavky(hlavickaDokladu,polozkyDokladu);

        vysledok.setDoklad(hlavickaDokladu);
        vysledok.setPolozky(polozkyDokladu);
        vysledok.setChyby(chyby);
        return vysledok;






    }

    private static void ulozDokladDavky(Doklad hlavickaDokladu, List<PolozkaDokladu> polozkyDokladu) {
        if (polozkyDokladu.size()==0)
            return;

        ulozDoklad(hlavickaDokladu);
        for (PolozkaDokladu polozka: polozkyDokladu){
            PolozkaDokladuNastroje.ulozPolozkuDokladu(polozka);
        }
    }
}
