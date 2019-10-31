package sk.zpn.zaklad.view.statistiky;

import com.vaadin.data.ValueProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import sk.zpn.domena.*;

import sk.zpn.zaklad.model.PolozkaDokladuNastroje;
import sk.zpn.zaklad.model.StatPoberatelNastroje;

import java.math.BigInteger;
import java.util.List;

public class StatPohybyBodovPoberatelovView extends VerticalLayout implements View {// ContactForm is an example of a custom component class
    public static final String NAME = "StatPohybyBodovPoberatelovView";
    private List<Object[]> statistika;
    private Button btnSpat;
    private FilterGrid<Object[]> grid;
    private Poberatel poberatel;
    private GridLayout gl;
    private HorizontalLayout hl;
    private String rodicovskyView;
    Label popis;

    public StatPohybyBodovPoberatelovView() {

        gl = new GridLayout(1, 2);
        hl = new HorizontalLayout();

        popis = new Label("", ContentMode.HTML);
        hl.addComponent(popis);
        btnSpat = new Button("Späť");
        btnSpat.setHeight(100, Unit.PERCENTAGE);
        btnSpat.addClickListener(this::spat);
        hl.addComponent(btnSpat);

        gl.setSpacing(false);
        gl.setSizeFull();
        gl.setColumnExpandRatio(0, 1f);

        gl.setSpacing(false);

        gl.setRowExpandRatio(0, 0.10f);
        gl.setRowExpandRatio(1, 0.90f);

        grid = new FilterGrid<>();
        gl.addComponent(hl);
        gl.setComponentAlignment(hl, Alignment.TOP_LEFT);
        gl.addComponent(grid);


    }

    private void spat(Button.ClickEvent clickEvent) {
        if (this.getRodicovskyView() != null)
            UI.getCurrent().getNavigator().navigateTo(this.getRodicovskyView());
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Double celkovyPocetBodov = StatPoberatelNastroje.bodyZaPoberatela(this.poberatel.getId());
        this.statistika = PolozkaDokladuNastroje.zoznamPohybovZaPoberatela(this.poberatel.getId());
        String text;
        String meno = this.poberatel.getPoberatelMenoAdresa();
        text = "<font size=\"4\" color=\"blue\">Poberateľ:</font> <br>";
        text = text + "<font size=\"4\" color=\"green\"> <b> " + meno + " <b> </font> <br>";
        text = text + "<font size=\"4\" color=\"blue\"> <b> konečný stav bodov:  <b> </font> ";
        text = text + "<font size=\"6\" color=\"green\"> <b> " + celkovyPocetBodov + " <b> </font>";

        popis.setValue(text);


        ValueProvider<Object[], String> pCislo = new ValueProvider<Object[], String>() {
            @Override
            public String apply(Object[] objects) {
                return ((Doklad) objects[1]).getCisloDokladu();
            }
        };
        FilterGrid.Column<Object[], String> colCisloDokladu = grid.addColumn(pCislo).setCaption("Číslo dokladu").setId("cisloDokladu");

        ValueProvider<Object[], String> pDatum = new ValueProvider<Object[], String>() {
            @Override
            public String apply(Object[] objects) {
                return ((Doklad) objects[1]).getFormatovanyDatum();
            }
        };
        FilterGrid.Column<Object[], String> colDatum = grid.addColumn(pDatum).setCaption("Dátum").setId("datum");

        ValueProvider<Object[], BigInteger> pBody = new ValueProvider<Object[], BigInteger>() {
            @Override
            public BigInteger apply(Object[] objects) {
                return ((PolozkaDokladu) objects[0]).getBodyBigInteger();
            }
        };
        FilterGrid.Column<Object[], BigInteger> colBody = grid.addColumn(pBody).setCaption("Body").setId("body");


        ValueProvider<Object[], String> pStav = new ValueProvider<Object[], String>() {
            @Override
            public String apply(Object[] objects) {
                return ((Doklad) objects[1]).getStavDokladu().getDisplayValue();
            }
        };
        FilterGrid.Column<Object[], String> colStavDokladu = grid.addColumn(pStav).setCaption("Stav dokladu").setId("Stav dokladu");

        ValueProvider<Object[], String> pTypDokl = new ValueProvider<Object[], String>() {
            @Override
            public String apply(Object[] objects) {
                return ((Doklad) objects[1]).getTypDokladu().getDisplayValue();
            }
        };
        FilterGrid.Column<Object[], String> colTypDokladu = grid.addColumn(pTypDokl).setCaption("Typ dokladu").setId("typDokladu");

        ValueProvider<Object[], String> pProdukt = new ValueProvider<Object[], String>() {
            @Override
            public String apply(Object[] objects) {
                if (objects[5]==null)
                    return "";


                return ((Produkt) objects[5]).getNazov();
            }
        };
        ValueProvider<Object[], String> pProduktKat = new ValueProvider<Object[], String>() {
            @Override
            public String apply(Object[] objects)
            {
                if (objects[5]==null)
                    return "";

                return ((Produkt) objects[5]).getKat();
            }
        };

        FilterGrid.Column<Object[], String> colProduktKAT = grid.addColumn(pProduktKat).setCaption("Produkt KAT").setId("produktKat");
        FilterGrid.Column<Object[], String> colProduktNazov = grid.addColumn(pProdukt).setCaption("Produkt nazov").setId("produktNazov");

        ValueProvider<Object[], String> pPrevadzka = new ValueProvider<Object[], String>() {
            @Override
            public String apply(Object[] objects) {
                if (objects[3]==null)
                    return "";
                return ((Prevadzka) objects[3]).getNazov();
            }
        };
        FilterGrid.Column<Object[], String> colPervadzka = grid.addColumn(pPrevadzka).setCaption("Prevadzka").setId("prevadzka");


        ValueProvider<Object[], String> pFirma = new ValueProvider<Object[], String>() {
            @Override
            public String apply(Object[] objects) {
                return ((Firma) objects[4]).getNazov();
            }
        };
        FilterGrid.Column<Object[], String> colFirma = grid.addColumn(pFirma).setCaption("Firma").setId("firma");



//        colBody.setRenderer(new HtmlRenderer());

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumnOrder(colCisloDokladu, colDatum, colBody, colTypDokladu, colProduktKAT,colProduktNazov, colPervadzka, colFirma);


        grid.setItems(this.statistika);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setSizeFull();


        grid.setSizeFull();
        gl.setComponentAlignment(grid, Alignment.MIDDLE_LEFT);

        this.setSizeFull();
        this.addComponentsAndExpand(gl);


        //this.addComponent(tlacitkovy);
        //this.setComponentAlignment(tlacitkovy, Alignment.BOTTOM_LEFT);
        gl.setVisible(true);

        this.addComponentsAndExpand(gl);
    }

    public Poberatel getPoberatel() {
        return poberatel;
    }

    public void setPoberatel(Poberatel poberatel) {
        this.poberatel = poberatel;
    }

    public void setRodicovskyView(String view) {
        this.rodicovskyView = view;
    }

    public String getRodicovskyView() {
        return rodicovskyView;
    }
}

