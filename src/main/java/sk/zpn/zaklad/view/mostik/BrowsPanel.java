package sk.zpn.zaklad.view.mostik;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.FirmaProdukt;
import sk.zpn.zaklad.model.FirmaProduktNastroje;
import sk.zpn.zaklad.view.ViewConstants;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {

    private FilterGrid<FirmaProdukt> grid;
    private List<FirmaProdukt> firmaProduktList;
    public Button btnNovy;
    private FirmaProdukt oznacenyFirmaProdukt;
    private HorizontalLayout upperFilterHorizontalLayout = new HorizontalLayout();
    private CheckBox cbFilerInvalid = new CheckBox("Zobraz len nevalidné zaznamy");
    private String nazovFirmy;

    public BrowsPanel(List<FirmaProdukt> firmaProduktList, String nazovFirmy) {
        GridLayout gl =new GridLayout(1,1);
        gl.setSpacing(false);
        gl.setSizeFull();
        gl.setColumnExpandRatio(0,1f);
        gl.setRowExpandRatio(0, 1f);



        this.nazovFirmy = nazovFirmy;
        this.firmaProduktList = firmaProduktList;
        upperFilterHorizontalLayout.addComponent(cbFilerInvalid);

        grid = new FilterGrid<>();
        grid.setItems(this.firmaProduktList);

        grid.addStyleName("test");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.getEditor().setEnabled(true);

        Binder<FirmaProdukt> binder = grid.getEditor().getBinder();
        // definitionn of columns
        FilterGrid.Column<FirmaProdukt, String> colKit = grid.addColumn(FirmaProdukt::getKit)
            .setCaption("Kit")
            .setId("kit")
            .setEditorBinding(binder
                .forField(new TextField())
                .withValidator(kit -> !kit.trim().isEmpty(),
                    "Kit nesmie byt prázdny")
                .bind(FirmaProdukt::getKit,
                    (produkt, kit) -> {
                    produkt.setKitAndPersist(kit);
                    refresh();
                }))
            .setEditable(true);
        FilterGrid.Column<FirmaProdukt, String> colKat = grid.addColumn(firmaProdukt -> firmaProdukt.getProdukt().getKat())
            .setCaption("Kat")
            .setId("kat");
        FilterGrid.Column<FirmaProdukt, String> colNazov = grid.addColumn(firmaProdukt -> firmaProdukt.getProdukt().getNazov())
            .setCaption("Názov")
            .setId("nazov");
        FilterGrid.Column<FirmaProdukt, BigDecimal> colKusy = grid.addColumn(firmaProdukt -> firmaProdukt.getProdukt().getKusy())
            .setCaption("Kusy")
            .setId("kusy");
        FilterGrid.Column<FirmaProdukt, BigDecimal> colBody = grid.addColumn(firmaProdukt -> firmaProdukt.getProdukt().getBody())
            .setCaption("Body")
            .setId("body");
        FilterGrid.Column<FirmaProdukt, String> colKoeficient = grid.addColumn(this::getKoeficientDisplayValue);
        colKoeficient.setCaption("Koeficient")
            .setId("koeficient")
            .setEditorBinding(binder
                .forField(new TextField())
                .withConverter(new StringToBigDecimalConverter("Koeficient musi byt desatinné číslo"))
                .withValidator(koeficient -> koeficient.compareTo(BigDecimal.valueOf(0)) != 0,
                    "Koeficient nesmie byt prázdny")
                .bind(FirmaProdukt::getKoeficient,
                    (produkt, koeficient) -> {
                    produkt.setKoeficientAndPersist(koeficient);
                    refresh();
                    }))
            .setEditable(true);
        FilterGrid.Column<FirmaProdukt, String> colFirma = grid.addColumn(firmaProdukt -> firmaProdukt.getFirma().getNazov())
                .setCaption("Firma")
                .setId("firma");

        colKit.setWidth(ViewConstants.THIN_COLUMN_WIDTH);
        colKat.setWidth(ViewConstants.THIN_COLUMN_WIDTH);
        colKusy.setWidth(ViewConstants.THIN_COLUMN_WIDTH);
        colBody.setWidth(ViewConstants.THIN_COLUMN_WIDTH);
        colKoeficient.setWidth(ViewConstants.THIN_COLUMN_WIDTH);

        TextField colKitFilter = new TextField();
        TextField colKatFilter = new TextField();
        TextField colKusyFilter = new TextField();
        TextField colBodyFilter = new TextField();
        TextField colKoeficientFilter = new TextField();

        // filters
        colKit.setFilter(colKitFilter, StringComparator.containsIgnoreCase());
        colKat.setFilter(colKatFilter, StringComparator.containsIgnoreCase());
        colNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colKusy.setFilter(colKusyFilter, StringComparator.containsIgnoreCase());
        colBody.setFilter(colBodyFilter, StringComparator.containsIgnoreCase());
        colKoeficient.setFilter(colKoeficientFilter, StringComparator.containsIgnoreCase());

        colKitFilter.setWidth(ViewConstants.THIN_COLUMN_FILTER_WIDTH, Unit.PIXELS);
        colKatFilter.setWidth(ViewConstants.THIN_COLUMN_FILTER_WIDTH, Unit.PIXELS);
        colKusyFilter.setWidth(ViewConstants.THIN_COLUMN_FILTER_WIDTH, Unit.PIXELS);
        colBodyFilter.setWidth(ViewConstants.THIN_COLUMN_FILTER_WIDTH, Unit.PIXELS);
        colKoeficientFilter.setWidth(ViewConstants.THIN_COLUMN_FILTER_WIDTH, Unit.PIXELS);

        grid.setColumnOrder(colKat, colKit, colNazov, colBody, colKusy, colKoeficient,colFirma);





        gl.addComponents(grid);
        gl.setComponentAlignment(grid,Alignment.MIDDLE_LEFT);


        gl.setVisible(true);
        grid.setSizeFull();
        this.setSizeFull();
        this.addComponentsAndExpand(gl);


//        this.addComponent(upperFilterHorizontalLayout);
//        this.addComponents(grid);
//        this.addComponent(tlacitkovy);

        this.addSelectionListener(firmaProdukt -> oznacenyFirmaProdukt = firmaProdukt);
        cbFilerInvalid.addValueChangeListener(
            event -> refresh()
        );
    }

    void refresh() {
        filterInvalid(cbFilerInvalid.getValue(), nazovFirmy);
        grid.getDataProvider().refreshAll();
        System.out.println("Refresh browsu all");
    }


    void addSelectionListener(Consumer<FirmaProdukt> listener) {
        grid.asSingleSelect()
            .addValueChangeListener(e -> listener.accept(e.getValue()));
    }

    void deselect() {
        FirmaProdukt value = grid.asSingleSelect().getValue();
        if (value != null) {
            grid.getSelectionModel().deselect(value);
        }
    }

    private String getKoeficientDisplayValue(FirmaProdukt firmaProdukt) {
        BigDecimal koeficient = firmaProdukt.getKoeficient();
        return (koeficient == null || koeficient.equals(0)) ? "" : koeficient.toString();
    }

    public FirmaProdukt getOznacenyFirmaProdukt() {
        return oznacenyFirmaProdukt;
    }

    void odstranZaznam(FirmaProdukt firmaProdukt) {
        firmaProduktList.remove(firmaProdukt);
        this.refresh();
    }

    void selectFirst() {
        List<FirmaProdukt> prvyFirmaProduktList = grid.getDataCommunicator().fetchItemsWithRange(0, 1);
        if (prvyFirmaProduktList.size() > 0) {
            grid.asSingleSelect().select(prvyFirmaProduktList.get(0));
        }
    }

    /**
     * If filter enabled displays only item, that are invalid/incomplete.
     *
     * @param filterEnabled flag determining if filter should be applied
     * @param nazovFirmy name of the company
     *
     */
    public void filterInvalid(boolean filterEnabled, String nazovFirmy) {
        if (filterEnabled) {
            firmaProduktList = FirmaProduktNastroje.filterInvalidRecords(firmaProduktList);
        } else {
            firmaProduktList = FirmaProduktNastroje.getListFirmaProduktPodlaNazvuFirmy(nazovFirmy);
        }
        grid.setItems(firmaProduktList);
    }
}



