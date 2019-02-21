package sk.zpn.zaklad.view.doklady;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.*;
import sk.zpn.zaklad.view.VitajteView;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private FilterGrid<Doklad> grid;
    private List<Doklad> dokladyList;


    public Button btnNovy;


    public BrowsPanel(List<Doklad> dokladyList) {
        this.dokladyList = dokladyList;
        grid = new FilterGrid<>();
        grid.setItems(this.dokladyList);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setWidth(1000, Unit.PIXELS);
        grid.setHeight(700, Unit.PIXELS);

        // definitionn of columns
        FilterGrid.Column<Doklad, String> colCisloDokladu = grid.addColumn(Doklad::getCisloDokladu).setCaption("Doklad").setId("doklad");
        FilterGrid.Column<Doklad, String> colTypDokladu = grid.addColumn(doklad -> doklad.getTypDokladu().getDisplayValue()).setCaption("Typ dokladu").setId("typ");
        FilterGrid.Column<Doklad, Date> colDatum = grid.addColumn(Doklad::getDatum).setCaption("Dátum").setId("datum");
        FilterGrid.Column<Doklad, String> colFirmaNazov = grid.addColumn(Doklad::getFirmaNazov).setCaption("Firma").setId("nazovFirmy");
        FilterGrid.Column<Doklad, String> colPoznamka = grid.addColumn(Doklad::getPoznamka).setCaption("Poznámka").setId("poznmla");




        // filters
        colCisloDokladu.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colDatum.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colPoznamka.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colTypDokladu.setFilter(new ComboBox<>("", TypDokladu.getListOfDisplayValues()),
                (cValue, fValue) -> fValue == null || fValue.equals(cValue));
        colFirmaNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());

        grid.setColumnOrder(colCisloDokladu, colTypDokladu,colFirmaNazov, colDatum);
        Button btnSpat=new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );



        HorizontalLayout tlacitkovy=new HorizontalLayout();
        btnNovy=new Button("Novy",VaadinIcons.FILE_O);


        tlacitkovy.addComponent(btnNovy);
        tlacitkovy.addComponent(btnSpat);//666


        this.addComponent(new Label("Prehľad Dokladov"));
        this.addComponents(grid);


        this.addComponent(tlacitkovy);




    }


    void refresh() {
        grid.getDataProvider().refreshAll();
        System.out.println("Refresh browsu all");

    }


    void addSelectionListener(Consumer<Doklad> listener) {
        grid.asSingleSelect()
                .addValueChangeListener(e -> listener.accept(e.getValue()));
    }

    void deselect() {
        Doklad value = grid.asSingleSelect().getValue();
        if (value != null) {
            grid.getSelectionModel().deselect(value);
        }
    }

    void selectFirst() {
        List<Doklad> prvyDokladList = grid.getDataCommunicator().fetchItemsWithRange(0,1);
        if(prvyDokladList.size() > 0){
            grid.asSingleSelect().select(prvyDokladList.get(0));
        }
    }

    void selectDoklad(Doklad doklad) {
        Optional.ofNullable(doklad).ifPresent(grid.asSingleSelect()::select);
    }

}






