package sk.zpn.zaklad.view.uzivatel;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.model.UzivatelNastroje;

import java.util.Arrays;

public class EditacnyForm extends VerticalLayout {


    private TextField tMeno;
    private TextField tFirma;
    private ComboBox<String> typUzivatelaComboBox;
    protected Button btnUloz;
    protected Button btnZmaz;
    private final Binder<Uzivatel> binder = new Binder<>();
    private Uzivatel uzivateEditovany;
    private UzivateliaView uzivatelView;
//    private PopupView odstranUzivatelaPopUp;

    public EditacnyForm(){
        tMeno=new TextField("Meno");
        tFirma=new TextField("Firma");
        typUzivatelaComboBox =new ComboBox<>("Typ konta");
        btnUloz=new Button("Ulož");
        btnZmaz =new Button("Zmaž");
        nastavComponnenty();
        FormLayout lEdit=new FormLayout();
        lEdit.addComponent(tMeno);
        lEdit.addComponent(tFirma);
        lEdit.addComponent(typUzivatelaComboBox);

        HorizontalLayout lBtn=new HorizontalLayout();
        lBtn.addComponent(btnUloz);
        lBtn.addComponent(btnZmaz);

        typUzivatelaComboBox.setItems(Arrays
                .stream(TypUzivatela.values())
                .map(TypUzivatela::getDisplayValue));


         this.addComponent(lEdit);
         this.addComponent(lBtn);

//        odstranUzivatelaPopUp = new PopupView(new RemoveUzivatelPopUpView());
//        this.addComponent(odstranUzivatelaPopUp);
    }
    private void nastavComponnenty(){


    Binder.Binding<Uzivatel, String> menoBinding = binder.forField(tMeno)
                .withValidator(v -> !tMeno.getValue().trim().isEmpty(),
                        "Meno je povinne")
                .bind(Uzivatel::getMeno, Uzivatel::setMeno);

    Binder.Binding<Uzivatel, String> typUzivatelaBinding = binder.forField(typUzivatelaComboBox)
            .bind(uzivatel -> uzivatel.getTypUzivatela().getDisplayValue(),
                    (uzivatel, value) -> uzivatel.setTypUzivatela(
                          TypUzivatela.fromDisplayName(value)));

    tMeno.addValueChangeListener(event -> menoBinding.validate());

    btnUloz.setStyleName(ValoTheme.BUTTON_PRIMARY);
    btnUloz.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    btnUloz.addClickListener(this::save);

    btnZmaz.addClickListener(this::delete);

    //setVisible(false);


}
    void edit(Uzivatel uzivatel) {
        uzivateEditovany = uzivatel;
        if (uzivatel != null) {
            System.out.println("Zvoleny "+uzivateEditovany.getMeno());
            binder.readBean(uzivatel);
        }
        else{
            System.out.println("Zvoleny novy");
            binder.readBean(uzivatel);}
        //setVisible(uzivatel != null);
    }

    public void save(Button.ClickEvent event) {
        if (binder.writeBeanIfValid(uzivateEditovany)) {
            boolean jeUzivatelNovy = uzivateEditovany.isNew();
            Uzivatel ulozenyUzivatel = UzivatelNastroje.ulozUzivatela(uzivateEditovany);
            String msg = String.format("Ulozeny .",
                    uzivateEditovany.getMeno());

            Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
            if (jeUzivatelNovy){
                uzivatelView.pridajNovehoUzivatela(ulozenyUzivatel);
            }
            uzivatelView.refreshUzivatelov();

        }

    }

    public void delete(Button.ClickEvent event) {
    // TODO dokoncit odstranovanie uzivatela
        ConfirmDialog.show(UI.getCurrent(), "Odstránenie uživateľa", "Naozaj si prajete odstrániť uživatela "+uzivateEditovany.getMeno()+"?",
                "Áno", "Nie", new ConfirmDialog.Listener() {

                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue
                            UzivatelNastroje.zmazUzivatela(uzivateEditovany);
                            uzivatelView.odstranUzivatela(uzivateEditovany);
                            Notification.show("Užívateľ odstránený", Notification.Type.TRAY_NOTIFICATION);
                        }
                    }
                });
        //todo getUI().getContent().deselect();
    }

    public void setUzivatelView(UzivateliaView uzivatelView) {
        this.uzivatelView = uzivatelView;
    }
}
