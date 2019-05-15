package sk.zpn.zaklad.view.statistiky;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import jdk.nashorn.internal.objects.NativeDate;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import sk.zpn.domena.StatPoberatel;
import sk.zpn.zaklad.model.StatPoberatelNastroje;
import sk.zpn.zaklad.view.VitajteView;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatPoberatelView extends VerticalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "statPoberatelView";
    private final Button btnAktivujFilter;
    private FilterGrid<StatPoberatel> grid;
    private Button btnSpat;
    private List<StatPoberatel> statList =null;
    DateField dfOd;
    DateField dfDo;
    Label lblOd;
    Label lblDo;
    public StatPoberatelView() {

        HorizontalLayout hornyFilter =new HorizontalLayout();
        lblOd=new Label("Dátum od:");
        lblDo=new Label("Dátum do:");

        Calendar calDate = null;
        calDate.set(Calendar.DAY_OF_YEAR, 1);
        Date yearStartDate = calDate.getTime();
        dfOd=new DateField().setValue();
        dfDo=new DateField();
        lblOd.setWidth(10, Sizeable.Unit.PERCENTAGE);
        lblDo.setWidth(10, Sizeable.Unit.PERCENTAGE);
        dfOd.setWidth(10, Sizeable.Unit.PERCENTAGE);
        dfDo.setWidth(10,Sizeable.Unit.PERCENTAGE);
        btnAktivujFilter=new Button("Prezobraz");
        btnAktivujFilter.setWidth(10,Sizeable.Unit.PERCENTAGE);
        hornyFilter.addComponent(lblOd);
        hornyFilter.addComponent(dfOd);
        hornyFilter.addComponent(lblDo);
        hornyFilter.addComponent(dfDo);


        GridLayout gl =new GridLayout(1,1);
        gl.setSpacing(false);
        gl.setSizeFull();
        gl.setColumnExpandRatio(0,1f);
        gl.setRowExpandRatio(0, 1f);

        statList = new  StatPoberatelNastroje().load();

        grid = new FilterGrid<>();
        grid.setItems(statList);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);


        FilterGrid.Column<StatPoberatel, String> colPoberatel= grid.addColumn(StatPoberatel::getPoberatelNazov).setCaption("Poberatel").setId("poberatel");
        FilterGrid.Column<StatPoberatel, BigDecimal> colPS= grid.addColumn(StatPoberatel::getPociatocnyStav).setCaption("Počiatočný stav").setId("ps");
        FilterGrid.Column<StatPoberatel, BigDecimal> colbodyPredaj= grid.addColumn(StatPoberatel::getBodyZaPredaj).setCaption("Body za predaj").setId("predaj");
        FilterGrid.Column<StatPoberatel, BigDecimal> colbodyIne= grid.addColumn(StatPoberatel::getBodyIne).setCaption("Body ine").setId("ine");
        FilterGrid.Column<StatPoberatel, BigDecimal> colKS= grid.addColumn(StatPoberatel::getKonecnyStav).setCaption("Konečný stav").setId("ks");


        // filters


        colPoberatel.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colPS.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colbodyPredaj.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colbodyIne.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colKS.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());



        grid.setColumnOrder(colPoberatel,colPS,colbodyPredaj,colbodyIne,colKS);



        this.addComponent(new Label("Poberatelia "));
        this.addComponent(hornyFilter);
        gl.addComponents(grid);
        gl.setComponentAlignment(grid,Alignment.MIDDLE_LEFT);





        Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );
        Button btnXLS = new Button("Do Excelu", VaadinIcons.TABLE);
        btnSpat.addClickListener(clickEvent ->
                StatPoberatelNastroje.exportDoXLS(statList)
        );


        HorizontalLayout tlacitkovy = new HorizontalLayout();
        btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        tlacitkovy.addComponent(btnSpat);
        tlacitkovy.addComponent(btnXLS);



        grid.setSizeFull();
        this.setSizeFull();
        this.addComponentsAndExpand(gl);


        this.addComponent(tlacitkovy);
        this.setComponentAlignment(tlacitkovy, Alignment.BOTTOM_LEFT);
        gl.setVisible(true);

        this.addComponentsAndExpand(gl);
        configureComponents();



    }


    private void configureComponents() {
        grid.getDataProvider().refreshAll();
    }




}

