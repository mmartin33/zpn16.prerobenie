package sk.zpn.zaklad.view.doklady;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

import com.vaadin.ui.components.grid.ItemClickListener;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
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
    private Firma velkosklad;
    public Button btnNovy;
    protected Button btnZmaz;
    public Button btnPolozky;
    GridLayout gl;
    FilterGrid.Column<Doklad, String> colCisloDokladu ;
    FilterGrid.Column<Doklad, String> colDokladuOdmeny ;
    FilterGrid.Column<Doklad, String> colTypDokladu ;
    FilterGrid.Column<Doklad, String> colDatum ;
    FilterGrid.Column<Doklad, String> colPoberatel ;
    FilterGrid.Column<Doklad, String> colFirmaNazov ;
    FilterGrid.Column<Doklad, String> colPoznamka ;
    FilterGrid.Column<Doklad, String> colStavDokladu ;
    private boolean rezimOdmen;


    public BrowsPanel(List<Doklad> dokladyList, Firma velkosklad) {

        this.velkosklad=velkosklad;
        gl = new GridLayout(1, 3);
        gl.setSizeFull();
        gl.setRowExpandRatio(0, 0.05f);
        gl.setRowExpandRatio(1, 0.90f);

        this.dokladyList = dokladyList;
        grid = new FilterGrid<>();
        grid.setItems(this.dokladyList);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);


        // definitionn of columns
        colCisloDokladu = grid.addColumn(Doklad::getCisloDokladu).setCaption("Doklad").setId("doklad");
        colDokladuOdmeny = grid.addColumn(Doklad::getCisloDokladuOdmeny).setCaption("Číslo dokladu odmeny").setId("dokladOdmeny");
        colTypDokladu = grid.addColumn(doklad -> doklad.getTypDokladu().getDisplayValue()).setCaption("Typ dokladu").setId("typ");
        colDatum = grid.addColumn(Doklad::getFormatovanyDatum).setCaption("Dátum").setId("datum");
        colPoberatel = grid.addColumn(Doklad::getPoberatelMenoAdresa).setCaption("Poberateľ").setId("menoPoberatela");
        colFirmaNazov = grid.addColumn(Doklad::getFirmaNazov).setCaption("Firma").setId("nazovFirmy");
        colPoznamka = grid.addColumn(Doklad::getPoznamka).setCaption("Poznámka").setId("poznamka");
        colStavDokladu = grid.addColumn(doklad -> doklad.getStavDokladu().getDisplayValue()).setCaption("Stav dokladu").setId("stavDokladu");

        ComboBox<String> statusDokladuFilter = new ComboBox<>("", StavDokladu.getListOfDisplayValues());
        statusDokladuFilter.setWidth(120, Unit.PIXELS);

        // filters
        colCisloDokladu.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colDokladuOdmeny.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colDatum.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colPoznamka.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colTypDokladu.setFilter(new ComboBox<>("", TypDokladu.getListOfDisplayValues()),
                (cValue, fValue) -> fValue == null || fValue.equals(cValue));
        colPoberatel.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colFirmaNazov.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colStavDokladu.setFilter(new ComboBox<>("",StavDokladu.getListOfDisplayValues()),
                (cValue, fValue) -> fValue == null || fValue.equals(cValue));


        grid.setColumnOrder(colCisloDokladu, colStavDokladu,colTypDokladu, colFirmaNazov, colDatum,colDokladuOdmeny,colPoberatel);






//        grid.setColumnOrder(colCisloDokladu, colTypDokladu, colFirmaNazov, colDatum);
        Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);

        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );


        btnPolozky = new Button("Položky", VaadinIcons.BOOK);
        btnPolozky.addClickListener(clickEvent -> {
                    if (grid.getSelectedItems() != null) {
                        //polozkyDokladuView = new PolozkyDokladuView((Doklad) grid.getSelectedItems().iterator().next());
                        Doklad otvorenyDoklad=new Doklad();
                        otvorenyDoklad=(Doklad) grid.getSelectedItems().iterator().next();
                        polozkyDokladuView = new PolozkyDokladuView(otvorenyDoklad,velkosklad);
                        if (this.rezimOdmen)
                            polozkyDokladuView.rezimOdmien();
                        else
                            polozkyDokladuView.klasickyRezim();
                        UI.getCurrent().getNavigator().addView(PolozkyDokladuView.NAME, polozkyDokladuView);
                        UI.getCurrent().getNavigator().navigateTo(PolozkyDokladuView.NAME);
                    }
                }
        );


        HorizontalLayout tlacitkovy = new HorizontalLayout();
        btnNovy = new Button("Novy", VaadinIcons.FILE_O);

        btnNovy.setClickShortcut(ShortcutAction.KeyCode.N,
                new int[]{ShortcutAction.ModifierKey.ALT});

        btnZmaz = new Button("Zmaž",  VaadinIcons.CLOSE_CIRCLE);

        btnZmaz.setClickShortcut(ShortcutAction.KeyCode.Z,
                new int[]{ShortcutAction.ModifierKey.ALT});

        btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        if (!jeRezimVelkoskladu()) {
            tlacitkovy.addComponent(btnNovy);
            tlacitkovy.addComponent(btnZmaz);

        }

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
//        if (dokladyList.size()>0)
//            grid.select(dokladyList.get(0));
//        grid.scrollToStart();
        //List items = grid.getDataCommunicator().fetchItemsWithRange(0, Integer.MAX_VALUE);
        if (dokladyList.size()>0) {
            grid.select(dokladyList.get(dokladyList.size() - 1));
            grid.scrollTo(dokladyList.indexOf(dokladyList.get(dokladyList.size() - 1)));
        }

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
        grid.scrollTo(dokladyList.indexOf(doklad));

    }

    public void refresh(Doklad d) {
        grid.getDataProvider().refreshAll();
        grid.select(d);

    }
    public boolean jeRezimVelkoskladu() {
        return this.velkosklad==null?false:true;
    }

    public void rezimOdmien() {
        this.rezimOdmen=true;
        grid.removeColumn("typ");
        grid.removeColumn("doklad");
        grid.removeColumn("stavDokladu");
        grid.setColumnOrder(colDokladuOdmeny, colPoberatel, colFirmaNazov, colDatum);
    }

    public void rezimBodovaci() {
        grid.removeColumn("dokladOdmeny");
        grid.removeColumn("menoPoberatela");
        grid.setColumnOrder(colCisloDokladu, colStavDokladu,colTypDokladu, colFirmaNazov, colDatum);


    }
}






