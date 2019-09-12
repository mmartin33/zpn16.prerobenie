package sk.zpn.zaklad.view.statistiky;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.NumberRenderer;
import org.vaadin.addons.filteringgrid.FilterGrid;
import sk.zpn.domena.statistiky.ZoznamBodov;
import sk.zpn.zaklad.model.StatPoberatelNastroje;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.List;

public class StatPrePoberatelovView extends VerticalLayout implements View {// ContactForm is an example of a custom component class
    public static final String NAME = "StatPrePoberatelovView";
    private  List<ZoznamBodov >statistika;
    private Button btnAktivujFilter;
    private FilterGrid<ZoznamBodov> grid;
    private List<ZoznamBodov> statList =null;
    private GridLayout gl;
    private HorizontalLayout hl;
    public StatPrePoberatelovView(Navigator navigator) {

        gl =new GridLayout(1,2);
        hl =new HorizontalLayout();
        gl.setSpacing(false);
        gl.setSizeFull();
        gl.setColumnExpandRatio(0,1f);

        gl.setSpacing(false);

        gl.setRowExpandRatio(0, 0.10f);
        gl.setRowExpandRatio(1, 0.90f);
//        btnAktivujFilter=new Button("Zobraz detail");
//        btnAktivujFilter.setHeight(100, Unit.PERCENTAGE);
//        btnAktivujFilter.addClickListener(this::aktivujFilter);

        grid = new FilterGrid<>();



    }

//    private void aktivujFilter(Button.ClickEvent clickEvent) {
//        grid.setItems(this.statistika);
//        this.statistika= StatPoberatelNastroje.bodyZaPoberatela((Long)VaadinSession.getCurrent().getAttribute("id_uzivatela"));
//
//
//    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Double celkovyPocetBodov=StatPoberatelNastroje.bodyZaPoberatela((Long)VaadinSession.getCurrent().getAttribute("id_uzivatela"));
        this.statistika= StatPoberatelNastroje.zoznamBodovZaPoberatela((Long)VaadinSession.getCurrent().getAttribute("id_uzivatela"));
        String text;
        String meno=VaadinSession.getCurrent().getAttribute("meno").toString();
        text="<font size=\"4\" color=\"blue\">Poberateľ:</font> <br>";
        text=text+"<font size=\"4\" color=\"green\"> <b> "+meno+" <b> </font> <br>";
        text=text+"<font size=\"4\" color=\"blue\"> <b> konečný stav bodov:  <b> </font> ";
        text=text+"<font size=\"6\" color=\"green\"> <b> "+celkovyPocetBodov+" <b> </font>";

        Label popis=new Label(text,ContentMode.HTML);
        hl.addComponent(popis);
        //hl.addComponent(btnAktivujFilter);

        FilterGrid.Column<ZoznamBodov, String > colDatum = grid.addColumn(ZoznamBodov::getFormatovanyDatum).setCaption("Dátum").setId("datum");
        FilterGrid.Column<ZoznamBodov, String> colBody = grid.addColumn(ZoznamBodov::getHtmlBody).setCaption("Body").setId("body");
        FilterGrid.Column<ZoznamBodov, String> colTypDokladu = grid.addColumn(ZoznamBodov::getTypDokladu).setCaption("Typ dokladu").setId("typDokladu");
        FilterGrid.Column<ZoznamBodov, String> colPoznamka = grid.addColumn(ZoznamBodov::getPoznamka).setCaption("poznamka").setId("Poznamka");
        colBody.setRenderer(new HtmlRenderer());

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumnOrder(colDatum,colBody,colTypDokladu,colPoznamka);

        grid.setItems(this.statistika);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setSizeFull();

        gl.addComponent(hl);
        gl.setComponentAlignment(hl,Alignment.TOP_LEFT);
        gl.addComponent(grid);
        gl.setComponentAlignment(grid,Alignment.MIDDLE_LEFT);

        this.setSizeFull();
        this.addComponentsAndExpand(gl);


        //this.addComponent(tlacitkovy);
        //this.setComponentAlignment(tlacitkovy, Alignment.BOTTOM_LEFT);
        gl.setVisible(true);

        this.addComponentsAndExpand(gl);
    }








}

