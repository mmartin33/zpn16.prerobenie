package sk.zpn.zaklad.view.statistiky;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.Produkt;
import sk.zpn.zaklad.model.FirmaNastroje;
import sk.zpn.zaklad.model.PoberatelNastroje;
import sk.zpn.zaklad.model.ProduktyNastroje;
import sk.zpn.zaklad.view.produkty.ProduktyView;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class StatPrePoberatelovZmenaPrihlasovacichUdajovEdit extends VerticalLayout implements View {

    public static final String NAME = "StatPrePoberatelovZmenaPrihlasovacichUdajovEdit";

    private TextField tfEmail;
    private TextField tfHeslo;
    private TextField tfHeslo2;
    private Poberatel poberatel;



    protected Button btnUloz;
    protected Button btnSpat;

    private final Binder<Poberatel> binder = new Binder<>();


    public StatPrePoberatelovZmenaPrihlasovacichUdajovEdit() {
        tfEmail = new TextField("email");
        tfEmail.setWidth("200");
        tfHeslo = new TextField("Heslo");
        tfHeslo.setWidth("200");

        btnUloz=new Button("Ulož", VaadinIcons.CHECK_CIRCLE);
        btnUloz.setClickShortcut(ShortcutAction.KeyCode.U,
                new int[]{ShortcutAction.ModifierKey.ALT});

        btnSpat =new Button("Späť",VaadinIcons.EJECT);

        nastavComponnenty();
        FormLayout lEdit = new FormLayout();

        lEdit.addComponent(tfEmail);
        lEdit.addComponent(tfHeslo);


        HorizontalLayout lBtn = new HorizontalLayout();
        lBtn.addComponent(btnUloz);
        lBtn.addComponent(btnSpat);


        this.addComponent(lEdit);
        this.addComponent(lBtn);

    }

    private void nastavComponnenty() {

        binder.readBean(this.poberatel);
        Binder.Binding<Poberatel, String> emailBinding = binder.forField(tfEmail)
                .bind(Poberatel::getEmail, Poberatel::setEmail);
        Binder.Binding<Poberatel, String> hesloBinding = binder.forField(tfHeslo)
                .bind(Poberatel::getHeslo, Poberatel::setHeslo);


        tfEmail.addValueChangeListener(event -> emailBinding.validate());
        tfHeslo.addValueChangeListener(event -> hesloBinding.validate());


        //btnUloz.setStyleName(ValoTheme.BUTTON_PRIMARY);

        btnUloz.addClickListener(this::save);
        btnSpat.addClickListener(this::delete);


        setVisible(true);


    }



    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        binder.readBean(poberatel);
    }

    public void save(Button.ClickEvent event) {
        if (binder.writeBeanIfValid(poberatel)) {
            PoberatelNastroje.ulozPoberatela(poberatel);

            UI.getCurrent().getNavigator().navigateTo(StatPrePoberatelovView.NAME);


        }

    }

    public void delete(Button.ClickEvent event) {
        UI.getCurrent().getNavigator().navigateTo(StatPrePoberatelovView.NAME);
    }

    public void setPoberatel(Poberatel poberatel) {
        this.poberatel = poberatel;
    }
}

