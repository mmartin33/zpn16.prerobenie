package sk.zpn.zaklad.view.firmy;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.Registration;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.ItemClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.*;
import sk.zpn.zaklad.model.FirmaNastroje;
import sk.zpn.zaklad.model.PoberatelNastroje;
import sk.zpn.zaklad.model.PrevadzkaNastroje;

import java.util.List;
import java.util.stream.Collectors;

public class EditacnyForm extends VerticalLayout {


    private TextField tMesto;
    private TextField tPsc;
    private TextField tTelefon;
    private TextField tUlica;
    private TextField tICO;
    private TextField tNazov;
    private TextField tDic;
    private TextField tIcDph;
    private TextField tPoberatelNazov;


    private Poberatel poberatel;

    protected Button btnUloz;

    protected Button btnZmaz;

    protected Button btnVyber;
    private final Binder<Firma> binder = new Binder<>();
    private Firma firmaEditovana;
    private FirmyView firmyView;
    private Poberatel aktualnyPoberatel;

    public EditacnyForm() {
        tICO = new TextField("IČO");
        tICO.setMaxLength(12);
        tNazov = new TextField("Názov");
        tNazov.setWidth("400");
        tDic = new TextField("Dič");
        tDic.setMaxLength(10);
        tIcDph = new TextField("ič DPH");
        tIcDph.setMaxLength(12);
        tUlica = new TextField("Ulica");
        tUlica.setWidth("400");
        tMesto = new TextField("Mesto");
        tMesto.setWidth("400");
        tPsc = new TextField("PSČ");
        tPsc.setMaxLength(5);
        tPsc.setWidth("50%");
        tTelefon = new TextField("Telefon");

        tPoberatelNazov = new TextField("Poberatel");

        btnUloz=new Button("Ulož", VaadinIcons.CHECK_CIRCLE);
        btnZmaz =new Button("Zmaž",VaadinIcons.CLOSE_CIRCLE);
        btnVyber = new Button("Výber firmy zo slovensko-digital");
        nastavComponnenty();
        FormLayout lEdit = new FormLayout();
        lEdit.addComponent(tICO);
        lEdit.addComponent(tNazov);
        lEdit.addComponent(tDic);
        lEdit.addComponent(tIcDph);
        lEdit.addComponent(tUlica);
        lEdit.addComponent(tMesto);
        lEdit.addComponent(tPsc);
        lEdit.addComponent(tTelefon);
        lEdit.addComponent(tPoberatelNazov);


        HorizontalLayout lBtn = new HorizontalLayout();
        lBtn.addComponent(btnUloz);
        lBtn.addComponent(btnZmaz);
        lBtn.addComponent(btnVyber);


        this.addComponent(lEdit);
        this.addComponent(lBtn);


        AutocompleteExtension<Poberatel> poberatelAutocompleteExtension = new AutocompleteExtension<>(tPoberatelNazov);
        poberatelAutocompleteExtension.setSuggestionGenerator(
                this::navrhniPoberatela,
                this::transformujPoberatelaNaMeno,
                this::transformujPoberatelaNaMenoSoZvyraznenymQuery);


    }

