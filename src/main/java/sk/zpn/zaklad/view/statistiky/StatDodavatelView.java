package sk.zpn.zaklad.view.statistiky;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.NumberRenderer;
import org.apache.commons.lang.StringUtils;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import sk.zpn.domena.StatistikaBodov;
import sk.zpn.nastroje.XlsStatistikaBodov;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.model.StatDodavatelNastroje;
import sk.zpn.zaklad.model.StatPoberatelNastroje;
import sk.zpn.zaklad.view.VitajteView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

public class StatDodavatelView extends VerticalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "statDodavatelView";
    private Button btnAktivujFilter;
    private FilterGrid<StatistikaBodov> grid;
    private Button btnSpat;
    private List<StatistikaBodov> statList =null;
    DateField dfOd;
    DateField dfDo;
    TextField txtRok;
    LocalDate dod;
    LocalDate ddo;
    String rok;
    public StatDodavatelView() {

        HorizontalLayout hornyFilter =new HorizontalLayout();


        rok= ParametreNastroje.nacitajParametre().getRok();
        dod = LocalDate.of(LocalDate.now().getYear(),1,1);
        ddo = LocalDate.of(LocalDate.now().getYear(),12,31);
        dfOd=new DateField("Od:");
        dfDo=new DateField("do:");
        txtRok=new TextField("Rok:");
        txtRok.setValue(rok);
        dfOd.setValue(dod);
        dfDo.setValue(ddo);
        dfOd.setWidth(15, Unit.PERCENTAGE);
        dfDo.setWidth(15, Unit.PERCENTAGE);
        txtRok.setWidth(15, Unit.PERCENTAGE);

        btnAktivujFilter=new Button("Prezobraz");
        btnAktivujFilter.setWidth(10, Unit.PERCENTAGE);
        btnAktivujFilter.setHeight(80, Unit.PERCENTAGE);
        btnAktivujFilter.addClickListener(this::aktivujFilter);
        hornyFilter.addComponent(dfOd);
        hornyFilter.addComponent(dfDo);
        hornyFilter.addComponent(txtRok);
        hornyFilter.addComponent(btnAktivujFilter);


        GridLayout gl =new GridLayout(1,1);
        gl.setSpacing(false);
        gl.setSizeFull();
        gl.setColumnExpandRatio(0,1f);
        gl.setRowExpandRatio(0, 1f);


        DecimalFormat df = new DecimalFormat("#,###.00");

        grid = new FilterGrid<>();

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        //grid.setSelectionMode(Grid.SelectionMode.MULTI);

        FilterGrid.Column<StatistikaBodov, String> colDodavatel= grid.addColumn(StatistikaBodov::getNazov).setCaption("Dodávateľ").setId("nazov");
        FilterGrid.Column<StatistikaBodov, BigDecimal> colPS= grid.addColumn(StatistikaBodov::getPociatocnyStav).setCaption("Počiatočný stav").setId("ps");
        FilterGrid.Column<StatistikaBodov, BigDecimal> colbodyPredaj= grid.addColumn(StatistikaBodov::getBodyZaPredaj).setCaption("Body za predaj").setId("predaj");
                FilterGrid.Column<StatistikaBodov, BigDecimal> colKS= grid.addColumn(StatistikaBodov::getKonecnyStav).setCaption("Konečný stav").setId("ks");


        // filters

        colDodavatel.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colPS.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colPS.setRenderer(new NumberRenderer(new DecimalFormat("#,###")));
        colbodyPredaj.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colbodyPredaj.setRenderer(new NumberRenderer(new DecimalFormat("#,###")));
        colKS.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colKS.setRenderer(new NumberRenderer(new DecimalFormat("#,###")));



        grid.setColumnOrder(colDodavatel,colPS,colbodyPredaj,colKS);



        this.addComponent(new Label("Dodávatelia "));
        this.addComponent(hornyFilter);
        gl.addComponents(grid);
        gl.setComponentAlignment(grid,Alignment.MIDDLE_LEFT);





        Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );
        Button btnXLS = new Button("Do Excelu", VaadinIcons.TABLE);
        btnXLS.addClickListener(clickEvent ->
                {
                    SimpleDateFormat formatter= new SimpleDateFormat("dd.MM.yyyy");


                    String nadpis="Vyhodnotenie dodavateľov od: "+(dfOd.getValue())+" dp: "+ (dfDo.getValue());


//                    if (grid.getSelectedItems().size()<=0)
                        XlsStatistikaBodov.vytvorXLS(statList,nadpis);
//                    else {
//                        List<StatPoberatel> vybrane =new ArrayList<StatPoberatel>(  );
//                        vybrane.addAll(grid.getSelectedItems());
//                        XlsStatPoberatel.vytvorXLS(vybrane, nadpis);
//                    }






                }
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
        //btnAktivujFilter.setEnabled(false);
        }

    private void aktivujFilter(){
        if (statList!=null)
            statList.clear();
        statList = StatDodavatelNastroje.load(dfOd.getValue(), dfDo.getValue(),Integer.parseInt(txtRok.getValue()));
        grid.setItems(statList);
    }



    private void configureComponents() {
//        aktivujFilter();
//        grid.setItems(statList);
//        grid.getDataProvider().refreshAll();
    }




}

