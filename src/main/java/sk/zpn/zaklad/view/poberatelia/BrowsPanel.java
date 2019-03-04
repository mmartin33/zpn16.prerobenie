package sk.zpn.zaklad.view.poberatelia;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.Poberatel;
import sk.zpn.zaklad.view.VitajteView;
import sk.zpn.zaklad.view.doklady.DokladyView;

import java.util.List;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private FilterGrid<Poberatel> grid;
    private List<Poberatel> poberatelList;


    public Button btnNovy;


        public BrowsPanel(List<Poberatel> poberatelList) {
            this.poberatelList = poberatelList;
            this.setSpacing(false);
            grid = new FilterGrid<>();
            grid.setItems(this.poberatelList);

            grid.addStyleName("test");
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            grid.setWidth(1000, Unit.PIXELS);
            grid.setHeight(700, Unit.PIXELS);

            // definitionn of columns
            FilterGrid.Column<Poberatel, String> colMeno = grid.addColumn(Poberatel::getMeno).setCaption("Meno").setId("meno");
            FilterGrid.Column<Poberatel, String> colPriezvisko = grid.addColumn(Poberatel::getPriezvisko).setCaption("Priezvisko").setId("prizvisko");
            FilterGrid.Column<Poberatel, String> colTitul = grid.addColumn(Poberatel::getTitul).setCaption("Titul").setId("titul");
            FilterGrid.Column<Poberatel, String> colMesto = grid.addColumn(Poberatel::getMesto).setCaption("Mesto").setId("mesto");
            FilterGrid.Column<Poberatel, String> colUlica = grid.addColumn(Poberatel::getUlica).setCaption("Ulica").setId("ulica");
            FilterGrid.Column<Poberatel, String> colPsc = grid.addColumn(Poberatel::getPsc).setCaption("Psč").setId("psc");
            FilterGrid.Column<Poberatel, String> colMobil = grid.addColumn(Poberatel::getMobil).setCaption("Mobil").setId("mobil");
            FilterGrid.Column<Poberatel, String> colTelefon = grid.addColumn(Poberatel::getTelefon).setCaption("Telefon").setId("telefon");
            FilterGrid.Column<Poberatel, String> colEmail = grid.addColumn(Poberatel::getEmail).setCaption("Email").setId("email");
            FilterGrid.Column<Poberatel, String> colKod = grid.addColumn(Poberatel::getKod).setCaption("Kód").setId("kod");

            // filters
            colMeno.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colPriezvisko.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colTitul.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colMesto.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colUlica.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colPsc.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colMobil.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colTelefon.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colEmail.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colKod.setFilter(new TextField(), StringComparator.containsIgnoreCase());
//
            grid.setColumnOrder(colMeno,colPriezvisko);



            Button btnSpat=new Button("Späť", VaadinIcons.ARROW_BACKWARD);
            btnSpat.addClickListener(clickEvent ->
                    UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)//UI.getCurrent().getNavigator().navigateTo(DokladyView.NAME)
            );



            HorizontalLayout tlacitkovy=new HorizontalLayout();
            btnNovy=new Button("Novy",VaadinIcons.FILE_O);


            tlacitkovy.addComponent(btnNovy);
            tlacitkovy.addComponent(btnSpat);//666


            this.addComponent(new Label("Prehľad poberateľov"));
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

        void addSelectionListener(Consumer<Poberatel> listener) {
            grid.asSingleSelect()
                    .addValueChangeListener(e -> listener.accept(e.getValue()));
        }

        void deselect() {
            Poberatel value = grid.asSingleSelect().getValue();
            if (value != null) {
                grid.getSelectionModel().deselect(value);
            }
        }

    }






