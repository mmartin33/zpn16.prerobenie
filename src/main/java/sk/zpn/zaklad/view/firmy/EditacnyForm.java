package sk.zpn.zaklad.view.firmy;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.Firma;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.model.FirmaNastroje;
import sk.zpn.zaklad.model.UzivatelNastroje;

import java.util.Arrays;

public class EditacnyForm extends VerticalLayout {


    private TextField tMesto;
    private TextField tPsc;
    private TextField tTelefon;
    private TextField tUlica;
    private TextField tICO;
    private TextField tNazov;
    private TextField tDic;
    private TextField tIcDph;


    protected Button btnUloz;
    protected Button btnZmaz;
    private final Binder<Firma> binder = new Binder<>();
    private Firma firmaEditovana;
    private FirmyView firmyView;


    public EditacnyForm(){
        tICO=new TextField("IČO");
        tICO.setMaxLength(12);
        tNazov=new TextField("Názov");
        tNazov.setWidth("400");
        tDic=new TextField("Dič");
        tDic.setMaxLength(10);
        tIcDph=new TextField("ič DPH");
        tIcDph.setMaxLength(12);
        tUlica=new TextField("Ulica");
        tUlica.setWidth("400");
        tMesto=new TextField("Mesto");
        tMesto.setWidth("400");
        tPsc=new TextField("PSČ");
        tPsc.setMaxLength(5);
        tPsc.setWidth("50%");
        tTelefon=new TextField("Telefon");




        btnUloz=new Button("Ulož");
        btnZmaz =new Button("Zmaž");
        nastavComponnenty();
        FormLayout lEdit=new FormLayout();
        lEdit.addComponent(tICO);
        lEdit.addComponent(tNazov);
        lEdit.addComponent(tDic);
        lEdit.addComponent(tIcDph);
        lEdit.addComponent(tUlica);
        lEdit.addComponent(tMesto);
        lEdit.addComponent(tPsc);
        lEdit.addComponent(tTelefon);


        HorizontalLayout lBtn=new HorizontalLayout();
        lBtn.addComponent(btnUloz);
        lBtn.addComponent(btnZmaz);


         this.addComponent(lEdit);
         this.addComponent(lBtn);

    }
    private void nastavComponnenty(){


    Binder.Binding<Firma, String> icoBinding = binder.forField(tICO)
                .withValidator(v -> !tICO.getValue().trim().isEmpty(),
                        "IČO je povinné")
                .bind(Firma::getIco, Firma::setIco);

    Binder.Binding<Firma, String> nazovBinding = binder.forField(tNazov)
                .withValidator(v -> !tNazov.getValue().trim().isEmpty(),
                        "Názov je poviný")
                .bind(Firma::getNazov, Firma::setNazov);

    Binder.Binding<Firma, String> dicBinding = binder.forField(tDic)
                .bind(Firma::getDic, Firma::setDic);

    Binder.Binding<Firma, String> icdphBinding = binder.forField(tIcDph)
                .bind(Firma::getIc_dph, Firma::setIc_dph);

    Binder.Binding<Firma, String> ulicaBinding = binder.forField(tUlica)
                .bind(Firma::getUlica, Firma::setUlica);

    Binder.Binding<Firma, String> mestoBinding = binder.forField(tMesto)
                .bind(Firma::getMesto, Firma::setMesto);

    Binder.Binding<Firma, String> pscBinding = binder.forField(tPsc)
                .bind(Firma::getPsc, Firma::setPsc);

    Binder.Binding<Firma, String> telefonBinding = binder.forField(tTelefon)
                .bind(Firma::getTelefon, Firma::setTelefon);

    tICO.addValueChangeListener(event -> icoBinding.validate());
    tNazov.addValueChangeListener(event -> nazovBinding.validate());

    btnUloz.setStyleName(ValoTheme.BUTTON_PRIMARY);
    btnUloz.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    btnUloz.addClickListener(this::save);

    btnZmaz.addClickListener(this::delete);

    //setVisible(false);


}
    void edit(Firma firma) {
        firmaEditovana = firma;
        if (firma != null) {
            System.out.println("Zvolená "+ firmaEditovana.getNazov());
            binder.readBean(firma);
        }
        else{
            System.out.println("Zvolená nová");
            binder.readBean(firma);}

    }

    public void save(Button.ClickEvent event) {
        if (binder.writeBeanIfValid(firmaEditovana)) {
            boolean jeFirmaNova = firmaEditovana.isNew();
            Firma ulozenaFirma = FirmaNastroje.ulozFirmu(firmaEditovana);
            String msg = String.format("Ulozeny .",
                    firmaEditovana.getNazov());

            Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
            if (jeFirmaNova){
                firmyView.pridajNovuFirmu(ulozenaFirma);
            }
            firmyView.refreshFiriem();

        }

    }

    public void delete(Button.ClickEvent event) {

        ConfirmDialog.show(UI.getCurrent(), "Odstránenie", "Naozaj si prajete odstrániť firmu "+ firmaEditovana.getNazov()+"?",
                "Áno", "Nie", new ConfirmDialog.Listener() {

                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue
                            FirmaNastroje.zmazFirmu(firmaEditovana);
                            firmyView.odstranFirmu(firmaEditovana);
                            Notification.show("Firma odstránena", Notification.Type.TRAY_NOTIFICATION);
                        }
                    }
                });

    }

    public void setFirmaView(FirmyView firmaView) {
        this.firmyView = firmaView;
    }
}
