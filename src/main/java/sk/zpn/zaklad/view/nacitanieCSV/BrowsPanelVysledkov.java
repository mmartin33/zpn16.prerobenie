package sk.zpn.zaklad.view.nacitanieCSV;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.importy.ChybaImportu;
import sk.zpn.domena.importy.VysledokImportu;
import sk.zpn.zaklad.grafickeNastroje.MFilteredGrid;
import sk.zpn.zaklad.view.VitajteView;

public class BrowsPanelVysledkov extends VerticalLayout {

    private NacitanieCSVView nacitanieCSVView;
    private MFilteredGrid<ChybaImportu> grid;
    private VysledokImportu vysledokImportu;



    public Button btnNovy;

        public BrowsPanelVysledkov(){}

        private void  init(){
            this.setSpacing(false);
            grid = new MFilteredGrid<>();
            grid.setItems(this.vysledokImportu.getChyby());

            grid.addStyleName("test");
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            grid.setWidth(1400, Unit.PIXELS);
            grid.setHeight(700, Unit.PIXELS);

            // definitionn of columns
            MFilteredGrid.Column<ChybaImportu, String> colNazovFirmy = grid.addColumn(ChybaImportu::getNazovFirmy).setCaption("Názov firmy").setId("firma");
            MFilteredGrid.Column<ChybaImportu, String> colKit = grid.addColumn(ChybaImportu::getKit).setCaption("KIT").setId("kit");
            MFilteredGrid.Column<ChybaImportu, String> colDoklad = grid.addColumn(ChybaImportu::getDoklad).setCaption("Doklad").setId("doklad");
            MFilteredGrid.Column<ChybaImportu, String> colICo = grid.addColumn(ChybaImportu::getIcoFirmy).setCaption("IČO").setId("ico");
            MFilteredGrid.Column<ChybaImportu, String> colChyba = grid.addColumn(ChybaImportu::getChyba).setCaption("Chyba").setId("chyba");
            grid.registrujZmenuStlpcov("browsVysledkovCSV");
            // filters
            colNazovFirmy.setFilter(new TextField(), StringComparator.containsIgnoreCase());

            colKit.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colChyba.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colDoklad.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colICo.setFilter(new TextField(), StringComparator.containsIgnoreCase());



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

    public VysledokImportu getVysledokImportu() {
        return vysledokImportu;
    }

    public void setVysledokImportu(VysledokImportu vysledokImportu) {
        this.vysledokImportu = vysledokImportu;
        this.init();
        this.setVisible(true);
        this.refresh();

    }
}






