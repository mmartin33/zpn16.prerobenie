package sk.zpn.zaklad.view.parametre;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import sk.zpn.domena.Parametre;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.view.MojView;
import sk.zpn.zaklad.view.VitajteView;

import java.util.Optional;

public class ParametreView extends MojView {
    public static final String NAME = "ParametreView";
    Button btnUloz;
    Button btnSpat;
    TextField tRok;
    Parametre p;
    Binder<Parametre> binder = new Binder<>();

    public ParametreView() {
        tRok = new TextField("Rok:");
        FormLayout lEdit = new FormLayout();
        lEdit.addComponent(tRok);
        HorizontalLayout lBtn = new HorizontalLayout();
        btnUloz = new Button("Ulož");
        btnSpat = new Button("Späť");
        lBtn.addComponent(btnUloz);
        lBtn.addComponent(btnSpat);

        this.addComponent(lEdit);
        this.addComponent(lBtn);
        nastavComponnenty();
    }

    private void nastavComponnenty() {
        Binder.Binding<Parametre, String> rokBinding = binder.forField(tRok)
                .withValidator(v -> !tRok.getValue().trim().isEmpty(),
                        "Rok je povinny")
                .bind(Parametre::getRok, Parametre::setRok);


        tRok.addValueChangeListener(event -> rokBinding.validate());

        btnUloz.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnUloz.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        btnUloz.addClickListener(this::save);
        btnSpat.addClickListener(this::spat);


    }

    private void spat(Button.ClickEvent clickEvent) {


        UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME) ;
    }

    private void save(Button.ClickEvent clickEvent) {
        System.out.println("Ulozenie parametrov");
        binder.writeBeanIfValid(p);
        ParametreNastroje.ulozParametre(p);
        UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME);

    }

    @Override
    protected void init() {

        p=ParametreNastroje.nacitajParametre();
        binder.readBean(p);
    }
}
