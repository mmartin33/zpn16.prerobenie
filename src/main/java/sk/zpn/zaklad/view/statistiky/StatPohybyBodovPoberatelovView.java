package sk.zpn.zaklad.view.statistiky;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.vaadin.addons.filteringgrid.FilterGrid;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.statistiky.ZoznamBodov;
import sk.zpn.domena.statistiky.ZoznamPohybov;
import sk.zpn.zaklad.model.PoberatelNastroje;
import sk.zpn.zaklad.model.StatPoberatelNastroje;
import sk.zpn.zaklad.view.poberatelia.PoberateliaView;

import java.math.BigInteger;
import java.util.List;

public class StatPohybyBodovPoberatelovView extends VerticalLayout implements View {// ContactForm is an example of a custom component class
    public static final String NAME = "StatPohybyBodovPoberatelovView";
    private  List<ZoznamPohybov>statistika;
    private Button btnSpat;
    private FilterGrid<ZoznamPohybov> grid;
    private Poberatel poberatel;
    private GridLayout gl;
    private HorizontalLayout hl;
    Label popis;
    public StatPohybyBodovPoberatelovView() {

        gl =new GridLayout(1,2);
        hl =new HorizontalLayout();

        popis=new Label("",ContentMode.HTML);
        hl.addComponent(popis);
        btnSpat =new Button("Späť");
        btnSpat.setHeight(100, Unit.PERCENTAGE);
        btnSpat.addClickListener(this::spat);
        hl.addComponent(btnSpat);

        gl.setSpacing(false);
        gl.setSizeFull();
        gl.setColumnExpandRatio(0,1f);

        gl.setSpacing(false);

        gl.setRowExpandRatio(0, 0.10f);
        gl.setRowExpandRatio(1, 0.90f);

        grid = new FilterGrid<>();
        gl.addComponent(hl);
        gl.setComponentAlignment(hl,Alignment.TOP_LEFT);
        gl.addComponent(grid);


    }

    private void spat(Button.ClickEvent clickEvent) {

        UI.getCurrent().getNavigator().navigateTo(PoberateliaView.NAME);

    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Double celkovyPocetBodov=StatPoberatelNastroje.bodyZaPoberatela(this.poberatel.getId());
        this.statistika= StatPoberatelNastroje.zoznamPohybovZaPoberatela(this.poberatel.getId());
        String text;
        String meno=VaadinSession.getCurrent().getAttribute("meno").toString();
        text="<font size=\"4\" color=\"blue\">Poberateľ:</font> <br>";
        text=text+"<font size=\"4\" color=\"green\"> <b> "+meno+" <b> </font> <br>";
        text=text+"<font size=\"4\" color=\"blue\"> <b> konečný stav bodov:  <b> </font> ";
        text=text+"<font size=\"6\" color=\"green\"> <b> "+celkovyPocetBodov+" <b> </font>";

        popis.setValue(text);


        FilterGrid.Column<ZoznamPohybov, String > colCisloDokladu = grid.addColumn(ZoznamPohybov::getDokladCislo).setCaption("|Číslo dokladu").setId("cisloDokladu");
        FilterGrid.Column<ZoznamPohybov, String > colDatum = grid.addColumn(ZoznamPohybov::getFromatovanyDatum).setCaption("Dátum").setId("datum");
        FilterGrid.Column<ZoznamPohybov, String> colBody = grid.addColumn(ZoznamPohybov::getHtmlBody).setCaption("Body").setId("body");
        FilterGrid.Column<ZoznamPohybov, String> colTypDokladu = grid.addColumn(ZoznamPohybov::getDokladTyp).setCaption("Typ dokladu").setId("typDokladu");
        FilterGrid.Column<ZoznamPohybov, String> colPoberatel = grid.addColumn(ZoznamPohybov::getPoberatelMeno).setCaption("Poberateľ").setId("poberatel");
        FilterGrid.Column<ZoznamPohybov, String> colPervadzka = grid.addColumn(ZoznamPohybov::getPrevadzkaPopisne).setCaption("Prevadzka").setId("prevadzka");
        FilterGrid.Column<ZoznamPohybov, String> colFirma = grid.addColumn(ZoznamPohybov::getFirmaNazov).setCaption("Firma").setId("firma");
        colBody.setRenderer(new HtmlRenderer());

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumnOrder(colCisloDokladu,colDatum,colBody,colTypDokladu,colPoberatel,colPervadzka,colFirma);

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

    public Poberatel getPoberatel() {
        return poberatel;
    }

    public void setPoberatel(Poberatel poberatel) {
        this.poberatel = poberatel;
    }
}

