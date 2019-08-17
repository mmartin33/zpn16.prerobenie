package sk.zpn.zaklad.view.doklady;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.*;
import sk.zpn.zaklad.model.DokladyNastroje;
import sk.zpn.zaklad.model.FirmaNastroje;
import sk.zpn.zaklad.model.UzivatelNastroje;
import sk.zpn.zaklad.view.uzivatel.UzivateliaView;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EditacnyForm extends VerticalLayout {


    private TextField tCislo;
    private DateField dDatum;
    private ComboBox<String> typDokladuComboBox;
    private TextField tPoznamka;
    private TextField tFirma;
    private ComboBox<String> stavDokladuComboBox;


    protected Button btnUloz;
    protected Button btnZmaz;
    private final Binder<Doklad> binder = new Binder<>();
    private Doklad staryDokladEditovany;
    private Doklad dokladEditovany;
    private DokladyView dokladyView;

    public EditacnyForm(){
        tCislo = new TextField("Číslo");
        dDatum = new DateField("Dátum");
        tFirma = new TextField("Firma");

        tFirma.setWidth("400");
        typDokladuComboBox = new ComboBox<>("Typ dokladu");
        tPoznamka = new TextField("Poznámka");
        tPoznamka.setWidth("400");
        stavDokladuComboBox = new ComboBox<>("Stav dokladu");

        btnUloz=new Button("Ulož", VaadinIcons.CHECK_CIRCLE);
        btnZmaz =new Button("Zmaž",VaadinIcons.CLOSE_CIRCLE);

        nastavComponnenty();
        FormLayout lEdit=new FormLayout();

        lEdit.addComponent(dDatum);
        lEdit.addComponent(tCislo);
        lEdit.addComponent(tFirma);
        lEdit.addComponent(typDokladuComboBox);
        lEdit.addComponent(tPoznamka);
        lEdit.addComponent(stavDokladuComboBox);


        HorizontalLayout lBtn=new HorizontalLayout();
        lBtn.addComponent(btnUloz);
        lBtn.addComponent(btnZmaz);

        typDokladuComboBox.setItems(
            Arrays.stream(TypDokladu.values())
            .map(TypDokladu::getDisplayValue));

        stavDokladuComboBox.setItems(
                Arrays.stream(StavDokladu.values())
                        .map(StavDokladu::getDisplayValue));



         this.addComponent(lEdit);
         this.addComponent(lBtn);

        AutocompleteExtension<Firma> dokladAutocompleteExtension = new AutocompleteExtension<>(tFirma);
        dokladAutocompleteExtension.setSuggestionListSize(50);
        dokladAutocompleteExtension.setSuggestionGenerator(
            this::navrhniFirmu,
            this::transformujFirmuNaNazov,
            this::transformujFirmuNaNazovSoZvyraznenymQuery);


    }
    private void nastavComponnenty(){

    Binder.Binding<Doklad, String> cisloDokladuBinding = binder.forField(tCislo)
        .withValidator(v -> !tCislo.getValue().trim().isEmpty(),
        "Číslo je povinné")
        .bind(Doklad::getCisloDokladu, Doklad::setCisloDokladu);

        Binder.Binding<Doklad, String> typDokladuBinding = binder.forField(typDokladuComboBox)
        .bind(doklad -> doklad.getTypDokladu().getDisplayValue(),
            (doklad, value) -> doklad.setTypDokladu(
                TypDokladu.fromDisplayName(value)));


        Binder.Binding<Doklad, String> stavDokladuBinding = binder.forField(stavDokladuComboBox)
                .bind(doklad -> doklad.getStavDokladu().getDisplayValue(),
                        (doklad, value) -> doklad.setStavDokladu(
                                StavDokladu.fromDisplayName(value)));

        Binder.Binding<Doklad, String> firmaBinding = binder.forField(tFirma)
                .withValidator(nazovFirmy -> FirmaNastroje.prvaFirmaPodlaNazvu(nazovFirmy).isPresent(),
                    "Firma musi byt existujuca")
                .bind(doklad -> doklad.getFirma() == null ? "" : doklad.getFirma().getNazov(),
                    (doklad, s) -> FirmaNastroje.prvaFirmaPodlaNazvu(tFirma.getValue()).ifPresent(doklad::setFirma));

        Binder.Binding<Doklad, Date> datumBinding = binder.forField(dDatum)
                .withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .bind(Doklad::getDatum, Doklad::setDatum);

        Binder.Binding<Doklad, String> pozamkaBinding = binder.forField(tPoznamka)
                .bind(Doklad::getPoznamka, Doklad::setPoznamka);



    tCislo.addValueChangeListener(event -> cisloDokladuBinding.validate());

    btnUloz.setStyleName(ValoTheme.BUTTON_PRIMARY);
    btnUloz.addClickListener(this::save);

    btnZmaz.addClickListener(this::delete);


}
    void edit(Doklad doklad) {
        staryDokladEditovany=dokladEditovany;
        dokladEditovany = doklad;
        if (doklad != null) {
            binder.readBean(doklad);
        }
        else{
            binder.readBean(doklad);}

    }

    public void save(Button.ClickEvent event) {
        if (binder.writeBeanIfValid(dokladEditovany)) {
            boolean jeDokladNovy = dokladEditovany.isNew();
            Doklad ulozenyDoklad = DokladyNastroje.ulozDoklad(dokladEditovany);
            String msg = String.format("Ulozeny .",
                    dokladEditovany.getCisloDokladu());

            Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
            if (jeDokladNovy){
                dokladyView.pridajNovyDoklad(ulozenyDoklad);
            }
            dokladyView.refreshDokladov();
            dokladyView.selectDoklad(ulozenyDoklad);

        }

    }

    public void delete(Button.ClickEvent event) {
        if (!Optional.ofNullable(dokladEditovany).isPresent()) {
            return;
        }

        ConfirmDialog.show(UI.getCurrent(), "Odstránenie dokladu", "Naozaj si prajete odstrániť doklad "+dokladEditovany.getCisloDokladu()+"?",
                "Áno", "Nie", new ConfirmDialog.Listener() {

                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue
                            DokladyNastroje.zmazDoklad(dokladEditovany);
                            dokladyView.odstranDoklad(dokladEditovany);
                            Notification.show("Doklad odstránený", Notification.Type.TRAY_NOTIFICATION);
                            clearEditacnyForm();
                            dokladyView.selectNejaky(staryDokladEditovany);
                        }
                    }
        });

    }

    public void setDokladyView(DokladyView dokladyView) {
        this.dokladyView = dokladyView;
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
        String noveCislo;
        noveCislo=DokladyNastroje.noveCisloDokladu(null);
        tCislo.clear();
        tFirma.clear();
        tPoznamka.clear();
        typDokladuComboBox.clear();
        stavDokladuComboBox.clear();
        dDatum.clear();
    }
}
