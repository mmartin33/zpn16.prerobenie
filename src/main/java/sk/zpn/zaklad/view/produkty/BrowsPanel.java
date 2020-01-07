package sk.zpn.zaklad.view.produkty;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.Produkt;
import sk.zpn.domena.TypProduktov;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.view.VitajteView;
import sk.zpn.zaklad.view.firmy.FirmyView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private FilterGrid<Produkt> grid;
    public List<Produkt> produktList;

    public HorizontalLayout hornyFilter;
    private FormLayout hFFormLayout;
    private TextField tfHfRok;
    private Button btnPrezobraz;

    public Button btnNovy;
    public Button btnFirmy;
    private FirmyView firmyView;
    private ProduktyView produktView;
    FilterGrid.Column<Produkt, BigDecimal> colCena;
    private final Binder<String> binder = new Binder<>();

    public BrowsPanel(List<Produkt> produktList) {
        hornyFilter = new HorizontalLayout();
        hFFormLayout= new FormLayout();
        hornyFilter.setHeight("60");
        tfHfRok = new TextField("Rok");

        tfHfRok.setWidth("400");
        tfHfRok.setValue(ParametreNastroje.nacitajParametre().getRok());


//        Binder.Binding<String, String> rokBinding = (Binder.Binding<String, String>) binder.forField(tfHfRok)
//                .withConverter(new StringToBigDecimalConverter("Nie je číslo"));


        btnPrezobraz = new Button("Prezobraz");

        GridLayout gl = new GridLayout(1, 4);
        gl.setSizeFull();
        gl.setRowExpandRatio(0, 0.05f);
        gl.setRowExpandRatio(1, 0.05f);
        gl.setRowExpandRatio(2, 0.85f);
        gl.setRowExpandRatio(3, 0.05f);

        this.produktList = produktList;
        this.setSpacing(false);

        grid = new FilterGrid<>();
        grid.setItems(this.produktList);

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setSizeFull();


        // definitionn of columns

        FilterGrid.Column<Produkt, String> colRok = grid.addColumn(Produkt::getRok).setCaption("Rok").setId("rok");
        FilterGrid.Column<Produkt, String> colKat = grid.addColumn(Produkt::getKat)
                .setCaption("KAT")
                .setId("kat")
                .setDescriptionGenerator(Produkt::getToolTip);
        FilterGrid.Column<Produkt, String> colNazov = grid.addColumn(Produkt::getNazov).setCaption("Názov").setId("nazov");
        FilterGrid.Column<Produkt, BigInteger> colKusy = grid.addColumn(Produkt::getKusyBigInteger).setCaption("kusy").setId("kusy");
        FilterGrid.Column<Produkt, BigInteger> colBody = grid.addColumn(Produkt::getBodyBigInteger).setCaption("Body").setId("body");
        FilterGrid.Column<Produkt, String> colFirmaNazov = grid.addColumn(Produkt::getFirmaNazov).setCaption("Firma").setId("nazovFirmy");
        colCena = grid.addColumn(Produkt::getCena).setCaption("Cena").setId("cena");

        // filters
        colRok.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colKat.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colNazov.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colKusy.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colBody.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colFirmaNazov.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());

//
        grid.setColumnOrder(colKat, colNazov, colKusy, colBody);


        Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnSpat.addClickListener(clickEvent ->
                {
                    if (this.produktView.getRodicovskyView() != null)
                        UI.getCurrent().getNavigator().navigateTo(this.produktView.getRodicovskyView());
                    else
                        UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME);
                }
        );

        btnFirmy = new Button("Firmy", VaadinIcons.BUILDING);

        HorizontalLayout tlacitkovy = new HorizontalLayout();
        btnNovy = new Button("Novy", VaadinIcons.FILE_O);

        btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        btnNovy.setClickShortcut(ShortcutAction.KeyCode.N,
                new int[]{ShortcutAction.ModifierKey.ALT});

        tlacitkovy.addComponent(btnNovy);
        tlacitkovy.addComponent(btnFirmy);
        tlacitkovy.addComponent(btnSpat);//666

        Label l = new Label("Prehľad produktov");
        gl.addComponent(l);
        hFFormLayout.addComponent(tfHfRok);
        hornyFilter.addComponent(hFFormLayout);
        hornyFilter.addComponent(btnPrezobraz);
        hornyFilter.setComponentAlignment(hFFormLayout, Alignment.BOTTOM_LEFT);
        hornyFilter.setComponentAlignment(btnPrezobraz, Alignment.BOTTOM_RIGHT);
        btnPrezobraz.addClickListener(this::aktivujHF);
        gl.addComponent(hornyFilter);
        gl.setComponentAlignment(l, Alignment.TOP_LEFT);

        gl.addComponents(grid);
        gl.setComponentAlignment(grid, Alignment.MIDDLE_LEFT);

        gl.addComponent(tlacitkovy);
        gl.setComponentAlignment(tlacitkovy, Alignment.BOTTOM_LEFT);
        gl.setVisible(true);
        this.addComponentsAndExpand(gl);


    }

    private void aktivujHF(Button.ClickEvent clickEvent) {
        produktList.clear();
        if (tfHfRok.getValue().length()>0)
            produktView.naplnList(tfHfRok.getValue());
        grid.setItems(produktView.produktList);
        grid.getDataProvider().refreshAll();

    }


    void refresh() {

        grid.getDataProvider().refreshAll();
        System.out.println("Refresh browsu all");

    }

    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (produktView.getTypProduktov() == TypProduktov.BODOVACI)
            grid.removeColumn("cena");
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

    public void setProduktyView(ProduktyView produktyView) {
        this.produktView = produktyView;
    }
}






