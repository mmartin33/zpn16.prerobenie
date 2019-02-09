package sk.zpn.zaklad.view.uzivatel;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.model.UzivatelNastroje;

public class EditacnyForm extends VerticalLayout {


    private TextField tMeno;
    private TextField tFirma;
    private ComboBox cTyp;
    protected Button btnUloz;
    protected Button btnCancel;
    private final Binder<Uzivatel> binder = new Binder<>();
    private Uzivatel uzivateEditovany;
    private UzivateliaView uzivatelView;


    public EditacnyForm(){
         tMeno=new TextField("Meno");
         tFirma=new TextField("Firma");
         cTyp=new ComboBox("Typ konta");
         btnUloz=new Button("Ulož");
         btnCancel=new Button("Storno");
         nastavComponnenty();
         FormLayout lEdit=new FormLayout();
         lEdit.addComponent(tMeno);
        lEdit.addComponent(tFirma);
        lEdit.addComponent(cTyp);

        HorizontalLayout lBtn=new HorizontalLayout();
        lBtn.addComponent(btnUloz);
        lBtn.addComponent(btnCancel);




         this.addComponent(lEdit);
         this.addComponent(lBtn);



    }
    private void nastavComponnenty(){


    final SerializablePredicate<String> menoPredicate = v -> !tMeno
            .getValue().trim().isEmpty();


    Binder.Binding<Uzivatel, String> menoBinding = binder.forField(tMeno)
                .withValidator(menoPredicate,
                        "Meno je povinne")
                .bind(Uzivatel::getMeno, Uzivatel::setMeno);



    tMeno.addValueChangeListener(event -> menoBinding.validate());

    btnUloz.setStyleName(ValoTheme.BUTTON_PRIMARY);
    btnUloz.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    btnUloz.addClickListener(this::save);

    btnCancel.addClickListener(this::cancel);

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

    public void cancel(Button.ClickEvent event) {
        Notification.show("Cancelled", Notification.Type.TRAY_NOTIFICATION);
        //todo getUI().getContent().deselect();
    }

    public void setUzivatelView(UzivateliaView uzivatelView) {
        this.uzivatelView = uzivatelView;
    }
}
