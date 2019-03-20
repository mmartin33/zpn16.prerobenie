package sk.zpn.zaklad.view.nacitanieCSV;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.ChybaImportu;
import sk.zpn.domena.Poberatel;
import sk.zpn.zaklad.view.VitajteView;
import sk.zpn.zaklad.view.poberatelia.PoberateliaView;
import sk.zpn.zaklad.view.prevadzky.PrevadzkyView;

import java.util.List;
import java.util.function.Consumer;

public class BrowsPanelVysledku extends VerticalLayout {

    private NacitanieCSVView nacitanieCSVView;
    private FilterGrid<ChybaImportu> grid;
    private List<ChybaImportu> chybyList;


    public Button btnNovy;


        public BrowsPanelVysledku(List<ChybaImportu> chybyList, NacitanieCSVView nacitanieCSVView) {

            this.nacitanieCSVView = nacitanieCSVView;
            this.chybyList = chybyList;
            this.setSpacing(false);
            grid = new FilterGrid<>();
            grid.setItems(this.chybyList);

            grid.addStyleName("test");
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            grid.setWidth(1000, Unit.PIXELS);
            grid.setHeight(700, Unit.PIXELS);

            // definitionn of columns
            FilterGrid.Column<ChybaImportu, String> colNazovFirmy = grid.addColumn(ChybaImportu::getNazovFirmy).setCaption("Názov firmy").setId("firma");
            FilterGrid.Column<ChybaImportu, String> colKit = grid.addColumn(ChybaImportu::getKit).setCaption("KIT").setId("kit");
            FilterGrid.Column<ChybaImportu, String> colChyba = grid.addColumn(ChybaImportu::getChyba).setCaption("Chyba").setId("chyba");

            // filters
            colNazovFirmy.setFilter(new TextField(), StringComparator.containsIgnoreCase());

            colKit.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colChyba.setFilter(new TextField(), StringComparator.containsIgnoreCase());

            Button btnSpat=new Button("Späť", VaadinIcons.ARROW_BACKWARD);
            btnSpat.addClickListener(clickEvent ->
                    UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME));

            HorizontalLayout tlacitkovy=new HorizontalLayout();
            tlacitkovy.addComponent(btnSpat);


            String nadpis=new String("Prehľad chyb importu");
            this.addComponent(new Label(nadpis));
            this.addComponents(grid);
            this.addComponent(tlacitkovy);

        }


        void refresh() {
            grid.getDataProvider().refreshAll();


        }
    }






