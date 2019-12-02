package sk.zpn.zaklad.view.firmyVelkoskladu;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.ItemClickListener;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.*;
import sk.zpn.zaklad.model.FirmaNastroje;
import sk.zpn.zaklad.model.UzivatelNastroje;
import sk.zpn.zaklad.model.FirmaVelkoskladuNastroje;
import sk.zpn.zaklad.view.VitajteView;

import java.util.List;
import java.util.stream.Collectors;

public class FirmyVelkoskladuView extends HorizontalLayout implements View {
    public static final String NAME = "FirmyVelkoskladuView";

    Button btnSpat;
    Button btnNova;
    Button btnZrus;
    private TextField txtFirma;
    private final Binder<Firma> binderHF = new Binder<>();
    Button btnPrezobraz;
    Parametre p;
    Firma velkosklad;
    private List<Firma> firmaVelkoskladuList;
    FilterGrid<Firma> grid;
    GridLayout gr = new GridLayout(1, 3);
    HorizontalLayout lHorny = new HorizontalLayout();
    HorizontalLayout lBtn = new HorizontalLayout();

    //Binder<Parametre> binder = new Binder<>();

    public FirmyVelkoskladuView() {


        gr.setSpacing(true);
        gr.setSizeFull();
        gr.setRowExpandRatio(0, 0.10f);
        gr.setRowExpandRatio(1, 0.80f);
        gr.setRowExpandRatio(2, 0.10f);
        txtFirma = new TextField("Veľkosklad:");
        txtFirma.setValue(UzivatelNastroje.getVlastnuFirmuPrihlasenehoUzivala().getNazov());
        txtFirma.setWidth("400");
        grid = new FilterGrid<>();
        this.velkosklad = UzivatelNastroje.getVlastnuFirmuPrihlasenehoUzivala();
        firmaVelkoskladuList = FirmaVelkoskladuNastroje.zoznamFiriem(velkosklad);
        grid.setItems(firmaVelkoskladuList);

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setSizeFull();




        FilterGrid.Column<Firma, String> colIco = grid.addColumn(Firma::getIco).setCaption("IČO").setId("ico");
        FilterGrid.Column<Firma, String> colnazov = grid.addColumn(Firma::getNazov).setCaption("Názov").setId("nazov");


        btnSpat = new Button("Späť",VaadinIcons.ARROW_BACKWARD);
        btnNova = new Button("Pridaj", VaadinIcons.FILE_O);
        btnZrus = new Button("Zruš",VaadinIcons.CLOSE_CIRCLE );
        btnPrezobraz = new Button("Prezobraz",VaadinIcons.REFRESH);
        btnPrezobraz.setHeight("60");
        btnPrezobraz.setWidth("30%");

        if (UzivatelNastroje.getPrihlasenehoUzivatela().getTypUzivatela() == TypUzivatela.PREDAJCA) {
            btnPrezobraz.setEnabled(false);
            txtFirma.setEnabled(false);
        }
        lBtn.addComponent(btnNova);
        lBtn.addComponent(btnZrus);
        lBtn.addComponent(btnSpat);
        lHorny.addComponent(txtFirma);
        lHorny.addComponent(btnPrezobraz);


        gr.addComponent(lHorny);
        gr.addComponent(grid);
        gr.addComponent(lBtn);
        gr.setComponentAlignment(lHorny, Alignment.MIDDLE_LEFT);
        gr.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
        gr.setComponentAlignment(lBtn, Alignment.MIDDLE_LEFT);

        gr.setVisible(true);
        gr.setSizeFull();
        btnSpat.addClickListener(this::spat);
        btnZrus.addClickListener(this::zrus);

        btnNova.addClickListener(clickEvent ->
                {
                    PridanieNovej pn = new PridanieNovej();
                    pn.nova();
                }
        );
        btnPrezobraz.addClickListener(this::aktivujHF);
        this.addComponentsAndExpand(gr);
        this.setSizeFull();


        binderHF.readBean(FirmaNastroje.prvaFirmaPodlaNazvu(velkosklad.getNazov()).get());

        Binder.Binding<Firma, String> nazovBinding = binderHF.forField(txtFirma)
                .withValidator(v -> !txtFirma.getValue().trim().isEmpty(),
                        "Názov je poviný")
                .bind(Firma::getNazov, Firma::setNazov);


        AutocompleteExtension<Firma> firmaAutocompleteExtension = new AutocompleteExtension<>(txtFirma);
        firmaAutocompleteExtension.setSuggestionGenerator(
                this::navrhniFirmu,
                this::transformujFirmuNaNazov,
                this::transformujFirmuNaNazovSoZvyraznenymQuery);


        firmaAutocompleteExtension.setSuggestionListSize(50);
        firmaAutocompleteExtension.addSuggestionSelectListener(event -> {
            event.getSelectedItem().ifPresent(this::vybrataFirma);
        });


    }

