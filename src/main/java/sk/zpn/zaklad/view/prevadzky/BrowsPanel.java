package sk.zpn.zaklad.view.prevadzky;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.Doklad;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Prevadzka;
import sk.zpn.zaklad.view.VitajteView;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private FilterGrid<Prevadzka> grid;
    private List<Prevadzka> prevadzkaList;


    public Button btnNovy;


        public BrowsPanel(List<Prevadzka> prevadzkaList) {
            this.prevadzkaList = prevadzkaList;
            grid = new FilterGrid<>();
            grid.setItems(this.prevadzkaList);

            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            grid.setWidth(1000, Unit.PIXELS);
            grid.setHeight(800, Unit.PIXELS);
            grid.setHeightByRows(15);

            // definitionn of columns
            FilterGrid.Column<Prevadzka, String> colnazov = grid.addColumn(Prevadzka::getNazov).setCaption("Názov").setId("nazov");
            FilterGrid.Column<Prevadzka, String> colFirmaNazov = grid.addColumn(Prevadzka::getFirmaNazov).setCaption("Firma").setId("nazovFirmy");
            FilterGrid.Column<Prevadzka, String> colUlica = grid.addColumn(Prevadzka::getUlica).setCaption("Ulica").setId("ulica");
            FilterGrid.Column<Prevadzka, String> colMesto = grid.addColumn(Prevadzka::getMesto).setCaption("Mesto").setId("mesto");
            FilterGrid.Column<Prevadzka, String> colPsc = grid.addColumn(Prevadzka::getPsc).setCaption("PSČ").setId("psc");
            FilterGrid.Column<Prevadzka, String> colPoberatelNazov = grid.addColumn(Prevadzka::getPoberatelMeno).setCaption("poberatel").setId("menoPoberatela");



            // filters

            grid.setColumnOrder(colnazov,colFirmaNazov,colPsc,colPoberatelNazov,colUlica, colMesto);

            Button btnSpat=new Button("Späť", VaadinIcons.ARROW_BACKWARD);
            btnSpat.addClickListener(clickEvent ->
                    UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
            );



            HorizontalLayout tlacitkovy=new HorizontalLayout();
            btnNovy=new Button("Novy",VaadinIcons.FILE_O);


            tlacitkovy.addComponent(btnNovy);
            tlacitkovy.addComponent(btnSpat);//666


            this.addComponent(new Label("Prehľad prevadzok"));
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

        void addSelectionListener(Consumer<Prevadzka> listener) {
            grid.asSingleSelect()
                    .addValueChangeListener(e -> listener.accept(e.getValue()));
        }

        void deselect() {
            Prevadzka value = grid.asSingleSelect().getValue();
            if (value != null) {
                grid.getSelectionModel().deselect(value);
            }
        }
    void selectFirst() {
        List<Prevadzka> prvaPrevadzkaList = grid.getDataCommunicator().fetchItemsWithRange(0,1);
        if(prvaPrevadzkaList.size() > 0){
            grid.asSingleSelect().select(prvaPrevadzkaList.get(0));
        }
    }

    void selectPrevadzku(Prevadzka prevadzka) {
        Optional.ofNullable(prevadzka).ifPresent(grid.asSingleSelect()::select);
    }


    }






