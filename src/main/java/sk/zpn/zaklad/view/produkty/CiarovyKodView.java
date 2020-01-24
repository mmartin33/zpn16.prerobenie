package sk.zpn.zaklad.view.produkty;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import sk.zpn.domena.CiarovyKod;
import sk.zpn.domena.Produkt;
import sk.zpn.zaklad.grafickeNastroje.MFilteredGrid;
import sk.zpn.zaklad.model.CiarovyKodNastroje;

import java.util.List;


public class CiarovyKodView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "CiarovyKodView";
    private FormLayout pravyPanel = new FormLayout();
    private GridLayout gridPanel = new GridLayout(1, 3);
    ;;
    private MFilteredGrid<CiarovyKod> grid = new MFilteredGrid<>();
    private HorizontalLayout panelNadpis = new HorizontalLayout();
    private HorizontalLayout tlacitkovyPanel = new HorizontalLayout();
    private List<CiarovyKod> ciarovyKodList = null;
    private Produkt produkt = null;
    private TextField tEAN;
    private Binder<CiarovyKod> binder = new Binder<>();
    ;

    private CiarovyKod ciarovyKodEditovany;

    public CiarovyKodView(Produkt produkt) {
        this.produkt = produkt;
        gridPanel.setSpacing(false);
        gridPanel.setRowExpandRatio(0, 0.10f);
        gridPanel.setRowExpandRatio(1, 0.80f);
        gridPanel.setRowExpandRatio(2, 0.10f);
        grid.setSizeFull();

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setSizeFull();
        ciarovyKodList = CiarovyKodNastroje.zoznam(produkt);
        grid.setItems(ciarovyKodList);

        MFilteredGrid.Column<CiarovyKod, String> colKod = grid.addColumn(CiarovyKod::getCiarovyKod).setCaption("Kedy").setId("kedy");

        // filters
        grid.registrujZmenuStlpcov("ean");
        grid.addSelectionListener(this::vyberEan);

        tEAN = new TextField("Čiarový kód");
        tEAN.setWidth("200");

        Binder.Binding<CiarovyKod, String> eanBinding = binder.forField(tEAN)
                .withValidator(kod -> !tEAN.getValue().trim().isEmpty(),
                        "Kód je povinný")
                .withValidator(kod -> !CiarovyKodNastroje.existuje(tEAN.getValue().toString()), "Kód musí byť jedinečný")
                .bind(CiarovyKod::getCiarovyKod, CiarovyKod::setCiarovyKod);
        AutocompleteExtension<CiarovyKod> autocompleteExtension = new AutocompleteExtension<>(tEAN);


        Button btnUloz = new Button("Ulož", VaadinIcons.CHECK_CIRCLE);
        if (ciarovyKodList.size() == 0)
            btnUloz.setEnabled(false);
        btnUloz.addClickListener(clickEvent ->
                {
                    uloz(ciarovyKodEditovany);
                }
        );
        pravyPanel.addComponent(tEAN);
        pravyPanel.addComponent(btnUloz);


        Button btnNovy = new Button("Nový čiarový kód", VaadinIcons.NEWSPAPER);
        Button btnZrus = new Button("Výmazať", VaadinIcons.FILE_REMOVE);
        Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnNovy.addClickListener(clickEvent ->
                {
                    edit(new CiarovyKod());
                    btnUloz.setEnabled(true);
                }
        );
        btnZrus.addClickListener(clickEvent ->
                {
                    vymaz();
                }
        );

        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(ProduktyView.NAME)
        );

        btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        tlacitkovyPanel.addComponent(btnNovy);
        tlacitkovyPanel.addComponent(btnZrus);
        tlacitkovyPanel.addComponent(btnSpat);

        Label nadpis = new Label("Čiarové kódy");
        panelNadpis.addComponent(nadpis);
        gridPanel.addComponent(panelNadpis);
        gridPanel.addComponent(grid);
        gridPanel.addComponent(tlacitkovyPanel);

        gridPanel.setComponentAlignment(grid, Alignment.MIDDLE_LEFT);
        gridPanel.setComponentAlignment(tlacitkovyPanel, Alignment.MIDDLE_LEFT);
        gridPanel.setVisible(true);
        gridPanel.setSizeFull();
        grid.setSizeFull();
        this.addComponent(gridPanel);
        this.addComponent(pravyPanel);
        this.setComponentAlignment(pravyPanel, Alignment.MIDDLE_LEFT);
        this.setSizeFull();
        grid.getDataProvider().refreshAll();

    }

    private void vymaz() {
        CiarovyKodNastroje.zmazCiarovyKod(ciarovyKodEditovany);
        ciarovyKodList.remove(ciarovyKodEditovany);
        grid.getDataProvider().refreshAll();
    }

    private void vyberEan(SelectionEvent<CiarovyKod> ciarovyKodSelectionEvent) {
        edit(ciarovyKodSelectionEvent.getFirstSelectedItem().get());
    }

    private void edit(CiarovyKod ciarovyKod) {
        ciarovyKodEditovany = (CiarovyKod) ciarovyKod;
        if (ciarovyKodEditovany == null) {
        } else {
            ciarovyKodEditovany.setProdukt(produkt);
        }
        binder.readBean(ciarovyKodEditovany);
    }

    private void uloz(CiarovyKod ciarovyKod) {
        if (binder.writeBeanIfValid(ciarovyKod)) {
            CiarovyKodNastroje.uloz(ciarovyKod);
            ciarovyKodList.add(ciarovyKod);
            grid.getDataProvider().refreshAll();
        }
    }
}
