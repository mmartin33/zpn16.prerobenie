package sk.zpn.zaklad.view.doklady;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.*;
import sk.zpn.zaklad.grafickeNastroje.MFilteredGrid;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.view.VitajteView;
import sk.zpn.zaklad.view.doklady.polozkaDokladu.PolozkyDokladuView;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    public MFilteredGrid<Doklad> grid;
    public List<Doklad> dokladyList;


    PolozkyDokladuView polozkyDokladuView;
    private Firma velkosklad;
    public Button btnNovy;
    public Button btnTest;
    protected Button btnZmaz;
    public Button btnPolozky;
    GridLayout gl;
    HorizontalLayout hornyFilter;
    FormLayout hFFormLayout;
    TextField tfHfRok;
    Button btnPrezobraz;
    MFilteredGrid.Column<Doklad, String> colCisloDokladu ;
    MFilteredGrid.Column<Doklad, String> colDokladuOdmeny ;
    MFilteredGrid.Column<Doklad, String> colTypDokladu ;
    MFilteredGrid.Column<Doklad, String> colDatum ;
    MFilteredGrid.Column<Doklad, String> colPoberatel ;
    MFilteredGrid.Column<Doklad, String> colFirmaNazov ;
    MFilteredGrid.Column<Doklad, String> colPoznamka ;
    MFilteredGrid.Column<Doklad, String> colStavDokladu ;
    private boolean rezimOdmen;
    private DokladyView dokladyView;


    public BrowsPanel(List<Doklad> dokladyList, Firma velkosklad) {
        //polozkyDokladuView = new PolozkyDokladuView(null,velkosklad);


        hornyFilter = new HorizontalLayout();
        hFFormLayout = new FormLayout();
        hornyFilter.setHeight("60");
        tfHfRok = new TextField("Rok");

        tfHfRok.setWidth("400");
        tfHfRok.setValue(ParametreNastroje.nacitajParametre().getRok());

        btnPrezobraz = new Button("Prezobraz");

        this.velkosklad=velkosklad;
        gl = new GridLayout(1, 4);
        gl.setSizeFull();
        gl.setRowExpandRatio(0, 0.02f);
        gl.setRowExpandRatio(1, 0.05f);
        gl.setRowExpandRatio(2, 0.85f);
        gl.setRowExpandRatio(3, 0.05f);

        this.dokladyList = dokladyList;
        grid = new MFilteredGrid<>();
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


        //grid.setColumnOrder(colCisloDokladu, colStavDokladu,colTypDokladu, colFirmaNazov, colDatum,colDokladuOdmeny,colPoberatel);
        grid.registrujZmenuStlpcov("doklady");





//        grid.setColumnOrder(colCisloDokladu, colTypDokladu, colFirmaNazov, colDatum);
        Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);

        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );
        btnTest = new Button("Test", VaadinIcons.ARROW_BACKWARD);




        btnPolozky = new Button("Položky", VaadinIcons.BOOK);
        btnPolozky.addClickListener(clickEvent -> {
                    if (grid.getSelectedItems() != null) {

                        Doklad otvorenyDoklad=null;
                        //Doklad otvorenyDoklad=new Doklad();
                        otvorenyDoklad=(Doklad) grid.getSelectedItems().iterator().next();
                        polozkyDokladuView = new PolozkyDokladuView(otvorenyDoklad,velkosklad);

                        if (this.rezimOdmen)
                            polozkyDokladuView.setRezimOdmien();
                        else
                        polozkyDokladuView.setKlasickyRezim();

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
        //tlacitkovy.addComponent(btnTest);//666

        hFFormLayout.addComponent(tfHfRok);
        hornyFilter.addComponent(hFFormLayout);
        hornyFilter.setComponentAlignment(hFFormLayout,Alignment.TOP_LEFT);
        hornyFilter.addComponent(btnPrezobraz);
        hornyFilter.setComponentAlignment(btnPrezobraz,Alignment.TOP_LEFT);
        hornyFilter.setComponentAlignment(hFFormLayout, Alignment.BOTTOM_LEFT);
        hornyFilter.setComponentAlignment(btnPrezobraz, Alignment.BOTTOM_RIGHT);
        btnPrezobraz.addClickListener(this::aktivujHF);


        HorizontalLayout nadpisLayout = new HorizontalLayout();


        nadpisLayout.addComponent(new Label("Prehľad Dokladov"));
        nadpisLayout.setHeight("2");
        gl.addComponent(nadpisLayout);
        gl.setComponentAlignment(nadpisLayout, Alignment.TOP_LEFT);
        gl.addComponent(hornyFilter);

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

    private void aktivujHF(Button.ClickEvent clickEvent) {
        dokladyList.clear();
        dokladyView.naplnList(tfHfRok.getValue());
        grid.setItems(dokladyView.dokladyList);
        grid.getDataProvider().refreshAll();

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
        int value =dokladyList.indexOf(doklad);

      //  if (value<=grid.setId("aaa");)
        grid.select(doklad);
            //grid.scrollTo(dokladyList.indexOf(doklad));

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

    public void nadstavNaOznaceny() {
        Doklad value = grid.asSingleSelect().getValue();
        int index = dokladyList.indexOf(value);

        grid.scrollTo(index);
    }

    public void test(Button.ClickEvent clickEvent) {
        Doklad value = grid.asSingleSelect().getValue();

        //grid.scrollTo(new Integer(grid.getId()).intValue());
        int index = dokladyList.indexOf(value);
        Integer aa = new Integer((Integer) grid.getDataProvider().getId(value));

        grid.scrollTo(index);
        //grid.select(value);

    }
    public void setProduktyView(DokladyView dokladyView) {
        this.dokladyView = dokladyView;
    }

}






