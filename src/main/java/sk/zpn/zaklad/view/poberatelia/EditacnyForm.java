package sk.zpn.zaklad.view.poberatelia;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.Poberatel;
import sk.zpn.zaklad.model.PoberatelNastroje;

import java.time.ZoneId;
import java.util.Date;

public class EditacnyForm extends VerticalLayout {


    private TextField tMeno;
//    private TextField tPriezvisko;
    private TextField tTitul;
    private DateField dVyznamnyDatum;
    private TextField tMesto;
    private TextField tUlica;
    private TextField tPsc;
    private TextField tMobil;
    private TextField tTelefon;
    private TextField tEmail;
    private TextField tKod;
    private TextField tHeslo;



    protected Button btnUloz;
    protected Button btnZmaz;

    private final Binder<Poberatel> binder = new Binder<>();
    private Poberatel poberatelEditovany;
    private PoberateliaView poberateliaView;


    public EditacnyForm() {
        tMeno = new TextField("Meno");
        tMeno.setWidth("400");
//        tPriezvisko = new TextField("Priezvisko");
//        tPriezvisko.setWidth("400");
        tTitul = new TextField("Titul");
        tTitul.setWidth("200");
        tUlica = new TextField("Ulica");
        tUlica.setWidth("400");
        tMesto = new TextField("Mesto");
        tMeno.setWidth("400");
        tPsc = new TextField("PSČ");
        tPsc.setWidth("150");
        tMobil = new TextField("Mobil");
        tTelefon = new TextField("Telefon");
        tEmail = new TextField("Email");

        dVyznamnyDatum = new DateField("Výynamny dátum");
        dVyznamnyDatum.setDateFormat("dd.MM.yyyy");
        tKod = new TextField("Kód");
        tKod.setWidth("300");
        tHeslo = new TextField("Heslo");
        tHeslo.setWidth("300");


        btnUloz=new Button("Ulož", VaadinIcons.CHECK_CIRCLE);
        btnUloz.setClickShortcut(ShortcutAction.KeyCode.U,
                new int[]{ShortcutAction.ModifierKey.ALT});

        btnZmaz =new Button("Zmaž",VaadinIcons.CLOSE_CIRCLE);

        nastavComponnenty();
        FormLayout lEdit = new FormLayout();
        lEdit.addComponent(tMeno);
//        lEdit.addComponent(tPriezvisko);
        lEdit.addComponent(tTitul);
        lEdit.addComponent(tUlica);
        lEdit.addComponent(tMesto);
        lEdit.addComponent(tPsc);
        lEdit.addComponent(tMobil);
        lEdit.addComponent(tTelefon);
        lEdit.addComponent(tEmail);
        lEdit.addComponent(dVyznamnyDatum);
        lEdit.addComponent(tKod);
        lEdit.addComponent(tHeslo);



        HorizontalLayout lBtn = new HorizontalLayout();
        lBtn.addComponent(btnUloz);
        lBtn.addComponent(btnZmaz);



        this.addComponent(lEdit);
        this.addComponent(lBtn);

    }

