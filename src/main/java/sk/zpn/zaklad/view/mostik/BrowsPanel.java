package sk.zpn.zaklad.view.mostik;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.FirmaProdukt;
import sk.zpn.zaklad.view.VitajteView;

import java.util.List;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private FilterGrid<FirmaProdukt> grid;
    private List<FirmaProdukt> firmaProduktList;


    public Button btnNovy;


        public BrowsPanel(List<FirmaProdukt> firmaProduktList) {
            this.firmaProduktList = firmaProduktList;
            this.setSpacing(false);
            grid = new FilterGrid<>();
            grid.setItems(this.firmaProduktList);

            grid.addStyleName("test");
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            grid.setWidth(1000, Unit.PIXELS);
            grid.setHeight(700, Unit.PIXELS);

            // definitionn of columns
            FilterGrid.Column<FirmaProdukt, String > colKit = grid.addColumn(FirmaProdukt::getKit).setCaption("Kit").setId("kit");
            FilterGrid.Column<FirmaProdukt, String> colProduktKat = grid.addColumn(firmaProdukt -> firmaProdukt.getProdukt().getKat()).setCaption("kat").setId("kat");
            FilterGrid.Column<FirmaProdukt, String> colNazov = grid.addColumn(firmaProdukt -> firmaProdukt.getProdukt().getNazov()).setCaption("Názov").setId("nazov");
            FilterGrid.Column<FirmaProdukt, Double> colKusy = grid.addColumn(firmaProdukt -> firmaProdukt.getProdukt().getKusy()).setCaption("kusy").setId("kusy");
            FilterGrid.Column<FirmaProdukt, Double> colBody = grid.addColumn(firmaProdukt -> firmaProdukt.getProdukt().getBody()).setCaption("Body").setId("body");

            // filters
           colKit.setFilter(new TextField(), StringComparator.containsIgnoreCase());
           colProduktKat.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colKusy.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colBody.setFilter(new TextField(), StringComparator.containsIgnoreCase());



            Button btnSpat=new Button("Späť", VaadinIcons.ARROW_BACKWARD);
            btnSpat.addClickListener(clickEvent ->
                    {

                    UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME);}
            );



            HorizontalLayout tlacitkovy=new HorizontalLayout();
//            btnNovy=new Button("Novy",VaadinIcons.FILE_O);


//            tlacitkovy.addComponent(btnNovy);
            tlacitkovy.addComponent(btnSpat);//666


            this.addComponent(new Label("FirmaProdukt KAT KIT"));
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

        void addSelectionListener(Consumer<FirmaProdukt> listener) {
            grid.asSingleSelect()
                    .addValueChangeListener(e -> listener.accept(e.getValue()));
        }

        void deselect() {
            FirmaProdukt value = grid.asSingleSelect().getValue();
            if (value != null) {
                grid.getSelectionModel().deselect(value);
            }
        }

    }






