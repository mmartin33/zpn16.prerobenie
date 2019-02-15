package sk.zpn.zaklad.view.uzivatel;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
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
            this.setSpacing(false);
            grid = new FilterGrid<>();
            grid.setItems(this.uzivatelList);
            grid.addStyleName("test");
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            grid.setWidth(1000, Unit.PIXELS);
            grid.setHeight(700, Unit.PIXELS);

            // definitionn of columns
            FilterGrid.Column<Uzivatel, String> colMeno = grid.addColumn(Uzivatel::getMeno).setCaption("Meno").setId("meno");
            FilterGrid.Column<Uzivatel, String> colTypUzivatela = grid.addColumn(uzivatel -> uzivatel.getTypUzivatela().getDisplayValue()).setCaption("Typ konta").setId("typ");
            FilterGrid.Column<Uzivatel, String> colFirmaNazov = grid.addColumn(Uzivatel::getFirmaNazov).setCaption("Firma").setId("nazovFirmy");

            // filters
            colMeno.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colTypUzivatela.setFilter(new ComboBox<>("", TypUzivatela.getListOfDisplayValues()),
                    (cValue, fValue) -> fValue == null || fValue.equals(cValue));
            colFirmaNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());

            grid.setColumnOrder(colMeno, colTypUzivatela,colFirmaNazov);



            Button btnSpat=new Button("Späť", VaadinIcons.ARROW_BACKWARD);
            btnSpat.addClickListener(clickEvent ->
                    UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
            );



            HorizontalLayout tlacitkovy=new HorizontalLayout();
            btnNovy=new Button("Novy",VaadinIcons.FILE_O);


            tlacitkovy.addComponent(btnNovy);
            tlacitkovy.addComponent(btnSpat);//666


            this.addComponent(new Label("Prehľad užívateľov"));
            this.addComponents(grid);


            this.addComponent(tlacitkovy);



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






