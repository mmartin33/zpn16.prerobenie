package sk.zpn.zaklad.view.statistiky;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
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

public class StatPoberatelView extends VerticalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "statPoberatelView";
    private Button btnAktivujFilter;

    private Button btnSpat;
    private List<StatistikaBodov> statList =null;
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
        btnAktivujFilter=new Button("Do Excelu");
        btnAktivujFilter.setWidth(10,Sizeable.Unit.PERCENTAGE);
        btnAktivujFilter.setHeight(80,Sizeable.Unit.PERCENTAGE);

        btnSpat=new Button("Späť");
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );

        btnSpat.setWidth(10,Sizeable.Unit.PERCENTAGE);
        btnSpat.setHeight(80,Sizeable.Unit.PERCENTAGE);

        btnAktivujFilter.addClickListener(this::aktivujFilter);
        hornyFilter.addComponent(dfOd);
        hornyFilter.addComponent(dfDo);
        hornyFilter.addComponent(btnAktivujFilter);
        hornyFilter.addComponent(btnSpat);


        GridLayout gl =new GridLayout(1,1);
        gl.setSpacing(false);
        gl.setSizeFull();
        gl.setColumnExpandRatio(0,1f);
        gl.setRowExpandRatio(0, 1f);

        this.addComponent(new Label("Poberatelia "));
        this.addComponent(hornyFilter);


        this.setSizeFull();
        this.addComponentsAndExpand(gl);


        gl.setVisible(true);

        this.addComponentsAndExpand(gl);

    }

    private void aktivujFilter(Button.ClickEvent clickEvent) {
        aktivujFilter2();
        //btnAktivujFilter.setEnabled(false);
        }

    private void aktivujFilter(){
        //povodne
        if (statList!=null)
            statList.clear();
        statList = StatPoberatelNastroje.load(dfOd.getValue(), dfDo.getValue());

    }

    private void aktivujFilter2(){
        StatPoberatelNastroje.load2(dfOd.getValue(), dfDo.getValue());
    }






}

