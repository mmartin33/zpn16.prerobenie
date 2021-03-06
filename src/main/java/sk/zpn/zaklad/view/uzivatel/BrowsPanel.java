package sk.zpn.zaklad.view.uzivatel;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;

import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.StatusUzivatela;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.grafickeNastroje.MFilteredGrid;
import sk.zpn.zaklad.model.UzivatelNastroje;
import sk.zpn.zaklad.view.VitajteView;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private MFilteredGrid<Uzivatel> grid;
    private List<Uzivatel> uzivatelList;


    public Button btnNovy;



    public BrowsPanel(List<Uzivatel> uzivatelList) {
        this.uzivatelList = uzivatelList;
        grid = new MFilteredGrid<>();
        grid.setItems(this.uzivatelList);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setWidth(1000, Unit.PIXELS);
        grid.setHeight(700, Unit.PIXELS);

        // definitionn of columns
        MFilteredGrid.Column<Uzivatel, String> colMeno = grid.addColumn(Uzivatel::getMeno).setCaption("Meno").setId("meno");
        MFilteredGrid.Column<Uzivatel, String> colTypUzivatela = grid.addColumn(uzivatel -> uzivatel.getTypUzivatela().getDisplayValue()).setCaption("Typ konta").setId("typ");
        MFilteredGrid.Column<Uzivatel, String> colFirmaNazov = grid.addColumn(Uzivatel::getFirmaNazov).setCaption("Firma").setId("nazovFirmy");
        MFilteredGrid.Column<Uzivatel, String> colStatusUzivatela = grid.addColumn(uzivatel -> uzivatel.getStatusUzivatela().getIconValue()).setCaption("Stav konta").setId("statusUzivatela");
        colStatusUzivatela.setRenderer(new HtmlRenderer());
        colStatusUzivatela.setWidth(150);
        //grid.registrujZmenuStlpcov("uzivatel");

        // filters


        colMeno.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colTypUzivatela.setFilter(new ComboBox<>("", TypUzivatela.getListOfDisplayValues()),
                (cValue, fValue) -> fValue == null || fValue.equals(cValue));
        colFirmaNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());


        ComboBox<String> statusUzivatelaFilter = new ComboBox<>("", StatusUzivatela.getListOfDisplayValues());
        statusUzivatelaFilter.setWidth(120, Unit.PIXELS);

        colStatusUzivatela.setFilter(statusUzivatelaFilter,
        (cValue, fValue) -> fValue == null || cValue.contains(StatusUzivatela.fromDisplayName(fValue).getIconColor()));

        grid.setColumnOrder(colMeno, colTypUzivatela,colFirmaNazov, colStatusUzivatela);
        Button btnSpat=new Button("Sp????", VaadinIcons.ARROW_BACKWARD);
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );

        Button btnGenerujMenaAHesla=new Button("Generuj pr??stupy a hesl??", VaadinIcons.ARROW_BACKWARD);
        btnGenerujMenaAHesla.addClickListener(clickEvent ->
                generujMenaAHesla()
        );
        HorizontalLayout tlacitkovy=new HorizontalLayout();
        btnNovy=new Button("Novy",VaadinIcons.FILE_O);

        tlacitkovy.addComponent(btnNovy);
        tlacitkovy.addComponent(btnGenerujMenaAHesla);
        tlacitkovy.addComponent(btnSpat);//666

        this.addComponent(new Label("Preh??ad u????vate??ov"));
        this.addComponents(grid);
        this.addComponent(tlacitkovy);
    }

    private void generujMenaAHesla() {
        UzivatelNastroje.generujNoveMenaAHesla();
    }


    void refresh() {
        grid.getDataProvider().refreshAll();
        System.out.println("Refresh browsu all");

    }

    void addSelectionListener(Consumer<Uzivatel> listener) {
        grid.asSingleSelect()
                .addValueChangeListener(e -> listener.accept(e.getValue()));
    }

    void deselect() {
        Uzivatel value = grid.asSingleSelect().getValue();
        if (value != null) {
            grid.getSelectionModel().deselect(value);
        }
    }

    void selectFirst() {
        List<Uzivatel> prvyUzivatelList = grid.getDataCommunicator().fetchItemsWithRange(0,1);
        if(prvyUzivatelList.size() > 0){
            grid.asSingleSelect().select(prvyUzivatelList.get(0));
        }
    }

    void selectUzivatel(Uzivatel uzivatel) {
        Optional.ofNullable(uzivatel).ifPresent(grid.asSingleSelect()::select);
    }

}






