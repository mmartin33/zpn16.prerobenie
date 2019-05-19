package sk.zpn.zaklad.view.statistiky;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import sk.zpn.domena.StatPoberatel;
import sk.zpn.zaklad.model.StatPoberatelNastroje;
import sk.zpn.zaklad.view.VitajteView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class StatPoberatelView extends VerticalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "statPoberatelView";
    private Button btnAktivujFilter;
    private FilterGrid<StatPoberatel> grid;
    private Button btnSpat;
    private List<StatPoberatel> statList =null;
    DateField dfOd;
    DateField dfDo;
    LocalDate dod;
    LocalDate ddo;
    public StatPoberatelView() {

        HorizontalLayout hornyFilter =new HorizontalLayout();



        dod = LocalDate.of(LocalDate.now().getYear(),1,1);
        ddo = LocalDate.of(LocalDate.now().getYear(),12,31);
        dfOd=new DateField("Od:");
        dfDo=new DateField("do:");
        dfOd.setValue(dod);
        dfDo.setValue(ddo);
        dfOd.setWidth(15, Sizeable.Unit.PERCENTAGE);
        dfDo.setWidth(15,Sizeable.Unit.PERCENTAGE);
        btnAktivujFilter=new Button("Prezobraz");
        btnAktivujFilter.setWidth(10,Sizeable.Unit.PERCENTAGE);
        btnAktivujFilter.setHeight(80,Sizeable.Unit.PERCENTAGE);
        btnAktivujFilter.addClickListener(this::aktivujFilter);
        hornyFilter.addComponent(dfOd);
        hornyFilter.addComponent(dfDo);
        hornyFilter.addComponent(btnAktivujFilter);


        GridLayout gl =new GridLayout(1,1);
        gl.setSpacing(false);
        gl.setSizeFull();
        gl.setColumnExpandRatio(0,1f);
        gl.setRowExpandRatio(0, 1f);



        grid = new FilterGrid<>();

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

    private void aktivujFilter(Button.ClickEvent clickEvent) {
        aktivujFilter();
        btnAktivujFilter.setEnabled(false);
        }

    private void aktivujFilter(){
//        statList.clear();
        statList = StatPoberatelNastroje.load(dfDo.getValue(), dfDo.getValue());
        grid.setItems(statList);
    }



    private void configureComponents() {
//        aktivujFilter();
//        grid.setItems(statList);
//        grid.getDataProvider().refreshAll();
    }




}

