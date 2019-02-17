package sk.zpn.zaklad.view.uzivatel;

import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.StatusUzivatela;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.view.VitajteView;

import java.util.List;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private FilterGrid<Uzivatel> grid;
    private List<Uzivatel> uzivatelList;


    public Button btnNovy;


        public BrowsPanel(List<Uzivatel> uzivatelList) {
            this.uzivatelList = uzivatelList;
            grid = new FilterGrid<>();
            grid.setItems(this.uzivatelList);
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            grid.setWidth(850, Unit.PIXELS);

            // definitionn of columns
            FilterGrid.Column<Uzivatel, String> colMeno = grid.addColumn(Uzivatel::getMeno).setCaption("Meno").setId("meno");
            FilterGrid.Column<Uzivatel, String> colTypUzivatela = grid.addColumn(uzivatel -> uzivatel.getTypUzivatela().getDisplayValue()).setCaption("Typ konta").setId("typ");
            FilterGrid.Column<Uzivatel, String> colFirmaNazov = grid.addColumn(Uzivatel::getFirmaNazov).setCaption("Firma").setId("nazovFirmy");
            FilterGrid.Column<Uzivatel, String> colStatusUzivatela = grid.addColumn(uzivatel ->
                    uzivatel.getStatusUzivatela().getIconValue()).setCaption("Stav konta").setId("statusUzivatela");
            colStatusUzivatela.setRenderer(new HtmlRenderer());
            colStatusUzivatela.setWidth(150);

            // filters
            colMeno.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colTypUzivatela.setFilter(new ComboBox<>("", TypUzivatela.getListOfDisplayValues()),
                    (cValue, fValue) -> fValue == null || fValue.equals(cValue));
            colFirmaNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());

            ComboBox<String> statusUzivatelaFilter = new ComboBox<>("", StatusUzivatela.getListOfDisplayValues());
            statusUzivatelaFilter.setWidth(120, Unit.PIXELS);

            colStatusUzivatela.setFilter(statusUzivatelaFilter,
            (cValue, fValue) -> fValue == null || cValue.contains(StatusUzivatela.fromDisplayName(fValue).getIconColor()));

            grid.setColumnOrder(colMeno, colTypUzivatela,colFirmaNazov, colStatusUzivatela);
            this.addComponent(new Label("Prehľad užívateľov"));
            HorizontalLayout prvy=new HorizontalLayout();

            Button btnSpat=new Button("Späť");


            btnSpat.addClickListener(clickEvent ->
                    UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
            );
            prvy.addComponent(btnSpat);

            this.addComponent(prvy);
            VerticalLayout druhy=new VerticalLayout();
            btnNovy=new Button("Novy");

            druhy.addComponentsAndExpand(grid);
            druhy.addComponent(btnNovy);
            this.addComponent(druhy);



        }


        void refresh() {
            grid.getDataProvider().refreshAll();
            System.out.println("Refresh browsu all");

        }

    void addEditListener(Runnable editListener) {
        btnNovy.addClickListener(e -> editListener.run());

    }

        void addSelectionListener(Consumer<Uzivatel> listener) {
            grid.asSingleSelect()
                    .addValueChangeListener(e -> listener.accept(e.getValue()));
        }

        void deselect() {
            Uzivatel value = grid.asSingleSelect().getValue();
            if (value != null) {
                grid.getSelectionModel().deselect(value);
            }
        }

    }






