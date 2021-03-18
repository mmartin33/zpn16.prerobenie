package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import sk.zpn.domena.*;
import sk.zpn.domena.importy.NavratovaHodnota;
import sk.zpn.domena.importy.ZaznamCsv;
import sk.zpn.domena.log.TypLogovanejHodnoty;
import sk.zpn.domena.log.TypUkonu;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PolozkaDokladuNastroje {

    private static final Logger logger = Logger.getLogger(PolozkaDokladuNastroje.class);


    public static Optional<PolozkaDokladu> getPolozkaDokladu(Long id) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<PolozkaDokladu> q = em.createNamedQuery("PolozkaDokladu.get", PolozkaDokladu.class);
        q.setParameter("id", id);
        return Optional.ofNullable(q.getSingleResult());
    }

    public static PolozkaDokladu ulozPolozkuDokladu(PolozkaDokladu d) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypUkonu tu=TypUkonu.OPRAVA;
        if (d.isNew()) {
            tu=TypUkonu.PRIDANIE;
            d.setDoklad(em.find(Doklad.class, d.getDoklad().getId()));

            d.setId((long) 0);
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
        LogAplikacieNastroje.uloz(TypLogovanejHodnoty.POLOZKA_DOKLADU, tu,PolozkaDokladu.getTextLog(d));
        return d;


    }
    public static PolozkaDokladu vytvorPolozkuDokladu(PolozkaDokladu d) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypUkonu tu=TypUkonu.OPRAVA;
        if (d.isNew()) {
        d.setId(null);
            tu=TypUkonu.PRIDANIE;
            d.setKedy(new Date());
            d.setKto(UzivatelNastroje.getPrihlasenehoUzivatela().getId());
        }

        em.getTransaction().begin();
        em.persist(d);
        em.getTransaction().commit();
        LogAplikacieNastroje.uloz(TypLogovanejHodnoty.POLOZKA_DOKLADU, tu,PolozkaDokladu.getTextLog(d));
        return d;


    }




    public static void zmazPolozkyDoklady(PolozkaDokladu d) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        LogAplikacieNastroje.uloz(TypLogovanejHodnoty.POLOZKA_DOKLADU, TypUkonu.VYMAZ,PolozkaDokladu.getTextLog(d));
        em.getTransaction().begin();
        em.remove(d);
        em.getTransaction().commit();


    }

    public static List<PolozkaDokladu> zoznamPoloziekDokladov(Doklad d) {

        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<PolozkaDokladu> q = em.createNamedQuery("PolozkaDokladu.getPolozkyJednehoDokladu", PolozkaDokladu.class);
        q.setParameter("doklad", d);
        em.clear();
        return q.getResultList();
    }

    public static NavratovaHodnota vytvorPolozkuZoZaznamuCSV(ZaznamCsv zaznam, Doklad doklad, boolean zalozitFirmu, Map<String, FirmaProdukt> katKit) {
        PolozkaDokladu pd = new PolozkaDokladu();
        boolean nasloSaNieco=false;
        BigDecimal koeficientMostikovy = BigDecimal.ONE;
        BigDecimal kusyProduktove = BigDecimal.ZERO;
        BigDecimal bodyProduktove = BigDecimal.ZERO;
        Produkt produktNajdeny = null;

        if (katKit == null) {
            //stary sposob
            FirmaProdukt fp = new FirmaProdukt();

            fp = FirmaProduktNastroje.getFirmaProduktPreImport(doklad.getFirma(),
                    ParametreNastroje.nacitajParametre().getRok(),
                    zaznam.getKit(),
                    zaznam.getCiarovyKod());

            if (fp != null) {
                nasloSaNieco = true;
                koeficientMostikovy = fp.getKoeficient();
                kusyProduktove = fp.getProdukt().getKusy();
                bodyProduktove = fp.getProdukt().getBody();
                produktNajdeny=fp.getProdukt();

            }

        }
        else{
            //novy sposob
            FirmaProdukt hodnota = katKit.get(zaznam.getKit() + "-" + ParametreNastroje.nacitajParametre().getRok());
            if (hodnota!=null) {

                koeficientMostikovy = hodnota.getKoeficient();
                kusyProduktove = hodnota.getProdukt().getKusy();
                bodyProduktove = hodnota.getProdukt().getBody();
                produktNajdeny = hodnota.getProdukt();
                nasloSaNieco=true;
            }


        }



        if (!nasloSaNieco)
            return new NavratovaHodnota(null, NavratovaHodnota.NENAJEDENY_KIT, zaznam.getKit());





        if (!StringUtils.isNotBlank(zaznam.getIco()))
            return new NavratovaHodnota(null,NavratovaHodnota.PRAZDNE_ICO,zaznam.getKit());
        pd.setDoklad(doklad);


        pd.setMnozstvo(zaznam.getMnozstvo().multiply(koeficientMostikovy));

        //pd.setMnozstvo(zaznam.getMnozstvo());
        pd.setMnozstvoPovodne(zaznam.getMnozstvo());
        pd.setKit(zaznam.getKit());
        if (koeficientMostikovy.compareTo(BigDecimal.ZERO)==0)
            return new NavratovaHodnota(null,NavratovaHodnota.NEURCENY_KOEFICIENT);

        int body=VypoctyUtil.vypocitajBody(
                pd.getMnozstvoPovodne(),
                koeficientMostikovy,
                kusyProduktove,
                bodyProduktove);
        if (body==0)
            return new NavratovaHodnota(null,NavratovaHodnota.MALY_PREDAJ);
        pd.setBody(new BigDecimal(body));




        Prevadzka prevadzka=PrevadzkaNastroje.najdiAleboZaloz(zaznam.getIco(), zaznam.getNazvFirmy(),zalozitFirmu);
        if (prevadzka==null) {
            if (!zalozitFirmu)
                return new NavratovaHodnota(null, NavratovaHodnota.MALO_BODOV);
            return new NavratovaHodnota(null, NavratovaHodnota.NENAJEDENA_FIRMA);
        }

        pd.setPrevadzka(prevadzka);
        pd.setPoberatel(prevadzka.getPoberatel());

        pd.setPoznamka(zaznam.getMtzDoklad());
        pd.setProdukt(produktNajdeny);
        pd.setPoznamka(zaznam.getMtzDoklad());


        return new NavratovaHodnota(pd,NavratovaHodnota.NIC);
    }


    public static List<Object[]> zoznamPohybovZaPoberatela(Long id) {

        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.clear();
        TypedQuery<Object[]> q = em.createNamedQuery("PolozkaDokladu.getPolozkyPoberatela", Object[].class);
        q.setParameter("id", id);
        em.clear();
        return q.getResultList();


    }

    public static void aktualizujPoberatela(Doklad ulozenyDoklad) {
        List <PolozkaDokladu> zoznamPoloziek=zoznamPoloziekDokladov(ulozenyDoklad);
        for (PolozkaDokladu pd : zoznamPoloziek) {
            pd.setPoberatel(ulozenyDoklad.getPoberatel());
            PolozkaDokladuNastroje.ulozPolozkuDokladu(pd);
        }

    }

    public static void vytvorPolozkuDokladuDavkovo(List<PolozkaDokladu> polozkyDokladu, Doklad ulozenyDoklad) {
        Long idPrihlasenehoUzivatela=UzivatelNastroje.getPrihlasenehoUzivatela().getId();
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypUkonu tu = TypUkonu.OPRAVA;
        int j=0;
        int i=0;
        System.out.println("start vytvortPolozkuDokladuDavkovo");
        for (PolozkaDokladu polozka : polozkyDokladu) {
            j++;
            i++;
            if (polozka.isNew()) {
                polozka.setId(null);
                tu = TypUkonu.PRIDANIE;
                polozka.setKedy(new Date());
                polozka.setKto(idPrihlasenehoUzivatela);
            }
            if (j==1 )
                em.getTransaction().begin();


            em.persist(polozka);


            if (j==500 ) {
                em.getTransaction().commit();
                j=0;
                System.out.println("Doklad:"+ulozenyDoklad.getFirmaNazov()+ulozenyDoklad.getCisloDokladu()+" ukladana polozka  "+i);
            }

        }
        if (j!=500)
            em.getTransaction().commit();
        System.out.println("koniec vytvortPolozkuDokladuDavkovo");
        //LogAplikacieNastroje.uloz(TypLogovanejHodnoty.POLOZKA_DOKLADU, tu,PolozkaDokladu.getTextLog(d));

    }

}
