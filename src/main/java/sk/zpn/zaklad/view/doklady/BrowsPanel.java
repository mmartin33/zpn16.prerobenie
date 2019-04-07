package sk.zpn.zaklad.view.doklady;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

import com.vaadin.ui.renderers.HtmlRenderer;
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
        GridLayout gl = new GridLayout(1, 3);
        gl.setSizeFull();
        gl.setRowExpandRatio(0, 0.05f);
        gl.setRowExpandRatio(1, 0.90f);

        this.dokladyList = dokladyList;
        grid = new FilterGrid<>();
        grid.setItems(this.dokladyList);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);


        // definitionn of columns
        FilterGrid.Column<Doklad, String> colCisloDokladu = grid.addColumn(Doklad::getCisloDokladu).setCaption("Doklad").setId("doklad");
        FilterGrid.Column<Doklad, String> colTypDokladu = grid.addColumn(doklad -> doklad.getTypDokladu().getDisplayValue()).setCaption("Typ dokladu").setId("typ");
        FilterGrid.Column<Doklad, String> colDatum = grid.addColumn(Doklad::getFormatovanyDatum).setCaption("Dátum").setId("datum");
        FilterGrid.Column<Doklad, String> colFirmaNazov = grid.addColumn(Doklad::getFirmaNazov).setCaption("Firma").setId("nazovFirmy");
        FilterGrid.Column<Doklad, String> colPoznamka = grid.addColumn(Doklad::getPoznamka).setCaption("Poznámka").setId("poznmla");
        FilterGrid.Column<Doklad, String> colStavDokladu = grid.addColumn(doklad -> doklad.getStavDokladu().getIconValue()).setCaption("Stav dokladu").setId("stavDokladu");
        colStavDokladu.setRenderer(new HtmlRenderer());

        ComboBox<String> statusDokladuFilter = new ComboBox<>("", StavDokladu.getListOfDisplayValues());
        statusDokladuFilter.setWidth(120, Unit.PIXELS);

        // filters
        colCisloDokladu.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colDatum.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colPoznamka.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colTypDokladu.setFilter(new ComboBox<>("", TypDokladu.getListOfDisplayValues()),
                (cValue, fValue) -> fValue == null || fValue.equals(cValue));
        colFirmaNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colStavDokladu.setFilter(statusDokladuFilter,
                (cValue, fValue) -> fValue == null || cValue.contains(StavDokladu.fromDisplayName(fValue).getIconColor()));

        grid.setColumnOrder(colCisloDokladu, colStavDokladu,colTypDokladu, colFirmaNazov, colDatum);
        Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);

        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );


        btnPolozky = new Button("Položky", VaadinIcons.BOOK);
        btnPolozky.addClickListener(clickEvent -> {
                    if (grid.getSelectedItems() != null) {
                        polozkyDokladuView = new PolozkyDokladuView((Doklad) grid.getSelectedItems().iterator().next());

                        UI.getCurrent().getNavigator().addView(PolozkyDokladuView.NAME, polozkyDokladuView);
                        UI.getCurrent().getNavigator().navigateTo(PolozkyDokladuView.NAME);
                    }
                }
        );


        HorizontalLayout tlacitkovy = new HorizontalLayout();
        btnNovy = new Button("Novy", VaadinIcons.FILE_O);

        btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        btnNovy.setClickShortcut(ShortcutAction.KeyCode.N,
                new int[]{ShortcutAction.ModifierKey.ALT});


        tlacitkovy.addComponent(btnNovy);

        tlacitkovy.addComponent(btnPolozky);
        tlacitkovy.addComponent(btnSpat);//666


        gl.addComponent(new Label("Prehľad Dokladov"));


        gl.addComponents(grid);
        gl.setComponentAlignment(grid, Alignment.MIDDLE_LEFT);

        gl.addComponent(tlacitkovy);
        gl.setComponentAlignment(tlacitkovy, Alignment.BOTTOM_LEFT);
        gl.setVisible(true);
        grid.setSizeFull();
        this.setSizeFull();
        this.addComponentsAndExpand(gl);


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
        List<Doklad> prvyDokladList = grid.getDataCommunicator().fetchItemsWithRange(0, 1);
        if (prvyDokladList.size() > 0) {
            grid.asSingleSelect().select(prvyDokladList.get(0));
        }
    }

    void selectDoklad(Doklad doklad) {
        Optional.ofNullable(doklad).ifPresent(grid.asSingleSelect()::select);
    }

    public void refresh(Doklad d) {
        grid.getDataProvider().refreshAll();
        grid.select(d);

    }
}






