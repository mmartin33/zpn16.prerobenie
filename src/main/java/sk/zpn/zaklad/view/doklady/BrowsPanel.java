package sk.zpn.zaklad.view.doklady;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.*;
import sk.zpn.zaklad.view.VitajteView;
import sk.zpn.zaklad.view.doklady.polozkaDokladu.PolozkyDokladuView;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private FilterGrid<Doklad> grid;
    private List<Doklad> dokladyList;

    PolozkyDokladuView polozkyDokladuView;
    public Button btnNovy;
    public Button btnPolozky;


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
        FilterGrid.Column<Doklad, String> colDatum = grid.addColumn(Doklad::getFormatovanyDatum).setCaption("Dátum").setId("datum");
        FilterGrid.Column<Doklad, String> colPrevadzkaNazov = grid.addColumn(Doklad::getFirmaNazov).setCaption("Prevadzka").setId("nazovPrevadzky");
        FilterGrid.Column<Doklad, String> colPoznamka = grid.addColumn(Doklad::getPoznamka).setCaption("Poznámka").setId("poznmla");




        // filters
        colCisloDokladu.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colDatum.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colPoznamka.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colTypDokladu.setFilter(new ComboBox<>("", TypDokladu.getListOfDisplayValues()),
                (cValue, fValue) -> fValue == null || fValue.equals(cValue));
        colPrevadzkaNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());

        grid.setColumnOrder(colCisloDokladu, colTypDokladu,colPrevadzkaNazov, colDatum);
        Button btnSpat=new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );


        btnPolozky=new Button("Položky", VaadinIcons.BOOK);
        btnPolozky.addClickListener(clickEvent -> {
                if (grid.getSelectedItems()!=null) {
                    polozkyDokladuView = new PolozkyDokladuView((Doklad) grid.getSelectedItems().iterator().next());

                    UI.getCurrent().getNavigator().addView(PolozkyDokladuView.NAME, polozkyDokladuView);
                    UI.getCurrent().getNavigator().navigateTo(PolozkyDokladuView.NAME);
                    }
                }
        );



        HorizontalLayout tlacitkovy=new HorizontalLayout();
        btnNovy=new Button("Novy",VaadinIcons.FILE_O);


        tlacitkovy.addComponent(btnNovy);

        tlacitkovy.addComponent(btnPolozky);
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






