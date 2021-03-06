package sk.zpn.zaklad.model;

import com.google.common.collect.Maps;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import sk.zpn.SystemoveParametre;
import sk.zpn.domena.*;
import sk.zpn.domena.importy.*;
import sk.zpn.domena.log.TypLogovanejHodnoty;
import sk.zpn.domena.log.TypUkonu;
import sk.zpn.zaklad.grafickeNastroje.ProgressBarZPN;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class DokladyNastroje {

    private static final Logger logger = Logger.getLogger(DokladyNastroje.class);


    public static Optional<Doklad> getDoklad(Long id) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<Doklad> q = em.createNamedQuery("Doklad.get", Doklad.class);
        q.setParameter("id", id);
        return Optional.ofNullable(q.getSingleResult());
    }

    public static Doklad ulozDoklad(Doklad d) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypUkonu tu=TypUkonu.OPRAVA;
        if (d.isNew()) {
            tu=TypUkonu.PRIDANIE;
            if (d.getStavDokladu() == null)
                d.setStavDokladu(StavDokladu.POTVRDENY);
            d.setId(null);
            d.setKedy(new Date());
            d.setKto(UzivatelNastroje.getPrihlasenehoUzivatela().getId());
            em.getTransaction().begin();
            em.persist(d);
        } else {
            em.getTransaction().begin();
            em.merge(d);
        }
        em.getTransaction().commit();
        LogAplikacieNastroje.uloz(TypLogovanejHodnoty.DOKLAD, tu,Doklad.getTextLog(d));
        System.out.println("Ulozeny doklad" + d.getCisloDokladu());
        return d;


    }

    public static void zmazDoklad(Doklad d) {

        vymazVsetkyPolozky(d);

        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        String sql;

        if (d == null)
            return ;
        LogAplikacieNastroje.uloz(TypLogovanejHodnoty.DOKLAD, TypUkonu.VYMAZ,Doklad.getTextLog(d));
        em.getTransaction().begin();
        Query query = em.createQuery(
                "delete  FROM  doklady p where COLUMN('id', p) = :id");

        int deletedCount = query.setParameter("id", d.getId()).executeUpdate();
        em.getTransaction().commit();
        return ;




        //        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
//
//
//        System.out.println("Vymazany doklad:" + d.getCisloDokladu());
//        em.getTransaction().begin();
//        em.merge(d);
//        em.flush();
//        em.detach(d);
//        em.remove(d);
//        em.getTransaction().commit();


    }

    public static Optional<TypDokladu> TypDokladu() {
        Optional<Doklad> doklad = getDoklad((Long) VaadinSession.getCurrent().getAttribute("id_dokladu"));
        return doklad.map(Doklad::getTypDokladu);
    }


    public static List<Doklad> zoznamDokladov(Firma velkosklad,String rok) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<Doklad> q;
        if (velkosklad != null) {
            if (rok == null || rok.length()==0) {
                   q = em.createNamedQuery("Doklad.getZaFirmu", Doklad.class);
                q.setParameter("id", velkosklad.getId());
            }else {
                q = em.createNamedQuery("Doklad.getZaFirmuARok", Doklad.class);
                q.setParameter("id", velkosklad.getId());
                q.setParameter("rok", rok);
            }
        } else
        if (rok == null || rok.length()==0)
            q = em.createNamedQuery("Doklad.getAll", Doklad.class);
        else {
            q = em.createNamedQuery("Doklad.getAllZaRok", Doklad.class);
            q.setParameter("rok", rok);
        }
        return q.getResultList();
    }

    public static String noveCisloDokladu(Date datum) {
        boolean prazdny = true;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        String sql;
        String obdobie = null;
        if (datum == null)
            sql = "SELECT  max(cisloDokladu) FROM  doklady";
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(datum);
            Integer rok = calendar.get(Calendar.YEAR);
            Integer mesiac = calendar.get(Calendar.MONTH) + 1;
            obdobie = (rok.toString() + StringUtils.leftPad(mesiac.toString(), 2, '0'));
            sql = "SELECT  max(cisloDokladu) FROM  doklady where substr(cislodokladu,1,6)='" + obdobie + "'";

        }

        Query query = em.createNativeQuery(sql);

        String result = (String) query.getSingleResult();

        if (result != null)
            return Long.toString(Long.parseLong(result) + 1);
        else
            return obdobie + "001";
    }

    public static int pocetPoloziek(Doklad doklad) {

        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        String sql;

        if (doklad == null)
            return 0;
        sql = "SELECT  count(*) FROM  polozkydokladu where doklad_id=?";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, doklad.getId());
        Object o =query.getSingleResult();
        if (o!=null) {
            Integer result = ((Long) o).intValue();
            return result;
        }
        else
            return 0;
    }
    public static int sumaBodov(Doklad doklad) {

        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        String sql;

        if (doklad == null)
            return 0;
        sql = "SELECT  sum(body) FROM  polozkydokladu where doklad_id=?";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, doklad.getId());
        Object o=query.getSingleResult();
        if (o!=null) {
            Integer result = ((Double) o).intValue();
            return result;
        }
        else
            return 0;
    }

    public static boolean vymazVsetkyPolozky(Doklad doklad) {

        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        String sql;

        if (doklad == null)
            return true;
        em.getTransaction().begin();
        Query query = em.createQuery(
                "delete  FROM  polozkyDokladu p where COLUMN('doklad_id', p) = :id");

        int deletedCount = query.setParameter("id", doklad.getId()).executeUpdate();
        em.getTransaction().commit();
        return true;
    }

    public static VysledokImportu zalozDokladovuDavku(Davka davka, String file, ParametreImportu parametreImportu, ProgressBarZPN progressBarZPN) {
        Uzivatel uzivatelVelkoskladu=null;
        String icoUzivatela=null;
        Map<String, ZaznamCsv> zaznam;
        Map<String, Integer> bodyNaIco ;
        Map<String, FirmaProdukt> katKit ;
        Map<String, BigDecimal> nespraovaneKity = Maps.newHashMap();;

        Integer mesacnySucetBodov;
        boolean zalozitFirmu;
        boolean pustitiDoDavky=true;
        zaznam=davka.getPolozky();
        bodyNaIco=davka.getBodyNaIco();
        katKit=davka.getKatKit();
        List<ChybaImportu> chyby = new ArrayList<>();
        Map<String, BigDecimal> icaVelkoskladov = FirmaNastroje.mapaICOFiriemIbaVelkosklady();
        Integer bodovaHranicaPreZakladanieNovejFirmy=ParametreNastroje.nacitajParametre().getMesacnaHranicaBodovImportu();
        uzivatelVelkoskladu=UzivatelNastroje.getUzivatelVelkoskladu(parametreImportu.getFirma());

        if (uzivatelVelkoskladu==null)
            return null;
        icoUzivatela=uzivatelVelkoskladu.getFirma().getIco();
        VysledokImportu vysledok = new VysledokImportu();
        progressBarZPN.nadstavNadpis("Zhranie dokladu");
        progressBarZPN.nadstavspustenie(true);
        progressBarZPN.zobraz();
        Doklad hlavickaDokladu = new Doklad();

        String noveCisloDokladu = noveCisloDokladu(parametreImportu.getDatum());


        if (noveCisloDokladu == null || noveCisloDokladu.isEmpty()) {
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


        List<PolozkaDokladu> polozkyDokladu = new ArrayList<>();



        int i = 0;
        System.out.println("Start ukladanie poloziek");
        for (Map.Entry<String, ZaznamCsv> entry : zaznam.entrySet()) {
            pustitiDoDavky=true;
            ZaznamCsv z = entry.getValue();
            i++;
            // progressBarZPN.posun(new BigDecimal(zaznam.size()),new BigDecimal(i));
            progressBarZPN.setProgresBarValue(new BigDecimal(i).divide(new BigDecimal(zaznam.size()), 2, BigDecimal.ROUND_HALF_UP).floatValue());
            if (new BigDecimal(i).remainder(new BigDecimal(1000)).compareTo(BigDecimal.ZERO) == 0)
                System.out.println("Vytvaranie zaznamov" + i);
            NavratovaHodnota navratovahodnota=null;


            if (uzivatelVelkoskladu.getUrcujeFirmyNaKtoreSaPridelujuBody())
                if (!FirmaVelkoskladuNastroje.existujeFirmaVelkoskladuPodlaIcoFirmy(parametreImportu.getFirma(),z.getIco()))
                    pustitiDoDavky=false;
            if (icaVelkoskladov.get(z.getIco())!=null && !z.getIco().equals(icoUzivatela))  //nepojdu take co si velkosklady ale vlastne ico velkoskladu pojde
                    pustitiDoDavky=false;
            if (pustitiDoDavky)
            {
                mesacnySucetBodov = bodyNaIco.get(z.getIco());
                zalozitFirmu = (mesacnySucetBodov >= bodovaHranicaPreZakladanieNovejFirmy ? true : false);
                navratovahodnota = PolozkaDokladuNastroje.vytvorPolozkuZoZaznamuCSV(z, hlavickaDokladu, zalozitFirmu,katKit);
            }
            else{
                navratovahodnota=new NavratovaHodnota(null,NavratovaHodnota.NEPOVOLENA_FIRMA);
            }

            //PolozkaDokladu pd=PolozkaDokladuNastroje.vytvorPolozkuZoZaznamuCSV(z,hlavickaDokladu);
            if (navratovahodnota.getPolozkaDokladu() != null)
                polozkyDokladu.add(navratovahodnota.getPolozkaDokladu());
            else if (navratovahodnota.getChyba() == NavratovaHodnota.NENAJEDENY_KIT){
                nespraovaneKity.put(z.getKit()+" "+z.getNazov(),z.getMnozstvo());
                chyby.add(new ChybaImportu(
                        z.getNazvFirmy(),
                        z.getIco(),
                        z.getKit(),
                        "Nenajdeny kit",
                        z.getMtzDoklad()));}
            else if (navratovahodnota.getChyba() == NavratovaHodnota.NEURCENY_KOEFICIENT)

                chyby.add(new ChybaImportu(
                        z.getNazvFirmy(),
                        z.getIco(),
                        z.getKit(),
                        "Na kit-e nebol urceny koeficient",
                        z.getMtzDoklad()));
            else if (navratovahodnota.getChyba() == NavratovaHodnota.NENAJEDENA_FIRMA)

                chyby.add(new ChybaImportu(
                        z.getNazvFirmy(),
                        z.getIco(),
                        z.getKit(),
                        "Nepodarilo sa zalozit polozku dokladu (firma)",
                        z.getMtzDoklad()));
            else if (navratovahodnota.getChyba() == NavratovaHodnota.PREVADZKA_NEMA_POBERATELA)

                chyby.add(new ChybaImportu(
                        z.getNazvFirmy(),
                        z.getIco(),
                        z.getKit(),
                        "Prevadzka nema poberatela",
                        z.getMtzDoklad()));
            else if (navratovahodnota.getChyba() == NavratovaHodnota.PRAZDNE_ICO)
                chyby.add(new ChybaImportu(
                        z.getNazvFirmy(),
                        z.getIco(),
                        z.getKit(),
                        "Nepodarilo sa zalozit polozku dokladu (firma-prazdne ICO)",
                        z.getMtzDoklad()));
            else if (navratovahodnota.getChyba() == NavratovaHodnota.NEPOVOLENA_FIRMA)
                chyby.add(new ChybaImportu(
                        z.getNazvFirmy(),
                        z.getIco(),
                        z.getKit(),
                        "Nepovolene ICO",
                        z.getMtzDoklad()));
            else if (navratovahodnota.getChyba() == NavratovaHodnota.MALO_BODOV)
                chyby.add(new ChybaImportu(
                        z.getNazvFirmy(),
                        z.getIco(),
                        z.getKit(),
                        "Nepodarilo sa zalozit polozku dokladu (firma-odobrala malo bodov pre registraciu)",
                        z.getMtzDoklad()));
            else if (navratovahodnota.getChyba() == NavratovaHodnota.MALY_PREDAJ)
                chyby.add(new ChybaImportu(
                        z.getNazvFirmy(),
                        z.getIco(),
                        z.getKit(),
                        "Nepodarilo sa zalozit polozku dokladu (nepostacujuce mnozstvo)",
                        z.getMtzDoklad()));
            else if (navratovahodnota.getChyba() == NavratovaHodnota.PREKROCENY_MAX_PREDAJ)
                chyby.add(new ChybaImportu(
                        z.getNazvFirmy(),
                        z.getIco(),
                        z.getKit(),
                        "Nepodarilo sa zalozit polozku dokladu (prekrocene mnozstvo)",
                        z.getMtzDoklad()));

        }
        System.out.println("Koniec vytvarania zaznamov" + i);
        progressBarZPN.koniec();
        System.out.println("Start ulozenia dokladu");
        DokladyNastroje.ulozDokladDavky(hlavickaDokladu, polozkyDokladu, progressBarZPN);
        System.out.println("Koniec ulozenia dokladu");
        vysledok.setDoklad(hlavickaDokladu);
        vysledok.setPolozky(polozkyDokladu);
        vysledok.setChyby(chyby);
        vysledok.setNespracovaneKity(nespraovaneKity);
        return vysledok;


    }

    private static void vymazPodHranicou(Doklad hlavickaDokladu, Integer mesacnaHranicaBodovImportu) {

    }


    private static void ulozDokladDavky(Doklad hlavickaDokladu, List<PolozkaDokladu> polozkyDokladu, ProgressBarZPN progressBarZPN) {
        if (polozkyDokladu.size() == 0)
            return;
        Doklad ulozenyDoklad;
        progressBarZPN.nadstavNadpis("ZPN - ukladanie polo??iek dokladu");
        System.out.println("Star tvorby poloziek dokladu");
        progressBarZPN.nadstavspustenie(true);
        progressBarZPN.zobraz();
        ulozenyDoklad = vytvorDoklad(hlavickaDokladu);
        int i = 0;
//        for (PolozkaDokladu polozka : polozkyDokladu) {
//            i++;
//            if (new BigDecimal(i).remainder(new BigDecimal(100)).compareTo(BigDecimal.ZERO) == 0)
//                System.out.println("ZPN- Vytvaranie poloziek dokladov" + i);
//
//            progressBarZPN.posun(new BigDecimal(polozkyDokladu.size()), new BigDecimal(i));
//            polozka.setDoklad(ulozenyDoklad);
//            PolozkaDokladuNastroje.vytvorPolozkuDokladu(polozka);
//        }
            PolozkaDokladuNastroje.vytvorPolozkuDokladuDavkovo(polozkyDokladu,ulozenyDoklad);


        System.out.println("ZPN - Koniec vytvaranie dokladov" + i);

        progressBarZPN.koniec();
    }

    public static Doklad vytvorDoklad(Doklad d) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypUkonu tu=TypUkonu.OPRAVA;
        if (d.isNew()) {
            tu=TypUkonu.PRIDANIE;
            if (d.getStavDokladu() == null)
                d.setStavDokladu(StavDokladu.POTVRDENY);
            d.setId(null);
            d.setKedy(new Date());
            d.setKto(UzivatelNastroje.getPrihlasenehoUzivatela().getId());
        }
        System.out.println("Ulozeny doklad" + d.getCisloDokladu());
        em.getTransaction().begin();
        em.persist(d);
        em.getTransaction().commit();
        LogAplikacieNastroje.uloz(TypLogovanejHodnoty.DOKLAD, tu,Doklad.getTextLog(d));
        return d;
    }

    public static String noveCisloDokladuOdmien() {
        boolean prazdny = true;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        String sql;
        String obdobie = ParametreNastroje.nacitajParametre().getRok();
        sql = "SELECT  max(cisloDokladuodmeny) FROM  doklady ";



        Query query = em.createNativeQuery(sql);

        String result = (String) query.getSingleResult();

        if (!StringUtils.isEmpty(result))
            return Long.toString(Long.parseLong(result) + 1);
        else
            return obdobie + "001";

    }

    public static List<Doklad> zoznamDokladovOdmien(Firma velkosklad,String rok) {

        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        if (rok.length()==0)
            rok=null;
        TypedQuery<Doklad> q;
        if (rok==null)
            q = em.createNamedQuery("Odmena.getAll", Doklad.class);
        else{
            q = em.createNamedQuery("Odmena.getAllZaRok", Doklad.class);
            q.setParameter("rok",rok);
        }
        return q.getResultList();


    }

}