    private void nastavComponnenty() {


        Binder.Binding<Firma, String> icoBinding = binder.forField(tICO)
                .withValidator(v -> !tICO.getValue().trim().isEmpty(),
                        "IČO je povinné")
//                .withValidator(b -> (FirmaNastroje.firmaPodlaICO(tICO.getValue())==null),
//                        "IČO už existuje")
                .bind(Firma::getIco, Firma::setIco);

        Binder.Binding<Firma, String> nazovBinding = binder.forField(tNazov)
                .withValidator(v -> !tNazov.getValue().trim().isEmpty(),
                        "Názov je poviný")
                .bind(Firma::getNazov, Firma::setNazov);

        Binder.Binding<Firma, String> dicBinding = binder.forField(tDic)
                .bind(Firma::getDic, Firma::setDic);

        Binder.Binding<Firma, String> icdphBinding = binder.forField(tIcDph)
                .bind(Firma::getIc_dph, Firma::setIc_dph);

        Binder.Binding<Firma, String> ulicaBinding = binder.forField(tUlica)
                .bind(Firma::getUlica, Firma::setUlica);

        Binder.Binding<Firma, String> mestoBinding = binder.forField(tMesto)
                .bind(Firma::getMesto, Firma::setMesto);

        Binder.Binding<Firma, String> pscBinding = binder.forField(tPsc)
                .bind(Firma::getPsc, Firma::setPsc);

        Binder.Binding<Firma, String> telefonBinding = binder.forField(tTelefon)
                .bind(Firma::getTelefon, Firma::setTelefon);


//       Binder.Binding<Prevadzka, String> poberatelBinding = binder.forField(tPoberatelNazov)
//           .withValidator(nazovPoberatela -> PoberatelNastroje.prvyPoberatelPodlaMena(nazovPoberatela).isPresent(),
//               "Poberateľ musi byt existujuci")
//           .bind(poberatel -> this.getPoberatel() == null ? "" : this.getPoberatel().getMeno(),
//               (poberatel, s) -> PoberatelNastroje.prvyPoberatelPodlaMena(tPoberatelNazov.getValue()).ifPresent(poberatel::this.setPoberatel(poberatel)));


//        Binder.Binding<Firma, String> poberatelBinding = binder.forField(tPoberatelNazov)
//                .withValidator(nazovPoberatel -> this.aktualnyPoberatel != null, "Poberteľ musi bzt vyplneny")
//                .withValidator(nazovPoberatela -> PoberatelNastroje.poberatelPodlaId(this.aktualnyPoberatel.getId()).isPresent(),
//                        "Poberateľ musi byt existujuci")
//                .bind(firma -> firma.getPoberatel() == null ? "" : prevadzka.getPoberatel().getMeno(),
//                        (prevadzka, s) -> PoberatelNastroje.poberatelPodlaId(this.aktualnyPoberatel.getId()).ifPresent(prevadzka::setPoberatel));




        tICO.addValueChangeListener(event -> icoBinding.validate());
        tNazov.addValueChangeListener(event -> nazovBinding.validate());

        btnUloz.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnUloz.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        btnUloz.addClickListener(this::save);
        btnZmaz.addClickListener(this::delete);
        btnVyber.addClickListener(this::vyber);

        //setVisible(false);


    }

    void edit(Firma firma) {
        firmaEditovana = firma;
        if (firma != null) {
            System.out.println("Zvolená " + firmaEditovana.getNazov());
            binder.readBean(firma);
        } else {
            System.out.println("Zvolená nová");
            binder.readBean(firma);
        }

    }

