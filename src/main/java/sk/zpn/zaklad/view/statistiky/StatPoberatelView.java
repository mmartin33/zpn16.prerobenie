package sk.zpn.zaklad.view.statistiky;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import sk.zpn.domena.LogPrihlasenia;
import sk.zpn.domena.StatPoberatel;
import sk.zpn.zaklad.model.LogPrihlaseniaNastroje;
import sk.zpn.zaklad.model.StatPoberatelNastroje;
import sk.zpn.zaklad.view.VitajteView;

import java.util.List;

public class StatPoberatelView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "statPoberatelView";
    private FilterGrid<StatPoberatel> grid;
    private Button btnSpat;
    private List<StatPoberatel> statList =null;

    public StatPoberatelView() {
        GridLayout gr=new GridLayout(1,3);
        gr.setSpacing(false);
        gr.setSizeFull();
        gr.setRowExpandRatio(1,0.05f);
        gr.setRowExpandRatio(2,0.95f);
        gr.setRowExpandRatio(3,0.05f);
        statList = new  StatPoberatelNastroje().load();
        grid = new FilterGrid<>();
        grid.setItems(statList);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);


        FilterGrid.Column<StatPoberatel, String> colPoberatel= grid.addColumn(StatPoberatel::getPoberatelNazov).setCaption("Poberatel").setId("poberatel");


        // filters

        colPoberatel.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());


        grid.setColumnOrder(colPoberatel);







        this.addComponent(gr);
        this.setSizeFull();

        Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );
        HorizontalLayout tlacitkovy = new HorizontalLayout();
        btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        tlacitkovy.addComponent(btnSpat);//666

        gr.addComponent(new Label("Poberatelia "));

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

