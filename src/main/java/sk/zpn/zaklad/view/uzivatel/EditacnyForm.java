package sk.zpn.zaklad.view.uzivatel;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.Firma;
import sk.zpn.domena.StatusUzivatela;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.model.FirmaNastroje;
import sk.zpn.zaklad.model.UzivatelNastroje;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EditacnyForm extends VerticalLayout {


    private TextField tMeno;
    private TextField tFirma;
    private PasswordField passwordField;
    private ComboBox<String> typUzivatelaComboBox;
    private ComboBox<String> statusUzivatelaComboBox;
    protected Button btnUloz;
    protected Button btnZmaz;
    private final Binder<Uzivatel> binder = new Binder<>();
    private Uzivatel uzivateEditovany;
    private UzivateliaView uzivatelView;

    public EditacnyForm(){
        tMeno = new TextField("Meno");
        tFirma = new TextField("Firma");
        passwordField =  new PasswordField("Heslo");
        typUzivatelaComboBox = new ComboBox<>("Typ konta");
        statusUzivatelaComboBox = new ComboBox<>("Stav konta");
        btnUloz=new Button("Ulož", VaadinIcons.CHECK_CIRCLE);
        btnZmaz =new Button("Zmaž",VaadinIcons.CLOSE_CIRCLE);
        nastavComponnenty();
        FormLayout lEdit=new FormLayout();
        lEdit.addComponent(tMeno);
        lEdit.addComponent(tFirma);
        lEdit.addComponent(passwordField);
        lEdit.addComponent(typUzivatelaComboBox);
        lEdit.addComponent(statusUzivatelaComboBox);

        HorizontalLayout lBtn=new HorizontalLayout();
        lBtn.addComponent(btnUloz);
        lBtn.addComponent(btnZmaz);

        typUzivatelaComboBox.setItems(
            Arrays.stream(TypUzivatela.values())
            .map(TypUzivatela::getDisplayValue));

        statusUzivatelaComboBox.setItems(
                Arrays.stream(StatusUzivatela.values())
                .map(StatusUzivatela::getDisplayValue));

         this.addComponent(lEdit);
         this.addComponent(lBtn);

        AutocompleteExtension<Firma> firmaAutocompleteExtension = new AutocompleteExtension<>(tFirma);
        firmaAutocompleteExtension.setSuggestionGenerator(
            this::navrhniFirmu,
            this::transformujFirmuNaNazov,
            this::transformujFirmuNaNazovSoZvyraznenymQuery);
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

        Binder.Binding<Uzivatel, String> hesloBinding = binder.forField(passwordField)
            .withValidator(v -> !passwordField.getValue().trim().isEmpty(),
            "Heslo je povinne")
            // zobrazene heslo bude mat vzdy rovnaku dlzku
            .bind(v -> v.getHeslo() == null || v.getHeslo().length() == 0 ? "" : "longString",
                  Uzivatel::setHeslo);


        Binder.Binding<Uzivatel, String> firmaBinding = binder.forField(tFirma)
            .withValidator(nazovFirmy -> !tFirma.getValue().trim().isEmpty(),
                    "Firma je povinna")
                .withValidator(nazovFirmy -> FirmaNastroje.prvaFirmaPodlaNazvu(nazovFirmy).isPresent(),
                    "Firma musi byt existujuca")
                .bind(uzivatel -> uzivatel.getFirma() == null ? "" : uzivatel.getFirma().getNazov(),
                    (uzivatel, s) -> FirmaNastroje.prvaFirmaPodlaNazvu(tFirma.getValue()).ifPresent(uzivatel::setFirma));

        Binder.Binding<Uzivatel, String> statusUzivatela = binder.forField(statusUzivatelaComboBox)
            .bind(uzivatel -> uzivatel.getStatusUzivatela().getDisplayValue(),
                    (uzivatel, value) -> uzivatel.setStatusUzivatela(
                            StatusUzivatela.fromDisplayName(statusUzivatelaComboBox.getValue())));

        tMeno.addValueChangeListener(event -> menoBinding.validate());

        btnUloz.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnUloz.addClickListener(this::save);

        btnZmaz.addClickListener(this::delete);


    }
    void edit(Uzivatel uzivatel) {
        uzivateEditovany = uzivatel;
        if (uzivatel != null) {
            binder.readBean(uzivatel);
        }
        else{
            binder.readBean(uzivatel);}
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
            uzivatelView.selectUzivatel(ulozenyUzivatel);

        }

    }

    public void delete(Button.ClickEvent event) {
        if (!Optional.ofNullable(uzivateEditovany).isPresent()) {
            return;
        }

        ConfirmDialog.show(UI.getCurrent(), "Odstránenie uživateľa", "Naozaj si prajete odstrániť uživatela "+uzivateEditovany.getMeno()+"?",
                "Áno", "Nie", new ConfirmDialog.Listener() {

                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue
                            UzivatelNastroje.zmazUzivatela(uzivateEditovany);
                            uzivatelView.odstranUzivatela(uzivateEditovany);
                            Notification.show("Užívateľ odstránený", Notification.Type.TRAY_NOTIFICATION);
                            clearEditacnyForm();
                            uzivatelView.selectFirst();
                        }
                    }
        });

    }

    public void setUzivatelView(UzivateliaView uzivatelView) {
        this.uzivatelView = uzivatelView;
    }

    private List<Firma> navrhniFirmu(String query, int cap) {
        return  FirmaNastroje.zoznamFiriem().stream()
                .filter(firma -> firma.getNazov().toLowerCase().contains(query.toLowerCase()))
                .limit(cap).collect(Collectors.toList());
    }
    /**
     * Co sa zobraziv textfielde, ked sa uz hodnota vyberie
     * */
    private String transformujFirmuNaNazov(Firma firma) {
        return firma.getNazov();
    }
    /**
     * Co sa zobrazi v dropdowne
     * */
    private String transformujFirmuNaNazovSoZvyraznenymQuery(Firma firma, String query) {
        return "<div class='suggestion-container'>"
                + "<span class='firma'>"
                + firma.getNazov()
                .replaceAll("(?i)(" + query + ")", "<b>$1</b>")
                + "</span>"
                + "</div>";
    }
    private void clearEditacnyForm() {
        tMeno.clear();
        tFirma.clear();
        passwordField.clear();
        typUzivatelaComboBox.clear();
        statusUzivatelaComboBox.clear();
    }
}
