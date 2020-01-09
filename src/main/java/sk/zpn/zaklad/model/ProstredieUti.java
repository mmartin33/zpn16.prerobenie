package sk.zpn.zaklad.model;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import sk.zpn.domena.prostredie.TypHodnotyStlpca;
import sk.zpn.domena.prostredie.UlozenyGrid;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class ProstredieUti {
    public static List<UlozenyGrid> getUlozeneSirkyStlpcovGridu(String kluc) {
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<UlozenyGrid> q = em.createNamedQuery("UlozenyGrid.get", UlozenyGrid.class);
        q.setParameter("kluc", kluc);
        q.setParameter("typ_hodnoty", TypHodnotyStlpca.SIRKA);
        q.setParameter("id_uzivatela", UzivatelNastroje.getPrihlasenehoUzivatela().getId());
        return q.getResultList();


    }

    public static void setSirkyStlpcovGridu(String kluc, String nazovStlpca, double sirkaStlpca) {
        UlozenyGrid ug = getUlozenaSirkaStlpcaGridu(kluc, nazovStlpca);
        if (ug == null) {
            ug = new UlozenyGrid();
            ug.setNazovStlpca(nazovStlpca);
            ug.setKluc(kluc);
            ug.setUzivatel(UzivatelNastroje.getPrihlasenehoUzivatela());
            ug.setTypHodnotyStlpca(TypHodnotyStlpca.SIRKA);
        }
        ug.setHodnota(sirkaStlpca);
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        em.getTransaction().begin();
        if (ug.getId() == null)
            em.persist(ug);
        else
            em.merge(ug);
        em.getTransaction().commit();


    }


    public static UlozenyGrid getUlozenaSirkaStlpcaGridu(String kluc, String nazovStlpca) {
        List<UlozenyGrid> ug;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<UlozenyGrid> q = em.createNamedQuery("UlozenyStlpecGridu.get", UlozenyGrid.class);
        q.setParameter("kluc", kluc);
        q.setParameter("id_uzivatela", UzivatelNastroje.getPrihlasenehoUzivatela().getId());
        q.setParameter("nazov_stlpca", nazovStlpca);
        q.setParameter("typ_hodnoty", TypHodnotyStlpca.SIRKA);
        q.setMaxResults(1);
        ug = q.getResultList();
        if (ug.isEmpty())
            return null;
        else
            return q.getResultList().get(0);
    }


    public static void setPoradieStlpcovGridu(Grid grid, String kluc) {
        List<Column> zoznamStlpcov = grid.getColumns();
        UlozenyGrid ug;
        int i = 0;
        for (Column col : zoznamStlpcov) {
            i++;
            ug = getUlozenePoradieStlpcaGridu(kluc, col.getId());

            if (ug == null) {
                ug = new UlozenyGrid();
                ug.setNazovStlpca(col.getId());
                ug.setKluc(kluc);
                ug.setUzivatel(UzivatelNastroje.getPrihlasenehoUzivatela());
                ug.setTypHodnotyStlpca(TypHodnotyStlpca.PORADIE);
            }
            ug.setHodnota(new Double(i));
            EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
            em.getTransaction().begin();
            if (ug.getId() == null)
                em.persist(ug);
            else
                em.merge(ug);
            em.getTransaction().commit();

        }


    }

    private static UlozenyGrid getUlozenePoradieStlpcaGridu(String kluc, String nazovStlpca) {
        List<UlozenyGrid> ug;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<UlozenyGrid> q = em.createNamedQuery("UlozenyStlpecGridu.get", UlozenyGrid.class);
        q.setParameter("kluc", kluc);
        q.setParameter("id_uzivatela", UzivatelNastroje.getPrihlasenehoUzivatela().getId());
        q.setParameter("nazov_stlpca", nazovStlpca);
        q.setParameter("typ_hodnoty", TypHodnotyStlpca.PORADIE);
        q.setMaxResults(1);
        ug = q.getResultList();
        if (ug.isEmpty())
            return null;
        else
            return q.getResultList().get(0);


    }

    public static String[] getUlozenePordiaStlpcovGridu(String kluc) {
        List<UlozenyGrid> zoznam;
        List<String> radenieStlpcov =  new ArrayList<>();;
        EntityManager em = (EntityManager) VaadinSession.getCurrent().getAttribute("createEntityManager");
        TypedQuery<UlozenyGrid> q = em.createNamedQuery("UlozenyGrid.get", UlozenyGrid.class);
        q.setParameter("kluc", kluc);
        q.setParameter("typ_hodnoty", TypHodnotyStlpca.PORADIE);
        q.setParameter("id_uzivatela", UzivatelNastroje.getPrihlasenehoUzivatela().getId());
        zoznam = q.getResultList();
        if (!zoznam.isEmpty()) {
            for (UlozenyGrid g : zoznam) {
                radenieStlpcov.add(g.getNazovStlpca());
            }
            String[] itemsArray = new String[radenieStlpcov.size()];
            itemsArray = radenieStlpcov.toArray(itemsArray);

            return itemsArray;
        }
        return null;

    }
}
