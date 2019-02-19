package sk.zpn.zaklad.view.mostik;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.Mostik;
import sk.zpn.domena.Produkt;
import sk.zpn.zaklad.view.VitajteView;

import java.util.List;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private FilterGrid<Mostik> grid;
    private List<Mostik> mostikList;


    public Button btnNovy;


        public BrowsPanel(List<Mostik> mostikList) {
            this.mostikList = mostikList;
            this.setSpacing(false);
            grid = new FilterGrid<>();
            grid.setItems(this.mostikList);

            grid.addStyleName("test");
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            grid.setWidth(1000, Unit.PIXELS);
            grid.setHeight(700, Unit.PIXELS);

            // definitionn of columns
            FilterGrid.Column<Mostik, String > colKit = grid.addColumn(Mostik::getKit).setCaption("Kit").setId("kit");
            FilterGrid.Column<Mostik, String> colProduktKat = grid.addColumn(Mostik::getProduktKat).setCaption("kat").setId("kat");
//            FilterGrid.Column<Produkt, String> colKat = grid.addColumn(Produkt::getKat).setCaption("KAT").setId("kat");
//            FilterGrid.Column<Produkt, String> colNazov = grid.addColumn(Produkt::getNazov).setCaption("Názov").setId("nazov");
//            FilterGrid.Column<Produkt, Double> colKusy = grid.addColumn(Produkt::getKusy).setCaption("kusy").setId("kusy");
//            FilterGrid.Column<Produkt, Double> colBody = grid.addColumn(Produkt::getBody).setCaption("Body").setId("body");
//            FilterGrid.Column<Produkt, String> colFirmaNazov = grid.addColumn(Produkt::getFirmaNazov).setCaption("Firma").setId("nazovFirmy");

            // filters
               colKit.setFilter(new TextField(), StringComparator.containsIgnoreCase());
               colProduktKat.setFilter(new TextField(), StringComparator.containsIgnoreCase());
//            colKat.setFilter(new TextField(), StringComparator.containsIgnoreCase());
//            colNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());
//            colKusy.setFilter(new TextField(), StringComparator.containsIgnoreCase());
//            colBody.setFilter(new TextField(), StringComparator.containsIgnoreCase());
//            colFirmaNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());

//
//            grid.setColumnOrder(colKat,colNazov,colKusy,colBody,colFirmaNazov);



            Button btnSpat=new Button("Späť", VaadinIcons.ARROW_BACKWARD);
            btnSpat.addClickListener(clickEvent ->
                    {

                    UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME);}
            );



            HorizontalLayout tlacitkovy=new HorizontalLayout();
//            btnNovy=new Button("Novy",VaadinIcons.FILE_O);


//            tlacitkovy.addComponent(btnNovy);
            tlacitkovy.addComponent(btnSpat);//666


            this.addComponent(new Label("Mostik KAT KIT"));
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

        void addSelectionListener(Consumer<Mostik> listener) {
            grid.asSingleSelect()
                    .addValueChangeListener(e -> listener.accept(e.getValue()));
        }

        void deselect() {
            Mostik value = grid.asSingleSelect().getValue();
            if (value != null) {
                grid.getSelectionModel().deselect(value);
            }
        }

    }






