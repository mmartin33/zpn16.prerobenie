package sk.zpn.zaklad.view.uzivatel;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.*;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.renderers.DateRenderer;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.model.UzivatelNastroje;
import sk.zpn.zaklad.view.VitajteView;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private Grid<Uzivatel> grid;
    private TextField filter;
    public Button btnNovy;


        public BrowsPanel() {

            grid = new Grid<>();
            List<Uzivatel> u = UzivatelNastroje.zoznamUzivatelov();
            grid.setItems(u);
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);

            grid.addColumn(Uzivatel::getMeno).setCaption("Meno").setId("meno");
            grid.addColumn(Uzivatel::getTypKontaTextom).setCaption("Typ konta").setId("typ");
            grid.addColumn(Uzivatel::getFirmaNazov).setCaption("Firma").setId("nazovFirmy");

            grid.setColumnOrder(grid.getColumn("meno"),grid.getColumn("typ"),grid.getColumn("nazovFirmy"));
            this.addComponent(new Label("Prehľad užívateľov"));
            filter=new TextField();
            this.addComponent(filter);
            HorizontalLayout prvy=new HorizontalLayout();

            Button btnSpat=new Button("Späť");


            btnSpat.addClickListener(clickEvent ->
                    UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
            );
            prvy.addComponent(btnSpat);

            this.addComponent(prvy);
            VerticalLayout druhy=new VerticalLayout();
            btnNovy=new Button("Novy");

            druhy.addComponentsAndExpand(grid);
            druhy.addComponent(btnNovy);
            this.addComponent(druhy);



        }

        void refresh(String filter) {
            grid.getDataProvider().refreshAll();
            System.out.println("Refresh browsu all");
            //todo grid.setItems(ContactService.getDemoService().findAll(filter));
        }

        void refresh() {
            grid.getDataProvider().refreshAll();
            System.out.println("Refresh browsu all");
            refresh(getFilterValue());
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

        String getFilterValue() {
            return filter.getValue();
        }

    void addEditListener(Runnable editListener) {
        btnNovy.addClickListener(e -> editListener.run());

    }

    void addFilterListener(Consumer<String> listener) {
        filter.addValueChangeListener(e -> listener.accept(e.getValue()));
    }

    }






