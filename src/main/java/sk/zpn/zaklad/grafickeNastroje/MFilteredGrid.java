package sk.zpn.zaklad.grafickeNastroje;

import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.ColumnReorderListener;
import com.vaadin.ui.components.grid.ColumnResizeListener;
import org.vaadin.addons.filteringgrid.FilterGrid;
import sk.zpn.domena.prostredie.UlozenyGrid;
import sk.zpn.zaklad.model.ProstredieUti;

import java.util.Collection;
import java.util.List;

public class MFilteredGrid<T> extends FilterGrid<T> {
    private List<UlozenyGrid> ulozenyGrid;
    private String kluc;
    public MFilteredGrid() {
    }

    public MFilteredGrid(Class<T> beanType) {
        super(beanType);
    }

    public MFilteredGrid(String caption) {
        super(caption);
    }

    public MFilteredGrid(String caption, Collection<T> items) {
        super(caption);
        this.setItems(items);

        this.addColumnResizeListener(new ColumnResizeListener() {
            @Override
            public void columnResize(ColumnResizeEvent event) {
                System.out.println(event.getColumn().getCaption());
            }
        });

    }

    public void registrujZmenuStlpcov(String kluc) {

        this.kluc=kluc;
        if (kluc==null)
            return;
        this.setColumnReorderingAllowed(true);
        this.addColumnReorderListener(new ColumnReorderListener() {
            @Override
            public void columnReorder(ColumnReorderEvent event) {
                if (event.isUserOriginated())
                    ProstredieUti.setPoradieStlpcovGridu((Grid) event.getSource(),kluc);
            }
        });



        this.addColumnResizeListener(new ColumnResizeListener() {
            @Override
            public void columnResize(ColumnResizeEvent event) {
                ProstredieUti.setSirkyStlpcovGridu(kluc,event.getColumn().getId(),event.getColumn().getWidth());
                System.out.print(event.getColumn().getId()+event.getColumn().getWidth());

            }
        });

        //vratenie siriek slpcov
        ulozenyGrid= ProstredieUti.getUlozeneSirkyStlpcovGridu(kluc);
        for (UlozenyGrid g : ulozenyGrid){
            this.getColumn(g.getNazovStlpca()).setWidth(g.getHodnota());
            System.out.println(this.getColumn(g.getNazovStlpca()).getCaption() +this.getColumn(g.getNazovStlpca()).getWidth());
        }
        //vratenie poradia slpcov
        String[] poradiaStlpcov=ProstredieUti.getUlozenePordiaStlpcovGridu(kluc);
        if (poradiaStlpcov!=null)
            this.setColumnOrder(poradiaStlpcov);



    }
}
