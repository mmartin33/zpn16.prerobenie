package sk.zpn.zaklad.view.statistiky;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import sk.zpn.domena.Firma;
import sk.zpn.zaklad.model.StatPoberatelNastroje;
import sk.zpn.zaklad.model.StatistikyNastroje;
import sk.zpn.zaklad.view.VitajteView;

import java.time.LocalDate;
import java.time.Year;
import java.util.Date;

public class StatPoRokochAVelkoskladochView extends VerticalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "StatPoRokochAVelkoskladochView";
    private Button btnAktivujFilter;

    private Button btnSpat;

    TextField tfOd;
    TextField  tfDo;
    int rokOd= Year.now().getValue();
    int rokDo= Year.now().getValue();

    public StatPoRokochAVelkoskladochView() {

        HorizontalLayout hornyFilter =new HorizontalLayout();



        rokDo= LocalDate.now().getYear();
        rokDo=rokOd;
        tfOd=new TextField("Od:");
        tfDo=new TextField("do:");
        tfOd.setValue(String.valueOf(rokOd));
        tfDo.setValue(String.valueOf(rokDo));
        tfOd.setWidth(5, Unit.PERCENTAGE);
        tfDo.setWidth(5, Unit.PERCENTAGE);
        btnAktivujFilter=new Button("Do Excelu");
        btnAktivujFilter.setWidth(10, Unit.PERCENTAGE);
        btnAktivujFilter.setHeight(80, Unit.PERCENTAGE);

        btnSpat=new Button("Späť");
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );

        btnSpat.setWidth(10, Unit.PERCENTAGE);
        btnSpat.setHeight(80, Unit.PERCENTAGE);

        btnAktivujFilter.addClickListener(this::aktivujFilter);
        hornyFilter.addComponent(tfOd);
        hornyFilter.addComponent(tfDo);
        hornyFilter.addComponent(btnAktivujFilter);
        hornyFilter.addComponent(btnSpat);


        GridLayout gl =new GridLayout(1,1);
        gl.setSpacing(false);
        gl.setSizeFull();
        gl.setColumnExpandRatio(0,1f);
        gl.setRowExpandRatio(0, 1f);

        this.addComponent(new Label("Vyhodnotenie velkoskladov po rokoch"));
        this.addComponent(hornyFilter);


        this.setSizeFull();
        this.addComponentsAndExpand(gl);


        gl.setVisible(true);

        this.addComponentsAndExpand(gl);

    }

    private void aktivujFilter(Button.ClickEvent clickEvent) {
        StatistikyNastroje.statPoRokochAVelkoskladoch(tfOd.getValue(), tfDo.getValue());

        }




}

