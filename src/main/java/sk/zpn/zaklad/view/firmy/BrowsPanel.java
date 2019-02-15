package sk.zpn.zaklad.view.firmy;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.Firma;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.view.VitajteView;

import java.util.List;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private FilterGrid<Firma> grid;
    private List<Firma> firmaList;


    public Button btnNovy;


        public BrowsPanel(List<Firma> firmaList) {
            this.firmaList = firmaList;
            grid = new FilterGrid<>();
            grid.setItems(this.firmaList);

            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            grid.setWidth(1000, Unit.PIXELS);
            grid.setHeight(800, Unit.PIXELS);
            grid.setHeightByRows(15);

            // definitionn of columns
            FilterGrid.Column<Firma, String> colIco = grid.addColumn(Firma::getIco).setCaption("IČO").setId("ico");
            FilterGrid.Column<Firma, String> colNazov = grid.addColumn(Firma::getNazov).setCaption("Názov").setId("nazov");
            FilterGrid.Column<Firma, String> colIcDPH = grid.addColumn(Firma::getIc_dph).setCaption("IČ DPH").setId("icdph");
            FilterGrid.Column<Firma, String> colDic = grid.addColumn(Firma::getDic).setCaption("DIČ").setId("dic");
            FilterGrid.Column<Firma, String> colUlica = grid.addColumn(Firma::getUlica).setCaption("Ulica").setId("ulica");
            FilterGrid.Column<Firma, String> colMesto = grid.addColumn(Firma::getMesto).setCaption("Mesto").setId("mesto");
            FilterGrid.Column<Firma, String> colPsc = grid.addColumn(Firma::getPsc).setCaption("PSČ").setId("psc");
            FilterGrid.Column<Firma, String> colTelefon = grid.addColumn(Firma::getTelefon).setCaption("Telefón").setId("telefon");


            // filters
            colIco.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colIcDPH.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colDic.setFilter(new TextField(), StringComparator.containsIgnoreCase());

            grid.setColumnOrder(colIco, colNazov,colIcDPH,colDic);

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

        void addSelectionListener(Consumer<Firma> listener) {
            grid.asSingleSelect()
                    .addValueChangeListener(e -> listener.accept(e.getValue()));
        }

        void deselect() {
            Firma value = grid.asSingleSelect().getValue();
            if (value != null) {
                grid.getSelectionModel().deselect(value);
            }
        }

    }






