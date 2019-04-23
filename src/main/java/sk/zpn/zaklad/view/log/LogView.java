package sk.zpn.zaklad.view.log;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import sk.zpn.domena.Doklad;
import sk.zpn.domena.LogPrihlasenia;
import sk.zpn.domena.StavDokladu;
import sk.zpn.zaklad.model.DokladyNastroje;
import sk.zpn.zaklad.model.LogPrihlaseniaNastroje;
import sk.zpn.zaklad.view.VitajteView;
import sk.zpn.zaklad.view.doklady.BrowsPanel;
import sk.zpn.zaklad.view.doklady.EditacnyForm;

import java.util.List;

public class LogView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "logView";
    private FilterGrid<LogPrihlasenia> grid;
    private Button btnSpat;
    private List<LogPrihlasenia> logList =null;

    public LogView() {
        GridLayout gr=new GridLayout(1,3);
        gr.setSpacing(false);
        gr.setSizeFull();
        gr.setRowExpandRatio(1,0.05f);
        gr.setRowExpandRatio(2,0.95f);
        gr.setRowExpandRatio(3,0.05f);
        logList = LogPrihlaseniaNastroje.zoznam();
        grid = new FilterGrid<>();
        grid.setItems(logList);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);


        FilterGrid.Column<LogPrihlasenia, String> colKedy= grid.addColumn(LogPrihlasenia::getKedyFormatovany).setCaption("Kedy").setId("kedy");
        FilterGrid.Column<LogPrihlasenia, String> colKto= grid.addColumn(LogPrihlasenia::getKtoMeno).setCaption("KTo").setId("kto");

        // filters
        colKedy.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colKto.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());


        grid.setColumnOrder(colKedy,colKto);







        this.addComponent(gr);
        this.setSizeFull();

        Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );
        HorizontalLayout tlacitkovy = new HorizontalLayout();
        btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        tlacitkovy.addComponent(btnSpat);//666

        gr.addComponent(new Label("Prehľad prihlásení"));

        gr.addComponent(grid);
        gr.setComponentAlignment(grid, Alignment.MIDDLE_LEFT);
        gr.addComponent(tlacitkovy);
        gr.setComponentAlignment(tlacitkovy, Alignment.BOTTOM_LEFT);
        gr.setVisible(true);
        configureComponents();



    }


    private void configureComponents() {
        grid.getDataProvider().refreshAll();
    }




}