    public void vyber(Button.ClickEvent event) {
        if ((tNazov.getValue().length() < 1) && (tICO.getValue().length() < 3))
            return;
        Window subWindow = new Window("Firmy");
        subWindow.setWidth(900,Unit.PIXELS);
        subWindow.setHeight(1900,Unit.PIXELS);
        FilterGrid<FirmaRegistra> grid;
        FirmySlovenskoDigital fsd = new FirmySlovenskoDigital();

        List<FirmaRegistra> firmaList = fsd.nacitanieDat(tNazov.getValue(), tICO.getValue(), false);

        grid = new FilterGrid<>();
        grid.setItems(firmaList);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setWidth(100, Unit.PERCENTAGE);
        grid.setHeight(100, Unit.PERCENTAGE);
        grid.setHeightByRows(15);

        grid.addItemClickListener(new ItemClickListener<FirmaRegistra>() {
                                      @Override

                                     public void itemClick(Grid.ItemClick<FirmaRegistra> event) {
                                          if (event.getMouseEventDetails().isDoubleClick()) {
                                              tNazov.setValue(event.getItem().getNazov());
                                              tICO.setValue(event.getItem().getIco());
                                              tPsc.setValue(event.getItem().getPsc());
                                              tUlica.setValue(event.getItem().getUlica()+" "+event.getItem().getCisloDomu());
                                              tMesto.setValue(event.getItem().getObec());
                                              subWindow.close();
                                          } }
                                  }
        );


        // definitionn of columns
        FilterGrid.Column<FirmaRegistra, String> colIco = grid.addColumn(FirmaRegistra::getIco).setCaption("IČO").setId("ico");
        FilterGrid.Column<FirmaRegistra, String> colNazov = grid.addColumn(FirmaRegistra::getNazov).setCaption("Názov").setId("nazov");
        FilterGrid.Column<FirmaRegistra, String> colUlica = grid.addColumn(FirmaRegistra::getUlica).setCaption("Ulica").setId("ulica");
        FilterGrid.Column<FirmaRegistra, String> colPsc = grid.addColumn(FirmaRegistra::getPsc).setCaption("Psč").setId("psc");
        FilterGrid.Column<FirmaRegistra, String> colMesto = grid.addColumn(FirmaRegistra::getObec).setCaption("Mesto/obec").setId("mesto");


        // filters
        colIco.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colNazov.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colUlica.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colPsc.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colMesto.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        grid.setColumnOrder(colIco, colNazov, colUlica, colPsc, colMesto);

        VerticalLayout vl = new VerticalLayout();
        vl.addComponentsAndExpand(grid);
        subWindow.setContent(vl);
        subWindow.setModal(true);


        subWindow.center();
        UI.getCurrent().addWindow(subWindow);
    }

    public void save(Button.ClickEvent event) {
        if (binder.writeBeanIfValid(firmaEditovana)) {
            boolean jeFirmaNova = firmaEditovana.isNew();
            Firma ulozenaFirma = FirmaNastroje.ulozFirmu(firmaEditovana);
            String msg = String.format("Ulozeny .",
                    firmaEditovana.getNazov());

            Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
            if (jeFirmaNova) {
                PrevadzkaNastroje.ulozPrvuPrevadzku(ulozenaFirma,null);
                firmyView.pridajNovuFirmu(ulozenaFirma);
            }
            firmyView.refreshFiriem();

        }

    }

    public void delete(Button.ClickEvent event) {

        ConfirmDialog.show(UI.getCurrent(), "Odstránenie", "Naozaj si prajete odstrániť firmu " + firmaEditovana.getNazov() + "?",
                "Áno", "Nie", new ConfirmDialog.Listener() {

                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue
                            FirmaNastroje.zmazFirmu(firmaEditovana);
                            firmyView.odstranFirmu(firmaEditovana);
                            Notification.show("Firma odstránena", Notification.Type.TRAY_NOTIFICATION);
                        }
                    }
                });

    }

    private List<Poberatel> navrhniPoberatela(String query, int cap) {

        return  PoberatelNastroje.zoznamPoberatelov().stream()
                .filter(poberatel -> poberatel.getMeno().toLowerCase().contains(query.toLowerCase()))
                .limit(cap).collect(Collectors.toList());
    }

    /**
     * Co sa zobraziv textfielde, ked sa uz hodnota vyberie
     * */
    private String transformujPoberatelaNaMeno(Poberatel poberatel) {
        return poberatel.getMeno();

    }

    /**
     * Co sa zobrazi v dropdowne
     * */
    private String transformujPoberatelaNaMenoSoZvyraznenymQuery(Poberatel poberatel, String query) {
        return "<div class='suggestion-container'>"
                + "<span class='poberatel'>"
                + poberatel.getPoberatelMenoAdresa()
//                + poberatel.getMeno()
                .replaceAll("(?i)(" + query + ")", "<b>$1</b>")
                + "</span>"
                + "</div>";
    }
    public void setFirmaView(FirmyView firmaView) {
        this.firmyView = firmaView;
    }

    public Poberatel getPoberatel() {
        return poberatel;
    }

    public void setPoberatel(Poberatel poberatel) {
        this.poberatel = poberatel;
    }
}

