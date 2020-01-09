package sk.zpn.zaklad.view.statistiky;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;


import sk.zpn.domena.Poberatel;
import sk.zpn.domena.statistiky.ZoznamBodov;
import sk.zpn.zaklad.grafickeNastroje.MFilteredGrid;
import sk.zpn.zaklad.model.PoberatelNastroje;
import sk.zpn.zaklad.model.StatPoberatelNastroje;

import java.util.List;

public class StatPrePoberatelovView extends VerticalLayout implements View {// ContactForm is an example of a custom component class
    public static final String NAME = "StatPrePoberatelovView";
    private  List<ZoznamBodov >statistika;
    private Button btnZmenPrihlasovanie;
    private MFilteredGrid<ZoznamBodov> grid;
    private List<ZoznamBodov> statList =null;
    private GridLayout gl;
    private HorizontalLayout hl;
    Label popis;
    public StatPrePoberatelovView(Navigator navigator) {

        gl =new GridLayout(1,2);
        hl =new HorizontalLayout();

        popis=new Label("",ContentMode.HTML);
        hl.addComponent(popis);
        btnZmenPrihlasovanie=new Button("Zmena prihlasovacich údajov");
        btnZmenPrihlasovanie.setHeight(100, Unit.PERCENTAGE);
        btnZmenPrihlasovanie.addClickListener(this::zmenaPrihlasovacichUdajov);
        hl.addComponent(btnZmenPrihlasovanie);

        gl.setSpacing(false);
        gl.setSizeFull();
        gl.setColumnExpandRatio(0,1f);

        gl.setSpacing(false);

        gl.setRowExpandRatio(0, 0.10f);
        gl.setRowExpandRatio(1, 0.90f);

        grid = new MFilteredGrid<>();
        gl.addComponent(hl);
        gl.setComponentAlignment(hl,Alignment.TOP_LEFT);
        gl.addComponent(grid);


    }

    private void zmenaPrihlasovacichUdajov(Button.ClickEvent clickEvent) {
         Poberatel poberatel;
         Long id_poberatela=(Long)VaadinSession.getCurrent().getAttribute("id_uzivatela");
         poberatel=PoberatelNastroje.poberatelPodlaId(id_poberatela).get();
        StatPrePoberatelovZmenaPrihlasovacichUdajovEdit statPrePoberatelovZmenaPrihlasovacichUdajovEdit = new StatPrePoberatelovZmenaPrihlasovacichUdajovEdit();
        statPrePoberatelovZmenaPrihlasovacichUdajovEdit.setPoberatel(poberatel);
        UI.getCurrent().getNavigator().addView(statPrePoberatelovZmenaPrihlasovacichUdajovEdit.NAME, statPrePoberatelovZmenaPrihlasovacichUdajovEdit);
        UI.getCurrent().getNavigator().navigateTo(StatPrePoberatelovZmenaPrihlasovacichUdajovEdit.NAME);

    }


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

        popis.setValue(text);
        //hl.addComponent(btnAktivujFilter);

        MFilteredGrid.Column<ZoznamBodov, String > colDatum = grid.addColumn(ZoznamBodov::getFormatovanyDatum).setCaption("Dátum").setId("datum");
        MFilteredGrid.Column<ZoznamBodov, String> colBody = grid.addColumn(ZoznamBodov::getHtmlBody).setCaption("Body").setId("body");
        MFilteredGrid.Column<ZoznamBodov, String> colTypDokladu = grid.addColumn(ZoznamBodov::getTypDokladu).setCaption("Typ dokladu").setId("typDokladu");
        MFilteredGrid.Column<ZoznamBodov, String> colPoznamka = grid.addColumn(ZoznamBodov::getPoznamka).setCaption("poznamka").setId("Poznamka");
        colBody.setRenderer(new HtmlRenderer());

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumnOrder(colDatum,colBody,colTypDokladu,colPoznamka);
        grid.registrujZmenuStlpcov("statPrePoberatelov");
        grid.setItems(this.statistika);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setSizeFull();


        grid.setSizeFull();
        gl.setComponentAlignment(grid,Alignment.MIDDLE_LEFT);

        this.setSizeFull();
        this.addComponentsAndExpand(gl);


        //this.addComponent(tlacitkovy);
        //this.setComponentAlignment(tlacitkovy, Alignment.BOTTOM_LEFT);
        gl.setVisible(true);

        this.addComponentsAndExpand(gl);
    }








}

