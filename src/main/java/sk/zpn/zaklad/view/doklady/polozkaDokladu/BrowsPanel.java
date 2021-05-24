package sk.zpn.zaklad.view.doklady.polozkaDokladu;


import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.*;


import sk.zpn.nastroje.PdfTlacProtokolu;
import sk.zpn.nastroje.XlsExportDavky;
import sk.zpn.nastroje.XlsTlacDokladu;
import sk.zpn.nastroje.XlsTlacProtokolu;
import sk.zpn.zaklad.grafickeNastroje.MFilteredGrid;
import sk.zpn.zaklad.model.DokladyNastroje;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.model.PolozkaDokladuNastroje;
import sk.zpn.zaklad.view.doklady.DokladyView;
import sk.zpn.zaklad.view.poberatelia.RegisterForm;
import sk.zpn.zaklad.view.produkty.ProduktyView;
import sk.zpn.zaklad.view.statistiky.StatPohybyBodovPoberatelovView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private MFilteredGrid<PolozkaDokladu> grid;
    private HorizontalLayout infoPanel;
    private List<PolozkaDokladu> polozkyDokladuList;
    private Label lblInfopanelu;
    private PolozkyDokladuView polozkyDokladuView;
    public Button btnNovy;
    public Button btnNovyKopia;
    public Button btnPanelovy;
    public Button btnKatalogOdmien;
    public Button btnTlac;
    private Button btnRegistrujPoberatela;
    private Firma velkosklad;
    private boolean rezimOdmien = false;
    private boolean klasickyRezim=false;
    private boolean rezimRegistracieBolUplatneny=false;



    public BrowsPanel(List<PolozkaDokladu> polozkyDokladuList, PolozkyDokladuView pdv) {

        this.velkosklad = pdv.getVelkosklad();
        GridLayout gl = new GridLayout(1, 5);
        gl.setSizeFull();
        gl.setRowExpandRatio(0, 0.02f);
        gl.setRowExpandRatio(1, 0.90f);
        gl.setRowExpandRatio(2, 0.02f);
        gl.setRowExpandRatio(3, 0.02f);
        gl.setRowExpandRatio(4, 0.02f);
        infoPanel = new HorizontalLayout();
        lblInfopanelu = new Label("", ContentMode.HTML);
        infoPanel.addComponent(lblInfopanelu);

        this.polozkyDokladuView = pdv;
        this.polozkyDokladuList = polozkyDokladuList;
        grid = new MFilteredGrid<>();
        grid.setItems(this.polozkyDokladuList);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
//        MultiSelectionModelImpl<?> model = (MultiSelectionModelImpl<?>) grid.getSelectionModel();
//        model.setSelectAllCheckBoxVisibility(MultiSelectionModel.SelectAllCheckBoxVisibility.VISIBLE);


        // definitionn of columns
        MFilteredGrid.Column<PolozkaDokladu, String> colProduktKod = grid.addColumn(PolozkaDokladu::getProduktKod).setCaption("Kód produktu").setId("kodProduktu");
        MFilteredGrid.Column<PolozkaDokladu, String> colProduktNazov = grid.addColumn(PolozkaDokladu::getProduktNazov).setCaption("Názov produktu").setId("nazovProduktu");
        MFilteredGrid.Column<PolozkaDokladu, String> colPrevadzkaNazov = grid.addColumn(PolozkaDokladu::getPrevadzkaNazov).setCaption("Prevádzka").setId("nazovPrevadzky");
        MFilteredGrid.Column<PolozkaDokladu, String> colPoberatel = grid.addColumn(PolozkaDokladu::getPoberatelMenoAdresa).setCaption("poberatel").setId("menoPoberatela");
        MFilteredGrid.Column<PolozkaDokladu, BigInteger> colBody = grid.addColumn(PolozkaDokladu::getBodyUpraveneBigInteger).setCaption("Body za pohyb").setId("body");
        MFilteredGrid.Column<PolozkaDokladu, String> colBodyZaProdukt = grid.addColumn(PolozkaDokladu::getBodyZaProdukt).setCaption("Body/za MN").setId("bodyNaProdukte");
        MFilteredGrid.Column<PolozkaDokladu, BigInteger> colMnozstvo = grid.addColumn(PolozkaDokladu::getMnozstvoBigInteger).setCaption("Množstvo").setId("mnozstvo");
        MFilteredGrid.Column<PolozkaDokladu, String> colPoznamka = grid.addColumn(PolozkaDokladu::getPoznamka).setCaption("Poznámka").setId("poznamka");
        grid.registrujZmenuStlpcov("polozkaFokladu");
        // filters
        colProduktKod.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colProduktNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colPrevadzkaNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colPoberatel.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colPoznamka.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnSpat.addClickListener(clickEvent -> {
                    UI.getCurrent().getNavigator().navigateTo(DokladyView.NAME);

                }
        );
        btnKatalogOdmien = new Button("Katalog odmien", VaadinIcons.LIST);

        btnKatalogOdmien.addClickListener(clickEvent ->
                spustiKatalog()
        );

        Button btnDetail = new Button("Detail o poberatelovy", VaadinIcons.INFO_CIRCLE);

        btnRegistrujPoberatela = new Button("Registruj poberateľa", VaadinIcons.CONNECT);
        btnRegistrujPoberatela.addClickListener(clickEvent -> {
            RegisterForm registrujPoberatelaView = new RegisterForm();
            registrujPoberatelaView.setRodicovskyView(polozkyDokladuView.NAME);
            UI.getCurrent().getNavigator().addView(RegisterForm.NAME, registrujPoberatelaView);
            UI.getCurrent().getNavigator().navigateTo(RegisterForm.NAME);
            registrujPoberatelaView.addListener(new Listener() {
                @Override
                public void componentEvent(Event event) {
                    Poberatel poberatel = ((RegisterForm) event.getComponent()).poberatel;
                    if (poberatel != null) {
                        PolozkaDokladu pd= new PolozkaDokladu();
                        pd.setPoberatel(poberatel);
                        pd.setBody(new BigDecimal(ParametreNastroje.nacitajParametre().getBodyZaRegistraciu()));
                        pd.setDoklad(polozkyDokladuView.getDoklad());
                        PolozkaDokladuNastroje.ulozPolozkuDokladu(pd);
                        String msg = String.format("Ulozeny .");

                        Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
                        polozkyDokladuView.pridajNovuPolozkuDokladu(pd);
                        polozkyDokladuView.povodnaPolozka =pd;
                        polozkyDokladuView.refreshPoloziekDokladov();
                        polozkyDokladuView.selectPolozkuDokladu(pd);
                        polozkyDokladuView.aktualizujInfo();
                    }

                }
            });
        });


        btnDetail.addClickListener(clickEvent -> {
            if (polozkyDokladuView.getEditacnyForm().getPolozkaEditovana() != null) {
                if (polozkyDokladuView.getEditacnyForm().getPolozkaEditovana().getPoberatel() != null) {
                    StatPohybyBodovPoberatelovView sv = new StatPohybyBodovPoberatelovView();
                    sv.setRodicovskyView(polozkyDokladuView.NAME);
                    sv.setPoberatel(polozkyDokladuView.getEditacnyForm().getPolozkaEditovana().getPoberatel());

                    UI.getCurrent().getNavigator().addView(sv.NAME, sv);
                    UI.getCurrent().getNavigator().navigateTo(sv.NAME);
                    ;
                }
            }else{
                Notification.show("Nebola vybraná položka!!!", Notification.Type.WARNING_MESSAGE);
            }

        });


        btnPanelovy = new Button("Zobraz/schovaj detail ", VaadinIcons.ARROWS_CROSS);

        Button btnZmaz = new Button("Zmaž", VaadinIcons.CLOSE_CIRCLE);
        btnZmaz.addClickListener(clickEvent ->
                zmaz()
        );


        HorizontalLayout tlacitkovy = new HorizontalLayout();
        HorizontalLayout tlacitkovy2 = new HorizontalLayout();
        btnNovy = new Button("Novy", VaadinIcons.FILE_O);
        btnTlac = new Button("Tlač", VaadinIcons.FILE_O);
        btnTlac.addClickListener(clickEvent ->
                tlac()
        );

        btnNovyKopia = new Button("Novy kópia", VaadinIcons.FILE_O);


        btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        btnNovy.setClickShortcut(ShortcutAction.KeyCode.N,
                new int[]{ShortcutAction.ModifierKey.ALT});


        if (!jeRezimVelkoskladu()) {
            tlacitkovy.addComponent(btnNovy);
            tlacitkovy.addComponent(btnNovyKopia);
            tlacitkovy.addComponent(btnTlac);
            tlacitkovy.addComponent(btnZmaz);
            tlacitkovy.addComponent(btnKatalogOdmien);
            tlacitkovy2.addComponent(btnRegistrujPoberatela);
            btnRegistrujPoberatela.setVisible(false);
        }

        tlacitkovy2.addComponent(btnDetail);//666

        tlacitkovy2.addComponent(btnSpat);//666
        tlacitkovy.addComponent(btnPanelovy);//666

        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String formatovanyDatum = simpleDateFormat.format(polozkyDokladuView.getDoklad().getDatum());


        gl.addComponent(new Label("Prehľad položiek dokladu:  " + polozkyDokladuView.getDoklad().getCisloDokladu() + " z dátumu:" + formatovanyDatum));


        gl.addComponents(grid);
        gl.setComponentAlignment(grid, Alignment.MIDDLE_LEFT);
        gl.addComponent(infoPanel);
        //gl.setComponentAlignment(infoPanel,Alignment.BOTTOM_LEFT);
        gl.addComponent(tlacitkovy);
        gl.addComponent(tlacitkovy2);
        gl.setComponentAlignment(tlacitkovy, Alignment.BOTTOM_LEFT);
        gl.setComponentAlignment(tlacitkovy2, Alignment.BOTTOM_LEFT);
        gl.setVisible(true);
//        aktualizujInfoPanle(0);
        grid.setSizeFull();
        this.setSizeFull();
        this.addComponentsAndExpand(gl);

        //grid.scrollToEnd();


    }

    private void spustiKatalog() {

        ProduktyView produktyView = new ProduktyView();
        produktyView.setTypProduktov(TypProduktov.BODOVACI);
        produktyView.setRodicovskyView(polozkyDokladuView.NAME);
        produktyView.setTypProduktov(TypProduktov.ODMENA);
        UI.getCurrent().getNavigator().addView(ProduktyView.NAME, produktyView);
        UI.getCurrent().getNavigator().navigateTo(ProduktyView.NAME);

    }

    private void tlac() {
        if (rezimOdmien)
//            XlsTlacProtokolu.tlac(polozkyDokladuView.getDoklad());
            PdfTlacProtokolu.tlac(polozkyDokladuView.getDoklad());
        else
            if (polozkyDokladuView.getDoklad().getTypDokladu()==TypDokladu.DAVKA)
                XlsExportDavky.tlac(polozkyDokladuView.getDoklad());
                else
            XlsTlacDokladu.tlac(polozkyDokladuView.getDoklad());
    }


    public void aktualizujInfoPanle(int pocetBodov) {

        String text = "<font size=\"3\" color=\"blue\">Počet všetkých položiek: </font> ";
        text = text + "<font size=\"3\" color=\"green\">" + polozkyDokladuList.size() + "</font> ";
        text = text + "<font size=\"3\" color=\"blue\">  súčet bodov: ";
        text = text + "<font size=\"3\" color=\"green\">" + pocetBodov + "</font> ";

        lblInfopanelu.setValue(text);
    }

    private void zmaz() {
        if (grid.getSelectedItems().size() <= 0)
            return;
        String otazka = new String("Naozaj si prajete odstrániť položku");
        if (grid.getSelectedItems().size() > 1)
            otazka = "Naozaj si prajete odstrániť položky";
        ConfirmDialog.show(UI.getCurrent(), "Odstránenie", otazka + "?",
                "Áno", "Nie", new ConfirmDialog.Listener() {

                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue

                            for (Object o : grid.getSelectionModel().getSelectedItems()) {
                                PolozkaDokladuNastroje.zmazPolozkyDoklady((PolozkaDokladu) o);
                                polozkyDokladuList.remove(o);
                            }
                            grid.getDataProvider().refreshAll();
                            aktualizujInfoPanle(DokladyNastroje.sumaBodov(polozkyDokladuView.getDoklad()));

                        }
                    }
                });

    }


    void refresh() {
        grid.getDataProvider().refreshAll();
        System.out.println("Refresh browsu all");

    }


    void addSelectionListener(Consumer<PolozkaDokladu> listener) {
        grid.asMultiSelect()
                .addValueChangeListener(e -> {
                    Set<PolozkaDokladu> polozkaDokladuSet = e.getValue();
                    if (polozkaDokladuSet.size() == 1)
                        listener.accept(polozkaDokladuSet.iterator().next());

                });

    }

