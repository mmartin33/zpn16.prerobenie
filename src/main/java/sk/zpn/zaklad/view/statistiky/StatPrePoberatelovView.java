package sk.zpn.zaklad.view.statistiky;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.NumberRenderer;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import sk.zpn.domena.StatistikaBodov;
import sk.zpn.nastroje.XlsStatistikaBodov;
import sk.zpn.zaklad.model.StatPoberatelNastroje;
import sk.zpn.zaklad.view.VitajteView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

public class StatPrePoberatelovView extends VerticalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "StatPrePoberatelovView";
    private Button btnAktivujFilter;
    private List<StatistikaBodov> statList =null;
    public StatPrePoberatelovView() {

        HorizontalLayout hornyFilter =new HorizontalLayout();



//        dod = LocalDate.of(LocalDate.now().getYear(),1,1);
//        ddo = LocalDate.of(LocalDate.now().getYear(),12,31);
        btnAktivujFilter=new Button("Prezobraz");
        btnAktivujFilter.setWidth(10, Unit.PERCENTAGE);
        btnAktivujFilter.setHeight(80, Unit.PERCENTAGE);
//        hornyFilter.addComponent(dfOd);
//        hornyFilter.addComponent(dfDo);
        hornyFilter.addComponent(btnAktivujFilter);


        GridLayout gl =new GridLayout(1,1);
        gl.setSpacing(false);
        gl.setSizeFull();
        gl.setColumnExpandRatio(0,1f);
        gl.setRowExpandRatio(0, 1f);


        DecimalFormat df = new DecimalFormat("#,###.00");

        //grid.setSelectionMode(Grid.SelectionMode.MULTI);


        // filters







        this.addComponent(new Label("Poberatelia "));
        this.addComponent(hornyFilter);





        Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );


        HorizontalLayout tlacitkovy = new HorizontalLayout();
        btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        tlacitkovy.addComponent(btnSpat);



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
    }



    private void configureComponents() {
    }




}