    private void zrus(Button.ClickEvent clickEvent) {
        Firma oznacenaFirma = (Firma) grid.getSelectedItems().iterator().next();
        if (oznacenaFirma==null) {
            return;
        }

        ConfirmDialog.show(UI.getCurrent(), "Odstránenie firmy",
                "Naozaj si prajete odstrániť firmu zo zoznamu pre import ?",
                "Áno", "Nie", new ConfirmDialog.Listener() {

                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue

                            FirmaVelkoskladuNastroje.zmazFirmuVelkoskladu(oznacenaFirma,velkosklad);
                            firmaVelkoskladuList.remove(oznacenaFirma);
                            grid.getDataProvider().refreshAll();

                        }
                    }
                });

    }

    private class PridanieNovej {
        FilterGrid<FirmaRegistra> gridSlovenskoDigital;
        Firma vybranaFirma = null;
        Button btnUloz;
        Button btnOver;
        Button btnVyberFiriem;
        Button btnStorno;
        Window wPridania;
        Label lbl;
        TextField tfIco;
        TextField tfNazov;


        public PridanieNovej() {
        }

        ;

        private void nova() {

            wPridania = new Window("Pridanie firmu");
            wPridania.setWidth(900, Unit.PIXELS);
            wPridania.setHeight(500, Unit.PIXELS);
            FormLayout lEdit = new FormLayout();
            tfIco = new TextField("IČO (úplné):");
            tfNazov = new TextField("Názov (aspoň 4 znaky):");

            tfNazov.setWidth("400");
            lEdit.addComponent(new Label("Zadajte časť názvu alebo celé IČO " ));
            lEdit.addComponent(new Label("a použite tlačítko Výber z firiem registra"));
            lEdit.addComponent(tfIco);
            lEdit.addComponent(tfNazov);


            VerticalLayout vl = new VerticalLayout();
            HorizontalLayout lBtn = new HorizontalLayout();
            btnUloz = new Button("Pridaj firmu");
            btnOver = new Button("Over");
            btnOver.setDescription("Overí či je firma evidovaná, ak nie je pokračujte cez výber firiem ");
            btnVyberFiriem = new Button("Výber firiem z registra");
            btnVyberFiriem.setDescription("Doplní povinné údaje pre register firiem");
            btnStorno = new Button("Zatvor");
            btnStorno.addClickListener(this::ukonciPridanie);
            btnUloz.addClickListener(this::ulozFirmu);
            btnUloz.setDescription("Uloží firmu do povolených firiem");
            btnOver.addClickListener(this::overFirmu);
            btnVyberFiriem.addClickListener(this::vyberFirmuDS);
            btnUloz.setEnabled(false);

            lBtn.addComponent(btnOver);
            lBtn.addComponent(btnUloz);
            lBtn.addComponent(btnVyberFiriem);
            lBtn.addComponent(btnStorno);
            vl.addComponent(lEdit);


            vl.addComponent(lBtn);
            vl.setComponentAlignment(lEdit, Alignment.TOP_CENTER);
            vl.setComponentAlignment(lBtn, Alignment.BOTTOM_CENTER);

            wPridania.setContent(vl);
            wPridania.setModal(true);

            wPridania.center();
            UI.getCurrent().addWindow(wPridania);

        }

        private void vyberFirmuDS(Button.ClickEvent clickEvent) {
            vyberFirmu();
        }

        private void vyberFirmu() {
            if ((tfNazov.getValue().length() < 1) && (tfIco.getValue().length() < 3))
                return;
            Window subWindow = new Window("Firmy");
            subWindow.setWidth(900, Unit.PIXELS);
            subWindow.setHeight(1900, Unit.PIXELS);

            FirmySlovenskoDigital fsd = new FirmySlovenskoDigital();

            List<FirmaRegistra> firmaList = fsd.nacitanieDat(tfNazov.getValue(), tfIco.getValue(), false);

            gridSlovenskoDigital = new FilterGrid<>();
            gridSlovenskoDigital.setItems(firmaList);
            gridSlovenskoDigital.setSelectionMode(Grid.SelectionMode.SINGLE);
            gridSlovenskoDigital.setWidth(100, Unit.PERCENTAGE);
            gridSlovenskoDigital.setHeight(100, Unit.PERCENTAGE);
            gridSlovenskoDigital.setHeightByRows(15);

            gridSlovenskoDigital.addItemClickListener(new ItemClickListener<FirmaRegistra>() {
                                                          @Override

                                                          public void itemClick(Grid.ItemClick<FirmaRegistra> event) {
                                                              if (event.getMouseEventDetails().isDoubleClick()) {
                                                                  vybranaFirma = new Firma();
                                                                  vybranaFirma.setIco(event.getItem().getIco());
                                                                  vybranaFirma.setNazov(event.getItem().getNazov());
                                                                  vybranaFirma.setPsc(event.getItem().getPsc());
                                                                  vybranaFirma.setUlica(event.getItem().getUlica() + " " + event.getItem().getCisloDomu());
                                                                  vybranaFirma.setMesto(event.getItem().getObec());


                                                                  if (FirmaNastroje.firmaPodlaICO(vybranaFirma.getIco()) == null) {
                                                                      pridatNeexistujucu(vybranaFirma);

                                                                  }
                                                                  tfIco.setValue(vybranaFirma.getIco());
                                                                  tfNazov.setValue(vybranaFirma.getNazov());
                                                                  overFirmu(null);

                                                                  subWindow.close();


                                                              }
                                                          }
                                                      }
            );


            // definitionn of columns
            FilterGrid.Column<FirmaRegistra, String> colIco = gridSlovenskoDigital.addColumn(FirmaRegistra::getIco).setCaption("IČO").setId("ico");
            FilterGrid.Column<FirmaRegistra, String> colNazov = gridSlovenskoDigital.addColumn(FirmaRegistra::getNazov).setCaption("Názov").setId("nazov");
            FilterGrid.Column<FirmaRegistra, String> colUlica = gridSlovenskoDigital.addColumn(FirmaRegistra::getUlica).setCaption("Ulica").setId("ulica");
            FilterGrid.Column<FirmaRegistra, String> colPsc = gridSlovenskoDigital.addColumn(FirmaRegistra::getPsc).setCaption("Psč").setId("psc");
            FilterGrid.Column<FirmaRegistra, String> colMesto = gridSlovenskoDigital.addColumn(FirmaRegistra::getObec).setCaption("Mesto/obec").setId("mesto");


            // filters
            colIco.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
            colNazov.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
            colUlica.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
            colPsc.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
            colMesto.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
            gridSlovenskoDigital.setColumnOrder(colIco, colNazov, colUlica, colPsc, colMesto);

            VerticalLayout vl = new VerticalLayout();
            vl.addComponentsAndExpand(gridSlovenskoDigital);
            vl.addComponents(new Label("Dvojklikom vyberte firmu!!!!!!"));
            subWindow.setContent(vl);
            subWindow.setModal(true);


            subWindow.center();
            UI.getCurrent().addWindow(subWindow);
        }


        private void pridatNeexistujucu(Firma vFirma) {


            String otazka = new String("Firma v systéme neexistuje. Založiť");
            ConfirmDialog.show(UI.getCurrent(), "Založiť", otazka + "?",
                    "Áno", "Nie", new ConfirmDialog.Listener() {

                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                // Confirmed to continue
                                vybranaFirma = FirmaNastroje.ulozFirmu(vFirma);
                                if (vybranaFirma.getId() != null)
                                    overFirmu(null);


                            }
                        }
                    });
        }

        private void ukonciPridanie(Button.ClickEvent clickEvent) {
            wPridania.close();
        }

        private void ulozFirmu(Button.ClickEvent clickEvent) {
            FirmaVelkoskladu fv = new FirmaVelkoskladu();
            fv.setOdberatel(vybranaFirma);
            fv.setVelkosklad(velkosklad);
            if (FirmaVelkoskladuNastroje.existuje(fv)==null) {
                FirmaVelkoskladuNastroje.uloz(fv);
                firmaVelkoskladuList.add(fv.getOdberatel());
                grid.getDataProvider().refreshAll();
                grid.select(fv.getOdberatel());


            }
            wPridania.close();
        }

        private void overFirmu(Button.ClickEvent clickEvent) {

            vybranaFirma = FirmaNastroje.firmaPodlaICO(tfIco.getValue());
            if (vybranaFirma != null) {
                tfNazov.setValue(vybranaFirma.getNazov());
                tfIco.setValue(vybranaFirma.getIco());
                btnUloz.setEnabled(true);
            }

        }


    }

    private void vybrataFirma(Firma firma) {
        velkosklad = firma;
    }

    private void aktivujHF(Button.ClickEvent clickEvent) {
        if (txtFirma.getValue().length() > 0) {

            firmaVelkoskladuList.clear();
            firmaVelkoskladuList = FirmaVelkoskladuNastroje.zoznamFiriem(velkosklad);
            grid.setItems(firmaVelkoskladuList);
            grid.getDataProvider().refreshAll();

        }
    }


    private void spat(Button.ClickEvent clickEvent) {
        UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME);
        //doplnit overeneie ci uz existuje a ak neexistuje tak zalozit

    }

    private List<Firma> navrhniFirmu(String query, int cap) {
        return FirmaNastroje.zoznamFiriemIbaVelkosklady().stream()
                .filter(firma -> firma.getNazov().toLowerCase().contains(query.toLowerCase()))
                .limit(cap).collect(Collectors.toList());
    }

    /**
     * Co sa zobraziv textfielde, ked sa uz hodnota vyberie
     */
    private String transformujFirmuNaNazov(Firma firma) {
        return firma.getNazov();
    }

    /**
     * Co sa zobrazi v dropdowne
     */
    private String transformujFirmuNaNazovSoZvyraznenymQuery(Firma firma, String query) {
        return "<div class='suggestion-container'>"
                + "<span class='firma'>"
                + firma.getNazov()
                .replaceAll("(?i)(" + query + ")", "<b>$1</b>")
                + "</span>"
                + "</div>";
    }


}
