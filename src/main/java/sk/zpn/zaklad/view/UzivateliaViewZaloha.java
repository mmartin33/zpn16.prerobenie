package sk.zpn.zaklad.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.model.UzivatelNastroje;
import sk.zpn.zaklad.view.uzivatel.EditacnyForm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UzivateliaViewZaloha extends VerticalLayout implements View {
    public static final String NAME = "uzivateliaView";
    private EditacnyForm editacnyForm;
    TextField tMeno;
    TextField tFirma;
    ComboBox cTyp;
    Grid<Uzivatel> grid;

    public UzivateliaViewZaloha() {

        List<Uzivatel> u = UzivatelNastroje.zoznamUzivatelov();

        grid = new Grid<>();
        grid.setItems(u);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.addColumn(Uzivatel::getMeno).setCaption("Meno").setId("meno");
        grid.addColumn(Uzivatel::getTypKontaTextom).setCaption("Typ konta").setId("typ");
        grid.addColumn(Uzivatel::getFirmaNazov).setCaption("Firma").setId("nazovFirmy");

        grid.setColumnOrder(grid.getColumn("meno"),grid.getColumn("typ"),grid.getColumn("nazovFirmy"));
        SingleSelectionModel<Uzivatel> singleSelect =
                (SingleSelectionModel<Uzivatel>) grid.getSelectionModel();
        singleSelect.setDeselectAllowed(false);
        grid.addItemClickListener(event ->
                    nadstavDetail(event.getItem()));






        this.addComponent(new Label("Prehľad užívateľov"));

        HorizontalLayout prvy=new HorizontalLayout();





        Button btnSpat=new Button("Späť");


        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
                );
        prvy.addComponent(btnSpat);

        this.addComponent(prvy);
        HorizontalLayout druhy=new HorizontalLayout();

        GridLayout editacny=new GridLayout(2,4);
        Label lMeno=new Label("Meno:");
        tMeno=new TextField();
        tFirma=new TextField();
        Label lTyp=new Label("Typ:");
        Label lFirma=new Label("Firma:");

        List<String> typy = new ArrayList<>();
        typy.add("Predajca" );
        typy.add("Spávca" );



        cTyp=new ComboBox();
        cTyp.setTextInputAllowed(false);
        cTyp.setItems(typy);
        Button btnOk =new Button("Nový");
        btnOk.addClickListener(cliokEvent->
                nadstavDetail(null));

        Button btnAkoNovy =new Button("Ulož");
        btnAkoNovy.addClickListener(cliokEvent->
                uloz(true));

        editacny.addComponent(lMeno);
        editacny.addComponent(tMeno);
        editacny.addComponent(lTyp);
        editacny.addComponent(cTyp);
        editacny.addComponent(lFirma);
        editacny.addComponent(tFirma);
        editacny.addComponent(btnOk);
        editacny.addComponent(btnAkoNovy);
        druhy.addComponent(grid);
        druhy.addComponent(editacny);
        this.addComponent(druhy);


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


    private void uloz(boolean novy) {
        System.out.println("Novy");
        Uzivatel u=new Uzivatel(tMeno.getValue(),"");
        u.setTypKonta(1);
        UzivatelNastroje.ulozUzivatela(u);
        grid.getDataProvider().refreshAll();


    }

    private void nadstavDetail(Uzivatel item) {
        if (item!=null){
            tMeno.setValue(item.getMeno());
            tFirma.setValue(item.getFirmaNazov());
            cTyp.setValue(item.getTypKontaTextom());}
        else
            {
            tMeno.setValue("");
            tFirma.setValue("");
            cTyp.setValue("");}

    }


//    private void nadstavDetail(Uzivatel item) {
//
//    }


}
