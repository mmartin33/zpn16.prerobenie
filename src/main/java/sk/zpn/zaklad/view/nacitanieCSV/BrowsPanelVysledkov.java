package sk.zpn.zaklad.view.nacitanieCSV;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.importy.ChybaImportu;
import sk.zpn.domena.importy.VysledokImportu;
import sk.zpn.domena.statistiky.Zaznam;
import sk.zpn.nastroje.NastrojePoli;
import sk.zpn.zaklad.grafickeNastroje.MFilteredGrid;
import sk.zpn.zaklad.view.VitajteView;

import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class BrowsPanelVysledkov extends VerticalLayout {

    private NacitanieCSVView nacitanieCSVView;
    private MFilteredGrid<ChybaImportu> gridChyb;
    private MFilteredGrid<Zaznam> gridKitov;
    private VysledokImportu vysledokImportu;
    private TabSheet tabsheet;


    public Button btnNovy;

    public BrowsPanelVysledkov() {
    }

    private void init() {
        this.setSpacing(false);
        this.setSizeFull();

        GridLayout gl = new GridLayout(1, 3);
        gl.setSizeFull();
        gl.setRowExpandRatio(0, 0.02f);
        gl.setRowExpandRatio(1, 0.02f);
        gl.setRowExpandRatio(2, 0.95f);

        definujBrowsChyb();
        definujBrowsKitov();

        Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME));

        Button btnExport = new Button("Export chýb", VaadinIcons.FILE_TABLE);
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME));

        HorizontalLayout tlacitkovy = new HorizontalLayout();
        tlacitkovy.addComponent(btnSpat);
        tlacitkovy.addComponent(btnExport);


        VerticalLayout zalozka1 = new VerticalLayout();
        VerticalLayout zalozka2 = new VerticalLayout();
        String nadpis = new String("Prehľad chyb importu");

        TabSheet zalozky = new TabSheet();
        gl.addComponent(new Label(nadpis));
        gl.addComponent(tlacitkovy);
        gl.addComponent(zalozky);
        gl.setSizeFull();
        addComponentsAndExpand(gl);
        zalozka1.addComponentsAndExpand(gridChyb);
        gridKitov.setSizeFull();
        gridChyb.setSizeFull();
        zalozka2.addComponentsAndExpand(gridKitov);
        zalozky.setSizeFull();
        zalozka1.setSizeFull();
        zalozka2.setSizeFull();

        zalozky.addTab(zalozka1, "Všetky chyby");
        zalozky.addTab(zalozka2, "Nesparovane kity");
        this.addComponent(gl);

    }


    void refresh() {
        gridChyb.getDataProvider().refreshAll();


    }


    private void definujBrowsKitov() {
        gridKitov = new MFilteredGrid<>();
        /*prerobenie map to list*/
        List<Zaznam> zoznam=null;
        if (this.vysledokImportu.getNespracovaneKity().size()>0){
             zoznam = this.vysledokImportu.getNespracovaneKity().entrySet().stream()
                .map(e -> new Zaznam(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        }



        if (zoznam != null) {
            gridKitov.setItems(zoznam);

            gridKitov.addStyleName("test");
            gridKitov.setSelectionMode(Grid.SelectionMode.SINGLE);
            gridKitov.setWidth(1400, Unit.PIXELS);


            // definitionn of columns
            MFilteredGrid.Column<Zaznam, String> colNazov = gridKitov.addColumn(Zaznam::getKluc).setCaption("Kit").setId("kit");
            MFilteredGrid.Column<Zaznam, BigDecimal> colPocet = gridKitov.addColumn(Zaznam::getHodnota).setCaption("Počet").setId("pocet");
            gridChyb.registrujZmenuStlpcov("browsChybnychKitov");
            // filters
            colNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        }

    }

    private void definujBrowsChyb() {
        gridChyb = new MFilteredGrid<>();
        gridChyb.setItems(this.vysledokImportu.getChyby());
        gridChyb.addStyleName("test");
        gridChyb.setSelectionMode(Grid.SelectionMode.SINGLE);
        gridChyb.setWidth(1400, Unit.PIXELS);
        //gridChyb.setHeight(700, Unit.PIXELS);

        // definitionn of columns
        MFilteredGrid.Column<ChybaImportu, String> colNazovFirmy = gridChyb.addColumn(ChybaImportu::getNazovFirmy).setCaption("Názov firmy").setId("firma");
        MFilteredGrid.Column<ChybaImportu, String> colKit = gridChyb.addColumn(ChybaImportu::getKit).setCaption("KIT").setId("kit");
        MFilteredGrid.Column<ChybaImportu, String> colDoklad = gridChyb.addColumn(ChybaImportu::getDoklad).setCaption("Doklad").setId("doklad");
        MFilteredGrid.Column<ChybaImportu, String> colICo = gridChyb.addColumn(ChybaImportu::getIcoFirmy).setCaption("IČO").setId("ico");
        MFilteredGrid.Column<ChybaImportu, String> colChyba = gridChyb.addColumn(ChybaImportu::getChyba).setCaption("Chyba").setId("chyba");
        gridChyb.registrujZmenuStlpcov("browsVysledkovCSV");
        // filters
        colNazovFirmy.setFilter(new TextField(), StringComparator.containsIgnoreCase());

        colKit.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colChyba.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colDoklad.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colICo.setFilter(new TextField(), StringComparator.containsIgnoreCase());


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






