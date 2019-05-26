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
        }
        System.out.println("Ulozeny dokald"+d.getCisloDokladu());
        em.getTransaction().begin();
        em.merge(d);
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
        em.clear();
        TypedQuery<Doklad> q = em.createNamedQuery("Doklad.getAll", Doklad.class);
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
            Integer mesiac =calendar.get(Calendar.MONTH);;
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

    public static VysledokImportu zalozDokladovuDavku(List<ZaznamCsv> zaznam, String file, ParametreImportu parametreImportu, ProgressBarZPN progressBarZPN, Button btnzmaz) {




        List <ChybaImportu> chyby = new ArrayList<>();;
        VysledokImportu vysledok=new VysledokImportu();
        progressBarZPN.nadstavNadpis("Zhranie dokladu");
        progressBarZPN.zobraz();
        Doklad hlavickaDokladu=new Doklad();

        String noveCisloDokladu = noveCisloDokladu(parametreImportu.getDatum());


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
        hlavickaDokladu.setDatum(parametreImportu.getDatum());
        hlavickaDokladu.setPoznamka(file);
        hlavickaDokladu.setStavDokladu(StavDokladu.NEPOTVRDENY);


        //hlavickaDokladu.setFirma(UzivatelNastroje.getVlastnuFirmuPrihlasenehoUzivala());
        hlavickaDokladu.setFirma(parametreImportu.getFirma());



        List<PolozkaDokladu> polozkyDokladu = new ArrayList<>();;









        int i=0;

        for (ZaznamCsv z: zaznam){
            i++;
           // progressBarZPN.posun(new BigDecimal(zaznam.size()),new BigDecimal(i));
            progressBarZPN.setProgresBarValue(new BigDecimal(i).divide(new BigDecimal(zaznam.size()),2,BigDecimal.ROUND_HALF_UP).floatValue());
            btnzmaz.click();


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
                    "Nepodarilo sa zalozit polozku dokladu (firma)"));

        }
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
        progressBarZPN.nadstavNadpis("ukladanie položiek dokladu");
        progressBarZPN.zobraz();
        ulozenyDoklad=vytvorDoklad(hlavickaDokladu);
        int i=0;
        for (PolozkaDokladu polozka: polozkyDokladu){
            i++;
            progressBarZPN.posun(new BigDecimal(polozkyDokladu.size()),new BigDecimal(i));
            polozka.setDoklad(ulozenyDoklad);
            PolozkaDokladuNastroje.vytvorPolozkuDokladu(polozka);
        }
        progressBarZPN.koniec();
    }

    private static Doklad vytvorDoklad(Doklad d) {
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
