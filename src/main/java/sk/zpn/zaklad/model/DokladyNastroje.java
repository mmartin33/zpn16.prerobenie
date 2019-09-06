package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import sk.zpn.domena.*;
import sk.zpn.domena.importy.*;
import sk.zpn.zaklad.grafickeNastroje.ProgressBarZPN;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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
            if (d.getStavDokladu()==null)
                d.setStavDokladu(StavDokladu.POTVRDENY);
            d.setId(null);
            d.setKedy(new Date());
            d.setKto(UzivatelNastroje.getPrihlasenehoUzivatela().getId());
            em.getTransaction().begin();
            em.persist(d);
        }
        else{
            em.getTransaction().begin();
            em.merge(d);
        }
        em.getTransaction().commit();

        System.out.println("Ulozeny dokald"+d.getCisloDokladu());
        return d;


    }
    public static void zmazDoklad(Doklad d){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        System.out.println("Vymazany doklad:"+d.getCisloDokladu());
        em.getTransaction().begin();
        em.merge(d);
        em.remove(d);
        em.getTransaction().commit();



    }
    public static Optional<TypDokladu> TypDokladu() {
        Optional<Doklad> doklad = getDoklad((Long) VaadinSession.getCurrent().getAttribute("id_dokladu"));
        return doklad.map(Doklad::getTypDokladu);
    }


    public static List<Doklad> zoznamDokladov(Firma velkosklad){
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<Doklad> q;
        if (velkosklad!=null) {
            q = em.createNamedQuery("Doklad.getZaFirmu", Doklad.class);
            q.setParameter("id", velkosklad.getId());
            }
        else
            q = em.createNamedQuery("Doklad.getAll", Doklad.class);
        return q.getResultList();
    }
    public static String noveCisloDokladu(Date datum){
        boolean prazdny=true;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        String sql ;
        String obdobie = null;
        if (datum==null)
            sql="SELECT  max(cisloDokladu) FROM  doklady";
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(datum);
            Integer rok =calendar.get(Calendar.YEAR);
            Integer mesiac =calendar.get(Calendar.MONTH)+1;
            obdobie=(rok.toString()+ StringUtils.leftPad(mesiac.toString(),2,'0'));
            sql = "SELECT  max(cisloDokladu) FROM  doklady where substr(cislodokladu,1,6)='"+obdobie+"'";

        }

        Query query = em.createNativeQuery(sql);

        String result = (String)query.getSingleResult();

        if (result !=null)
            return Long.toString(Long.parseLong(result)+1);
        else
            return obdobie+"001";
    }

    public static VysledokImportu zalozDokladovuDavku(Map<String, ZaznamCsv> zaznam, String file, ParametreImportu parametreImportu, ProgressBarZPN progressBarZPN) {




        List <ChybaImportu> chyby = new ArrayList<>();;
        VysledokImportu vysledok=new VysledokImportu();
        progressBarZPN.nadstavNadpis("Zhranie dokladu");
        progressBarZPN.nadstavspustenie(true);
        progressBarZPN.zobraz();
        Doklad hlavickaDokladu=new Doklad();

        String noveCisloDokladu = noveCisloDokladu(parametreImportu.getDatum());


        if   (noveCisloDokladu==null || noveCisloDokladu.isEmpty()){
            chyby.add(new ChybaImportu(
                    "",
                    "",
                    "",
                    "Nepodarilo sa vygenerovat cislo dokladu",
                    ""));
            return vysledok;
        }

        hlavickaDokladu.setCisloDokladu(noveCisloDokladu);
        hlavickaDokladu.setTypDokladu(TypDokladu.DAVKA);
        hlavickaDokladu.setDatum(parametreImportu.getDatum());
        hlavickaDokladu.setPoznamka(file);
        hlavickaDokladu.setStavDokladu(StavDokladu.NEPOTVRDENY);


        //hlavickaDokladu.setFirma(UzivatelNastroje.getVlastnuFirmuPrihlasenehoUzivala());
        hlavickaDokladu.setFirma(parametreImportu.getFirma());



        List<PolozkaDokladu> polozkyDokladu = new ArrayList<>();;









        int i=0;
        for (Map.Entry<String, ZaznamCsv> entry : zaznam.entrySet()) {
            ZaznamCsv z= entry.getValue();
            i++;
           // progressBarZPN.posun(new BigDecimal(zaznam.size()),new BigDecimal(i));
            progressBarZPN.setProgresBarValue(new BigDecimal(i).divide(new BigDecimal(zaznam.size()),2,BigDecimal.ROUND_HALF_UP).floatValue());
            if (new BigDecimal(i).remainder(new BigDecimal(100)).compareTo(BigDecimal.ZERO)==0)
                System.out.println("Vytvaranie zaznamov"+i);
            NavratovaHodnota navratovahodnota=PolozkaDokladuNastroje.vytvorPolozkuZoZaznamuCSV(z,hlavickaDokladu);

            //PolozkaDokladu pd=PolozkaDokladuNastroje.vytvorPolozkuZoZaznamuCSV(z,hlavickaDokladu);
            if (navratovahodnota.getPolozkaDokladu()!=null)
                polozkyDokladu.add(navratovahodnota.getPolozkaDokladu());
            else
                if (navratovahodnota.getChyba()==NavratovaHodnota.NENAJEDENA_FIRMA)

                chyby.add(new ChybaImportu(
                    z.getNazvFirmy(),
                    z.getIco(),
                    z.getKit(),
                    "Nepodarilo sa zalozit polozku dokladu (firma)",
                    z.getMtzDoklad()));
                else if  (navratovahodnota.getChyba()==NavratovaHodnota.PRAZDNE_ICO)
                    chyby.add(new ChybaImportu(
                        z.getNazvFirmy(),
                        z.getIco(),
                        z.getKit(),
                        "Nepodarilo sa zalozit polozku dokladu (firma-prazdne ICO)",
                            z.getMtzDoklad()));

        }
        System.out.println("Koniec vytvarania zaznamov"+i);
        progressBarZPN.koniec();
        DokladyNastroje.ulozDokladDavky(hlavickaDokladu,polozkyDokladu,progressBarZPN);

        vysledok.setDoklad(hlavickaDokladu);
        vysledok.setPolozky(polozkyDokladu);
        vysledok.setChyby(chyby);
        return vysledok;






    }


    private static void ulozDokladDavky(Doklad hlavickaDokladu, List<PolozkaDokladu> polozkyDokladu, ProgressBarZPN progressBarZPN) {
        if (polozkyDokladu.size()==0)
            return;
        Doklad ulozenyDoklad;
        progressBarZPN.nadstavNadpis("ZPN - ukladanie položiek dokladu");
        progressBarZPN.nadstavspustenie(true);
        progressBarZPN.zobraz();
        ulozenyDoklad=vytvorDoklad(hlavickaDokladu);
        int i=0;
        for (PolozkaDokladu polozka: polozkyDokladu){
            i++;
            if (new BigDecimal(i).remainder(new BigDecimal(100)).compareTo(BigDecimal.ZERO)==0)
                System.out.println("ZPN- Vytvaranie poloziek dokladov"+i);

            progressBarZPN.posun(new BigDecimal(polozkyDokladu.size()),new BigDecimal(i));
            polozka.setDoklad(ulozenyDoklad);
            PolozkaDokladuNastroje.vytvorPolozkuDokladu(polozka);
        }

        System.out.println("ZPN - Koniec vytvaranie dokladov"+i);

        progressBarZPN.koniec();
    }

    public static Doklad vytvorDoklad(Doklad d) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        if (d.isNew()) {
            if (d.getStavDokladu()==null)
                d.setStavDokladu(StavDokladu.POTVRDENY);
            d.setId(null);
            d.setKedy(new Date());
            d.setKto(UzivatelNastroje.getPrihlasenehoUzivatela().getId());
        }
        System.out.println("Ulozeny dokald"+d.getCisloDokladu());
        em.getTransaction().begin();
        em.persist(d);
        em.getTransaction().commit();
        return d;
    }
}
