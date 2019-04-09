package sk.zpn.zaklad.view.produkty;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.Produkt;
import sk.zpn.zaklad.view.VitajteView;
import sk.zpn.zaklad.view.firmy.FirmyView;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private FilterGrid<Produkt> grid;
    private List<Produkt> produktList;


    public Button btnNovy;
    public Button btnFirmy;
    private FirmyView firmyView;


        public BrowsPanel(List<Produkt> produktList) {
            GridLayout gl =new GridLayout(1,3);
            gl.setSizeFull();
            gl.setRowExpandRatio(0, 0.05f);
            gl.setRowExpandRatio(1, 0.90f);
            gl.setRowExpandRatio(2, 0.05f);

            this.produktList = produktList;
            this.setSpacing(false);

            grid = new FilterGrid<>();
            grid.setItems(this.produktList);

            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            grid.setSizeFull();


            // definitionn of columns

            FilterGrid.Column<Produkt, String > colRok = grid.addColumn(Produkt::getRok).setCaption("Rok").setId("rok");
            FilterGrid.Column<Produkt, String> colKat = grid.addColumn(Produkt::getKat)
                    .setCaption("KAT")
                    .setId("kat")
                    .setDescriptionGenerator(Produkt::getToolTip);
            FilterGrid.Column<Produkt, String> colNazov = grid.addColumn(Produkt::getNazov).setCaption("Názov").setId("nazov");
            FilterGrid.Column<Produkt, BigDecimal> colKusy = grid.addColumn(Produkt::getKusy).setCaption("kusy").setId("kusy");
            FilterGrid.Column<Produkt, BigDecimal> colBody = grid.addColumn(Produkt::getBody).setCaption("Body").setId("body");
            FilterGrid.Column<Produkt, String> colFirmaNazov = grid.addColumn(Produkt::getFirmaNazov).setCaption("Firma").setId("nazovFirmy");

            // filters
            colRok.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colKat.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colKusy.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colBody.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colFirmaNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());

//
            grid.setColumnOrder(colKat,colNazov,colKusy,colBody);



            Button btnSpat=new Button("Späť", VaadinIcons.ARROW_BACKWARD);
            btnSpat.addClickListener(clickEvent ->
                    {

                    UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME);}
            );

            btnFirmy =new Button("Firmy", VaadinIcons.BUILDING);

            HorizontalLayout tlacitkovy=new HorizontalLayout();
            btnNovy=new Button("Novy",VaadinIcons.FILE_O);

            btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
            btnNovy.setClickShortcut(ShortcutAction.KeyCode.N,
                    new int[] { ShortcutAction.ModifierKey.ALT });

            tlacitkovy.addComponent(btnNovy);
            tlacitkovy.addComponent(btnFirmy);
            tlacitkovy.addComponent(btnSpat);//666

            Label l=new Label("Prehľad produktov");
            gl.addComponent(l);
            gl.setComponentAlignment(l,Alignment.TOP_LEFT);

            gl.addComponents(grid);
            gl.setComponentAlignment(grid,Alignment.MIDDLE_LEFT);

            gl.addComponent(tlacitkovy);
            gl.setComponentAlignment(tlacitkovy,Alignment.BOTTOM_LEFT);
            gl.setVisible(true);
            this.addComponentsAndExpand(gl);



        }


        void refresh() {

            grid.getDataProvider().refreshAll();
            System.out.println("Refresh browsu all");

        }

    void addEditListener(Runnable editListener) {
        btnNovy.addClickListener(e -> editListener.run());

    }

        void addSelectionListener(Consumer<Produkt> listener) {
            grid.asSingleSelect()
                    .addValueChangeListener(e -> listener.accept(e.getValue()));
        }

        void deselect() {
            Produkt value = grid.asSingleSelect().getValue();
            if (value != null) {
                grid.getSelectionModel().deselect(value);
            }
        }

    public void refresh(Produkt p) {
        grid.getDataProvider().refreshAll();
        grid.select(p);

    }
}