//    void deselect() {
//        PolozkaDokladu value = grid.asSingleSelect().getValue();
//        if (value != null) {
//            grid.getSelectionModel().deselect(value);
//        }
//    }

    void selectFirst() {
        List<PolozkaDokladu> prvaPolozkaDokladu = grid.getDataCommunicator().fetchItemsWithRange(0, 1);
        if (prvaPolozkaDokladu.size() > 0) {
            grid.asMultiSelect().select(prvaPolozkaDokladu.get(0));
        }
    }

    void selectDoklad(PolozkaDokladu polozkaDokladu) {
        Optional.ofNullable(polozkaDokladu).ifPresent(grid.asMultiSelect()::select);
    }

    public void refresh(PolozkaDokladu p) {
        grid.getDataProvider().refreshAll();
        grid.select(p);

    }

    public boolean jeRezimVelkoskladu() {
        return this.velkosklad == null ? false : true;
    }

    public void rezimOdmien() {
        this.rezimOdmien = true;
        grid.removeColumn("nazovPrevadzky");
    }

    public void klasickyRezim() {
        this.rezimOdmien = false;
        btnKatalogOdmien.setVisible(false);
        if ((polozkyDokladuView.getDoklad().getTypDokladu()== TypDokladu.PREVOD)
                || (polozkyDokladuView.getDoklad().getTypDokladu()== TypDokladu.DAVKA))
        btnTlac.setVisible(true);
        else
            btnTlac.setVisible(false);

    }

    public void rezimRegistracia() {
        btnKatalogOdmien.setVisible(false);
        if ((polozkyDokladuView.getDoklad().getTypDokladu()== TypDokladu.PREVOD)
                  || (polozkyDokladuView.getDoklad().getTypDokladu()== TypDokladu.DAVKA))
            btnTlac.setVisible(true);
        else
            btnTlac.setVisible(false);
        btnNovyKopia.setVisible(false);
        btnRegistrujPoberatela.setVisible(true);


        if (!rezimRegistracieBolUplatneny) {
            grid.removeColumn("nazovPrevadzky");
            grid.removeColumn("kodProduktu");
            grid.removeColumn("nazovProduktu");
            grid.removeColumn("mnozstvo");
            grid.removeColumn("bodyNaProdukte");
        }
        rezimRegistracieBolUplatneny=true;

    }

    public void setKlasickyRezim() {
        this.klasickyRezim=true;
    }
}