    private void nastavComponnenty() {


        Binder.Binding<Poberatel, String> menoBinding = binder.forField(tMeno)
                .bind(Poberatel::getMeno, Poberatel::setMeno);
//        Binder.Binding<Poberatel, String> priezviskoBinding = binder.forField(tPriezvisko)
//                .bind(Poberatel::getPriezvisko, Poberatel::setPriezvisko);
        Binder.Binding<Poberatel, String> titulBinding = binder.forField(tTitul)
                .bind(Poberatel::getTitul, Poberatel::setTitul);
        Binder.Binding<Poberatel, String> mestoBinding = binder.forField(tMesto)
                .bind(Poberatel::getMesto, Poberatel::setMesto);
        Binder.Binding<Poberatel, String> ulicaBinding = binder.forField(tUlica)
                .bind(Poberatel::getUlica, Poberatel::setUlica);
        Binder.Binding<Poberatel, String> pscBinding = binder.forField(tPsc)
                .bind(Poberatel::getPsc, Poberatel::setPsc);
        Binder.Binding<Poberatel, String> mobilBinding = binder.forField(tMobil)
                .bind(Poberatel::getMobil, Poberatel::setMobil);
        Binder.Binding<Poberatel, String> telefonBinding = binder.forField(tTelefon)
                .bind(Poberatel::getTelefon, Poberatel::setTelefon);
        Binder.Binding<Poberatel, String> emailBinding = binder.forField(tEmail)
                .bind(Poberatel::getEmail, Poberatel::setEmail);
        Binder.Binding<Poberatel, Date> vyznamnyDatumBinding = binder.forField(dVyznamnyDatum)
                .withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .bind(Poberatel::getVyznamnyDatum, Poberatel::setVyznamnyDatum);

        Binder.Binding<Poberatel, String> kodBinding = binder.forField(tKod)
                .bind(Poberatel::getKod, Poberatel::setKod);
        Binder.Binding<Poberatel, String> hesloBinding = binder.forField(tHeslo)
                .bind(Poberatel::getHeslo, Poberatel::setHeslo);


        tMeno.addValueChangeListener(event -> menoBinding.validate());
//        tPriezvisko.addValueChangeListener(event -> priezviskoBinding.validate());
        tMobil.addValueChangeListener(event -> mobilBinding.validate());
        tEmail.addValueChangeListener(event -> emailBinding.validate());
        tKod.addValueChangeListener(event -> menoBinding.validate());
        tHeslo.addValueChangeListener(event -> hesloBinding.validate());
        dVyznamnyDatum.addValueChangeListener(event -> vyznamnyDatumBinding.validate());
        tKod.addValueChangeListener(event -> kodBinding.validate());
        tTelefon.addValueChangeListener(event -> telefonBinding.validate());
        tUlica.addValueChangeListener(event -> ulicaBinding.validate());
        tPsc.addValueChangeListener(event -> pscBinding.validate());
        tMesto.addValueChangeListener(event -> mestoBinding.validate());

        btnUloz.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnUloz.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        btnUloz.addClickListener(this::save);
        btnZmaz.addClickListener(this::delete);


        //setVisible(false);


    }

    void edit(Poberatel poberatel) {
        poberatelEditovany = poberatel;
        if (poberatel != null) {
            System.out.println("Zvolená " + poberatelEditovany.getMeno());
            binder.readBean(poberatel);
        } else {
            System.out.println("Zvolená nová");
            binder.readBean(poberatel);
        }

    }


    public void save(Button.ClickEvent event) {
        if (binder==null)
            return;
        if (binder.writeBeanIfValid(poberatelEditovany)) {
            boolean jePoberatelNovy = poberatelEditovany.isNew();
            Poberatel ulozenyPoberatel = PoberatelNastroje.ulozPoberatela(poberatelEditovany);
            String msg = String.format("Ulozeny .",
                    poberatelEditovany.getMeno());

            Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
            if (jePoberatelNovy) {
                poberateliaView.pridajNovehoPoberatela(ulozenyPoberatel);
            }
            poberateliaView.refreshPoberatelov();

        }

    }

    public void delete(Button.ClickEvent event) {

        ConfirmDialog.show(UI.getCurrent(), "Odstránenie", "Naozaj si prajete odstrániť pobetatela " + poberatelEditovany.getMeno() + "?",
                "Áno", "Nie", new ConfirmDialog.Listener() {

                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue
                            PoberatelNastroje.zmazPoberatela(poberatelEditovany);
                            poberateliaView.odstranPoberatela(poberatelEditovany);
                            Notification.show("Poberateľ odstránený", Notification.Type.TRAY_NOTIFICATION);
                        }
                    }
                });

    }

    public void setPoberatelView(PoberateliaView poberatelView) {
        this.poberateliaView = poberatelView;
    }


    public Poberatel getPoberatelEditovany() {
        return poberatelEditovany;
    }

    public void rezimVelkoskladu() {
        btnUloz.setVisible(false);
        btnZmaz.setVisible(false);
        tMeno.setEnabled(false);
        tTitul.setEnabled(false);;
        dVyznamnyDatum.setEnabled(false);;
        tMesto.setEnabled(false);;
        tUlica.setEnabled(false);;
        tPsc.setEnabled(false);;
        tMobil.setEnabled(false);;
        tTelefon.setEnabled(false);;
        tEmail.setEnabled(false);;
        tKod.setEnabled(false);;
        tHeslo.setEnabled(false);;

    }
}

