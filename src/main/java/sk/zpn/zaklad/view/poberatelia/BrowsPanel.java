package sk.zpn.zaklad.view.poberatelia;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.ItemClickListener;
import org.jsoup.helper.StringUtil;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import com.vaadin.ui.themes.ValoTheme;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.Prevadzka;
import sk.zpn.zaklad.grafickeNastroje.MFilteredGrid;
import sk.zpn.zaklad.grafickeNastroje.WindowWithEditBox;
import sk.zpn.zaklad.model.FirmaNastroje;
import sk.zpn.zaklad.model.PoberatelNastroje;
import sk.zpn.zaklad.model.PrevadzkaNastroje;
import sk.zpn.zaklad.view.VitajteView;
import sk.zpn.zaklad.view.doklady.DokladyView;
import sk.zpn.zaklad.view.doklady.polozkaDokladu.PolozkyDokladuView;
import sk.zpn.zaklad.view.prevadzky.PrevadzkyView;
import sk.zpn.zaklad.view.statistiky.StatPohybyBodovPoberatelovView;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BrowsPanel extends VerticalLayout {


    private final PoberateliaView poberateliaView;
    private HorizontalLayout hornyFilter;
    private TextField tfPrevadzkaZakaznika;
    private CheckBox chkAktivne;
    private Button btnPrezobraz;
    private Button btnPremenujPoberatelaPrevadzkyFirmu;

    private Button btnEditujPoznámku;
    private MFilteredGrid<Poberatel> grid;
    private List<Poberatel> poberatelList;
    private final Binder<Prevadzka> binder = new Binder<>();


    public Button btnNovy;
    public Button btnVyber;
    public Button btnRegistruj;
    private Prevadzka prevadzkaHF;


    public BrowsPanel(List<Poberatel> poberatelList, PoberateliaView poberateliaView) {
        hornyFilter = new HorizontalLayout();
        tfPrevadzkaZakaznika = new TextField("Názov prevádzky");
        tfPrevadzkaZakaznika.setWidth("400");
        btnPrezobraz = new Button("Prezobraz");
        chkAktivne=new CheckBox("Aktivny poberatelia");
        chkAktivne.setValue(true);

        GridLayout gl = new GridLayout(1, 5);
        gl.setSizeFull();
        gl.setRowExpandRatio(0, 0.02f);
        gl.setRowExpandRatio(1, 0.02f);
        gl.setRowExpandRatio(2, 0.90f);
        gl.setRowExpandRatio(3, 0.02f);
        gl.setRowExpandRatio(4, 0.02f);

        this.poberateliaView = poberateliaView;
        this.poberatelList = poberatelList;
        this.setSpacing(false);
        grid = new MFilteredGrid<>();
        grid.setItems(this.poberatelList);

        grid.addStyleName("test");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        // definitionn of columns
        MFilteredGrid.Column<Poberatel, String> colMeno = grid.addColumn(Poberatel::getMeno).setCaption("Meno").setId("meno");
//            MFilteredGrid.Column<Poberatel, String> colPriezvisko = grid.addColumn(Poberatel::getPriezvisko).setCaption("Priezvisko").setId("prizvisko");
        MFilteredGrid.Column<Poberatel, String> colTitul = grid.addColumn(Poberatel::getTitul).setCaption("Titul").setId("titul");
        MFilteredGrid.Column<Poberatel, String> colMesto = grid.addColumn(Poberatel::getMesto).setCaption("Mesto").setId("mesto");
        MFilteredGrid.Column<Poberatel, String> colUlica = grid.addColumn(Poberatel::getUlica).setCaption("Ulica").setId("ulica");
        MFilteredGrid.Column<Poberatel, String> colPsc = grid.addColumn(Poberatel::getPsc).setCaption("Psč").setId("psc");
        MFilteredGrid.Column<Poberatel, String> colMobil = grid.addColumn(Poberatel::getMobil).setCaption("Mobil").setId("mobil");
        MFilteredGrid.Column<Poberatel, String> colTelefon = grid.addColumn(Poberatel::getTelefon).setCaption("Telefon").setId("telefon");
        MFilteredGrid.Column<Poberatel, String> colEmail = grid.addColumn(Poberatel::getEmail).setCaption("Email").setId("email");
        MFilteredGrid.Column<Poberatel, String> colKod = grid.addColumn(Poberatel::getKod).setCaption("Kód").setId("kod");
        MFilteredGrid.Column<Poberatel, String> colPoznamkaVelkoskladu = grid.addColumn(Poberatel::getPoznamkaVelkoskladu).setCaption("Poznámka").setId("poznamka");
        MFilteredGrid.Column<Poberatel, BigDecimal> colPociatocnyStav = grid.addColumn(Poberatel::getPociatocnyStav).setCaption("PS bodov").setId("ps_body");
        grid.registrujZmenuStlpcov("poberatelia");
        // filters
        colMeno.setFilter(new TextField(), StringComparator.containsIgnoreCase());
//            colPriezvisko.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colTitul.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colMesto.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colUlica.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colPsc.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colMobil.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colTelefon.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colEmail.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colPoznamkaVelkoskladu.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colKod.setFilter(new TextField(), StringComparator.containsIgnoreCase());
//
//            grid.setColumnOrder(colMeno,colPriezvisko);


        Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnSpat.addClickListener(clickEvent -> {

            if (this.poberateliaView.getRodicovskyView() != null)
                UI.getCurrent().getNavigator().navigateTo(this.poberateliaView.getRodicovskyView());
            else if (poberateliaView.getPrevadzka() != null)
                UI.getCurrent().getNavigator().navigateTo(PrevadzkyView.NAME);
            else
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME);
        });

        Button btnDetail = new Button("Detail o poberatelovy", VaadinIcons.INFO_CIRCLE);
        btnDetail.addClickListener(clickEvent -> {
            if (poberateliaView.getEditacnyForm().getPoberatelEditovany() != null) {
                StatPohybyBodovPoberatelovView sv = new StatPohybyBodovPoberatelovView();
                sv.setRodicovskyView(poberateliaView.NAME);
                sv.setPoberatel(poberateliaView.getEditacnyForm().getPoberatelEditovany());

                UI.getCurrent().getNavigator().addView(sv.NAME, sv);
                UI.getCurrent().getNavigator().navigateTo(sv.NAME);
                ;
            }
        });

        btnEditujPoznámku = new Button("Uprav poznámku", VaadinIcons.NOTEBOOK);
        btnEditujPoznámku.addClickListener(clickEvent -> {
            WindowWithEditBox w;
            w = new WindowWithEditBox("Poznámka", ((Poberatel) grid.asSingleSelect().getValue()).getPoznamkaVelkoskladu());
//            w.addCloseListener(event -> windowClose(w));
            w.addCloseListener(new Window.CloseListener() {

                @Override
                public void windowClose(Window.CloseEvent e) {
                    vykonajPoZatvoreni(w);
                }
            });


        });
        btnPremenujPoberatelaPrevadzkyFirmu = new Button("Premenuj poberateľa-prevaddzku-firmu", VaadinIcons.ALARM);
        btnPremenujPoberatelaPrevadzkyFirmu.addClickListener(clickEvent -> {
            WindowWithEditBox w;
            w = new WindowWithEditBox("Názov poberateľa- prevádzky-firmu", ((Poberatel) grid.asSingleSelect().getValue()).getMeno());
            w.addCloseListener(new Window.CloseListener() {

                @Override
                public void windowClose(Window.CloseEvent e) {
                    premenujPoberatelaPrevadzkuFirmu(w);
                }
            });


        });

            grid.addItemClickListener(new ItemClickListener<Poberatel>() {
            @Override

            public void itemClick(Grid.ItemClick<Poberatel> event) {
                if (event.getMouseEventDetails().isDoubleClick())
                    vybratyPoberatel(event.getItem());
            }

        });


        HorizontalLayout tlacitkovy = new HorizontalLayout();
        HorizontalLayout tlacitkovy2 = new HorizontalLayout();
        btnNovy = new Button("Novy", VaadinIcons.FILE_O);
        btnVyber = new Button("Vyber poberateľa", VaadinIcons.ENTER);
        btnVyber.setStyleName(ValoTheme.BUTTON_DANGER);

        btnVyber.addClickListener(clickEvent -> {
            if (!StringUtil.isBlank(poberateliaView.getRodicovskyView())) {
                if (grid.getSelectedItems().iterator().next() != null) {
                    vybratyPoberatel(grid.getSelectedItems().iterator().next());
                }
            }
        });


        btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        btnNovy.setClickShortcut(ShortcutAction.KeyCode.N,
                new int[]
                        {
                                ShortcutAction.ModifierKey.ALT
                        });
        tlacitkovy.addComponent(btnNovy);
        tlacitkovy.addComponent(btnDetail);
        tlacitkovy.addComponent(btnEditujPoznámku);
        tlacitkovy.addComponent(btnPremenujPoberatelaPrevadzkyFirmu);

        if (!StringUtil.isBlank(poberateliaView.getRodicovskyView()))
            tlacitkovy2.addComponent(btnVyber);
        tlacitkovy.addComponent(btnSpat);//666


        String nadpis = new String("Prehľad poberateľov");
//            if (this.poberateliaView!=null)
//                if (this.poberateliaView.getPrevadzka()!=null)
//                    nadpis=nadpis+" prevadzky:"+this.poberateliaView.getPrevadzka().getNazov();


        //binder.readBean(PrevadzkaNastroje.prvaPrevadzkaPodlaNazvu(nazovFirmy).get());

        Binder.Binding<Prevadzka, String> nazovBinding = binder.forField(tfPrevadzkaZakaznika)
                .bind(Prevadzka::getNazov, Prevadzka::setNazov);


        AutocompleteExtension<Prevadzka> autocompleteExtension = new AutocompleteExtension<>(tfPrevadzkaZakaznika);
        autocompleteExtension.setSuggestionListSize(50);
        autocompleteExtension.setSuggestionGenerator(
                this::navrhniPrevadzku,
                this::transformujPrevadzkuNaNazov,
                this::transformujPrevádzkuNaNazovSoZvyraznenymQuery);
        autocompleteExtension.addSuggestionSelectListener(event -> {
            event.getSelectedItem().ifPresent(this::vybrataPrevadzka);
            ;
        });


        hornyFilter.addComponent(tfPrevadzkaZakaznika);
        hornyFilter.addComponent(chkAktivne);

        hornyFilter.addComponent(btnPrezobraz);
        btnPrezobraz.addClickListener(this::aktivujHF);
        gl.addComponent(new

                Label(nadpis));

        gl.addComponent(hornyFilter);
        gl.setComponentAlignment(hornyFilter, Alignment.TOP_LEFT);
        gl.addComponents(grid);
        gl.setComponentAlignment(grid, Alignment.MIDDLE_LEFT);

        gl.addComponent(tlacitkovy);
        gl.addComponent(tlacitkovy2);
        gl.setComponentAlignment(tlacitkovy, Alignment.BOTTOM_LEFT);
        gl.setVisible(true);
        grid.setSizeFull();
        this.

                setSizeFull();
        this.

                addComponentsAndExpand(gl);


    }

    private void premenujPoberatelaPrevadzkuFirmu(WindowWithEditBox w) {
        if (w.getText() != null) {
            Poberatel p = grid.asSingleSelect().getValue();
            PoberatelNastroje.premenujPoberatelaPrevadzkuPoberatela(p, w.getText());

            grid.getDataProvider().refreshItem(p);

        }

    }

    private void vykonajPoZatvoreni(WindowWithEditBox w) {

        if (w.getText() != null) {
            Poberatel p = grid.asSingleSelect().getValue();
            p.setPoznamkaVelkoskladu(w.getText());
            PoberatelNastroje.ulozPoberatela(p);
            grid.getDataProvider().refreshItem(p);
        }

    }

    private void vybrataPrevadzka(Prevadzka prevadzka) {
        if (tfPrevadzkaZakaznika.getValue().length() == 0)
            this.prevadzkaHF = null;
        else
            this.prevadzkaHF = prevadzka;
    }


    private void vybratyPoberatel(Poberatel item) {
        if (poberateliaView.getRodicovskyView().equals(DokladyView.NAME)) {
            DokladyView dv = (DokladyView) poberateliaView.getZdrojovyView();
            dv.getEditacnyForm().vybratyPoberatel(item);
            UI.getCurrent().getNavigator().navigateTo(poberateliaView.getRodicovskyView());
        } else if (poberateliaView.getRodicovskyView().equals(PolozkyDokladuView.NAME)) {
            PolozkyDokladuView pdv = (PolozkyDokladuView) poberateliaView.getZdrojovyView();
            pdv.getEditacnyForm().vybratyPoberatel(item);
            UI.getCurrent().getNavigator().navigateTo(poberateliaView.getRodicovskyView());
        }
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

    public void refresh(Poberatel p) {
        grid.getDataProvider().refreshAll();
        grid.select(p);

    }

    public void rezimVelkoskladu() {
        btnNovy.setVisible(false);

    }


    private List<Prevadzka> navrhniPrevadzku(String query, int cap) {
        if (poberateliaView.getVelkosklad() != null)
            return PrevadzkaNastroje.zoznamPrevadzokVelkoskladuPodlaMena(query, poberateliaView.getVelkosklad()).stream()
                    .filter(prevadzka -> prevadzka.getNazov().toLowerCase().contains(query.toLowerCase()))
                    .limit(30).collect(Collectors.toList());
        else
            return PrevadzkaNastroje.zoznamPrevadzokPodlaMena(query).stream()
                    .filter(prevadzka -> prevadzka.getNazov().toLowerCase().contains(query.toLowerCase()))
                    .limit(30).collect(Collectors.toList());
    }

    /**
     * Co sa zobraziv textfielde, ked sa uz hodnota vyberie
     */
    private String transformujPrevadzkuNaNazov(Prevadzka prevadzka) {
        return prevadzka.getNazov();
    }

    /**
     * Co sa zobrazi v dropdowne
     */
    private String transformujPrevádzkuNaNazovSoZvyraznenymQuery(Prevadzka prevadzka, String query) {
        return "<div class='suggestion-container'>"
                + "<span class='prevadzka'>"
                + prevadzka.getNazov()
                .replaceAll("(?i)(" + query + ")", "<b>$1</b>")
                + "</span>"
                + "</div>";
    }

    private void aktivujHF(Button.ClickEvent clickEvent) {
        poberatelList.clear();
        if (tfPrevadzkaZakaznika.getValue().length() > 0)
            poberatelList = poberateliaView.naplnList(poberateliaView.getVelkosklad(), prevadzkaHF,chkAktivne.getValue());
        else
            poberatelList = poberateliaView.naplnList(poberateliaView.getVelkosklad(), null,chkAktivne.getValue());
        grid.setItems(poberatelList);
        grid.getDataProvider().refreshAll();
    }

}






