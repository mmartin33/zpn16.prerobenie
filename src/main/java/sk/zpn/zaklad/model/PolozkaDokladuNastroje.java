package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import sk.zpn.domena.*;
import sk.zpn.domena.importy.NavratovaHodnota;
import sk.zpn.domena.importy.ZaznamCsv;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
        if (d.isNew()) {
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
        return d;


    }
    public static PolozkaDokladu vytvorPolozkuDokladu(PolozkaDokladu d) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        if (d.isNew()) {
        d.setId(null);
            d.setKedy(new Date());
            d.setKto(UzivatelNastroje.getPrihlasenehoUzivatela().getId());
        }

        em.getTransaction().begin();
        em.persist(d);
        em.getTransaction().commit();
        return d;


    }




    public static void zmazPolozkyDoklady(PolozkaDokladu d) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");

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

    public static NavratovaHodnota vytvorPolozkuZoZaznamuCSV(ZaznamCsv zaznam, Doklad doklad, boolean zalozitFirmu) {
        PolozkaDokladu pd = new PolozkaDokladu();
        FirmaProdukt fp = new FirmaProdukt();

        fp=FirmaProduktNastroje.getFirmaProduktPreImport(doklad.getFirma(),
                                                                      ParametreNastroje.nacitajParametre().getRok(),
                                                                               zaznam.getKit());

        if (fp==null)
            return new NavratovaHodnota(null,NavratovaHodnota.NENAJEDENY_KIT);
        if (!StringUtils.isNotBlank(zaznam.getIco()))
            return new NavratovaHodnota(null,NavratovaHodnota.PRAZDNE_ICO);
        pd.setDoklad(doklad);


        pd.setMnozstvo(zaznam.getMnozstvo().multiply(fp.getKoeficient()));

        //pd.setMnozstvo(zaznam.getMnozstvo());
        pd.setMnozstvoPovodne(zaznam.getMnozstvo());
        pd.setKit(zaznam.getKit());
        int body=VypoctyUtil.vypocitajBody(
                pd.getMnozstvoPovodne(),
                fp.getKoeficient(),
                fp.getProdukt().getKusy(),
                fp.getProdukt().getBody());
        if (body==0)
            return new NavratovaHodnota(null,NavratovaHodnota.NENAJEDENY_KIT);
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
        pd.setProdukt(fp.getProdukt());
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
}
