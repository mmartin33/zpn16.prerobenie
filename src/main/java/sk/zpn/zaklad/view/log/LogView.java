package sk.zpn.zaklad.view.log;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import sk.zpn.domena.TypDokladu;
import sk.zpn.domena.log.LogAplikacie;
import sk.zpn.domena.log.TypLogovanejHodnoty;
import sk.zpn.domena.log.TypUkonu;
import sk.zpn.zaklad.grafickeNastroje.MFilteredGrid;
import sk.zpn.zaklad.model.LogAplikacieNastroje;
import sk.zpn.zaklad.view.VitajteView;

import java.util.List;

public class LogView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "logView";
    private MFilteredGrid<LogAplikacie> grid =new MFilteredGrid<>();
    private Button btnSpat;
    private List<LogAplikacie> logList =null;
    GridLayout gr =new GridLayout(1,3);;
    HorizontalLayout lHorny = new HorizontalLayout();
    HorizontalLayout lTlacitkovy = new HorizontalLayout();

    public LogView() {


        gr.setSpacing(false);
        gr.setRowExpandRatio(0, 0.10f);
        gr.setRowExpandRatio(1, 0.80f);
        gr.setRowExpandRatio(2, 0.10f);
        gr.setSizeFull();




    }



    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setSizeFull();

        logList = LogAplikacieNastroje.zoznam();
        grid.setItems(logList);

        MFilteredGrid.Column<LogAplikacie, String> colKedy= grid.addColumn(LogAplikacie::getFormatovanyDatum).setCaption("Kedy").setId("kedy");
        MFilteredGrid.Column<LogAplikacie, String> colKto= grid.addColumn(LogAplikacie::getUzivatelMneno).setCaption("Kto").setId("kto");
        MFilteredGrid.Column<LogAplikacie, String> colTyp= grid.addColumn(typ->typ.getTypLogovanejHodnoty().getDisplayValue()).setCaption("Typ").setId("typ");
        MFilteredGrid.Column<LogAplikacie, String> colTypUkonu= grid.addColumn(typUkonu->typUkonu.getTypUkonu().getDisplayValue()).setCaption("TypUkonu").setId("typ_ukonu");
        MFilteredGrid.Column<LogAplikacie, String> colPoznamka= grid.addColumn(LogAplikacie::getPoznamka).setCaption("Poznámka").setId("poznamka");


        // filters
        colKedy.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colKto.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colTyp.setFilter(new ComboBox<>("", TypLogovanejHodnoty.getListOfDisplayValues()),
                (cValue, fValue) -> fValue == null || fValue.equals(cValue));
        colTypUkonu.setFilter(new ComboBox<>("", TypUkonu.getListOfDisplayValues()),
                (cValue, fValue) -> fValue == null || fValue.equals(cValue));
        colPoznamka.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        grid.registrujZmenuStlpcov("log");



        this.addComponent(gr);
        this.setSizeFull();

        Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );

        btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        lTlacitkovy.addComponent(btnSpat);//666

        Label nadpis=new Label("Log udalosti");
        lHorny.addComponent(nadpis);
        gr.addComponent(lHorny);
        gr.addComponent(grid);
        gr.addComponent(lTlacitkovy);

        gr.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
        gr.setComponentAlignment(lTlacitkovy, Alignment.MIDDLE_CENTER);
        gr.setVisible(true);
        grid.setSizeFull();
        this.setSizeFull();
        grid.getDataProvider().refreshAll();

    }
}

