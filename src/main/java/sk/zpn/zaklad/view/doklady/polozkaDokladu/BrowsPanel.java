package sk.zpn.zaklad.view.doklady.polozkaDokladu;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.PolozkaDokladu;
import sk.zpn.zaklad.view.VitajteView;
import sk.zpn.zaklad.view.doklady.DokladyView;

import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private FilterGrid<PolozkaDokladu> grid;
    private List<PolozkaDokladu> polozkyDokladuList;

    private PolozkyDokladuView polozkyDokladuView;
    public Button btnNovy;



    public BrowsPanel(List<PolozkaDokladu> polozkyDokladuList, PolozkyDokladuView pdv) {
        this.polozkyDokladuView=pdv;
        this.polozkyDokladuList = polozkyDokladuList;
        grid = new FilterGrid<>();
        grid.setItems(this.polozkyDokladuList);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setWidth(1000, Unit.PIXELS);
        grid.setHeight(700, Unit.PIXELS);

        // definitionn of columns
        FilterGrid.Column<PolozkaDokladu, String> colProduktKod = grid.addColumn(PolozkaDokladu::getProduktKod).setCaption("Kód produktu").setId("kodProduktu");
        FilterGrid.Column<PolozkaDokladu, String> colProduktNazov = grid.addColumn(PolozkaDokladu::getProduktNazov).setCaption("Názov produktu").setId("nazovProduktu");
        FilterGrid.Column<PolozkaDokladu, String> colFirmaNazov = grid.addColumn(PolozkaDokladu::getFirmaNazov).setCaption("Firma").setId("nazovFirmy");
        FilterGrid.Column<PolozkaDokladu, String> colPoberatel = grid.addColumn(PolozkaDokladu::getPoberatelMenoAdresa).setCaption("poberatel").setId("menoPoberatela");
        FilterGrid.Column<PolozkaDokladu, Double> colBody = grid.addColumn(PolozkaDokladu::getBody).setCaption("Body").setId("body");
        FilterGrid.Column<PolozkaDokladu, Double> colMnozstvo = grid.addColumn(PolozkaDokladu::getMnozstvo).setCaption("Množstvo").setId("mnozstvo");
        FilterGrid.Column<PolozkaDokladu, String> colPoznamka = grid.addColumn(PolozkaDokladu::getPoznamka).setCaption("Poznámka").setId("poznamka");

        // filters
        colProduktKod.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colProduktNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colFirmaNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colPoberatel.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colPoznamka.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        Button btnSpat=new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(DokladyView.NAME)
        );


        HorizontalLayout tlacitkovy=new HorizontalLayout();
        btnNovy=new Button("Novy",VaadinIcons.FILE_O);


        tlacitkovy.addComponent(btnNovy);
        tlacitkovy.addComponent(btnSpat);//666

        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String formatovanyDatum =simpleDateFormat.format(polozkyDokladuView.getDoklad().getDatum());
        this.addComponent(new Label("Prehľad položiek dokladu:  "+polozkyDokladuView.getDoklad().getCisloDokladu()+" z dátumu:"+formatovanyDatum));
        this.addComponents(grid);


        this.addComponent(tlacitkovy);




    }


    void refresh() {
        grid.getDataProvider().refreshAll();
        System.out.println("Refresh browsu all");

    }


    void addSelectionListener(Consumer<PolozkaDokladu> listener) {
        grid.asSingleSelect()
                .addValueChangeListener(e -> listener.accept(e.getValue()));
    }

    void deselect() {
        PolozkaDokladu value = grid.asSingleSelect().getValue();
        if (value != null) {
            grid.getSelectionModel().deselect(value);
        }
    }

    void selectFirst() {
        List<PolozkaDokladu> prvaPolozkaDokladu = grid.getDataCommunicator().fetchItemsWithRange(0,1);
        if(prvaPolozkaDokladu.size() > 0){
            grid.asSingleSelect().select(prvaPolozkaDokladu.get(0));
        }
    }

    void selectDoklad(PolozkaDokladu polozkaDokladu) {
        Optional.ofNullable(polozkaDokladu).ifPresent(grid.asSingleSelect()::select);
    }

}






