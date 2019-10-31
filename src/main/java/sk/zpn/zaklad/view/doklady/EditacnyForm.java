package sk.zpn.zaklad.view.doklady;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.*;
import sk.zpn.zaklad.model.DokladyNastroje;
import sk.zpn.zaklad.model.FirmaNastroje;
import sk.zpn.zaklad.model.PoberatelNastroje;
import sk.zpn.zaklad.view.poberatelia.PoberateliaView;

import java.awt.*;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EditacnyForm extends VerticalLayout {


    private TextField tCislo;
    private TextField tCisloDokladuOdmeny;
    private DateField dDatum;
    private ComboBox<String> typDokladuComboBox;
    private TextField tPoberatel;
    private TextField tPoznamka;
    private TextField tFirma;
    private ComboBox<String> stavDokladuComboBox;


    protected Button btnUloz;
    protected Button btnVyberPoberatela;
    protected Button btnZmaz;
    private final Binder<Doklad> binder = new Binder<>();
    private Doklad staryDokladEditovany;
    private Doklad dokladEditovany;
    private DokladyView dokladyView;
    private Poberatel aktualnyPoberatel;
    private boolean rezimOdmien=false;

    public EditacnyForm(){
        tCislo = new TextField("Číslo");
        tCisloDokladuOdmeny = new TextField("Číslo dokladu odmeny");
        dDatum = new DateField("Dátum");
        tFirma = new TextField("Firma");
        tPoberatel = new TextField("Poberatel");

        tFirma.setWidth("400");
        tPoberatel.setWidth("400");
        typDokladuComboBox = new ComboBox<>("Typ dokladu");
        tPoznamka = new TextField("Poznámka");
        tPoznamka.setWidth("400");
        stavDokladuComboBox = new ComboBox<>("Stav dokladu");

        btnUloz=new Button("Ulož", VaadinIcons.CHECK_CIRCLE);
        btnZmaz =new Button("Zmaž",VaadinIcons.CLOSE_CIRCLE);
        btnVyberPoberatela =new Button("Vyber poberateľa",VaadinIcons.USER_CHECK);
        btnVyberPoberatela.setEnabled(false);
        btnVyberPoberatela.setVisible(false);

        btnZmaz.setVisible(false);

        nastavComponnenty();
        FormLayout lEdit=new FormLayout();

        lEdit.addComponent(dDatum);
        lEdit.addComponent(tCislo);
        lEdit.addComponent(tCisloDokladuOdmeny);
        lEdit.addComponent(tPoberatel);
        lEdit.addComponent(tFirma);
        lEdit.addComponent(typDokladuComboBox);
        lEdit.addComponent(tPoznamka);
        lEdit.addComponent(stavDokladuComboBox);


        HorizontalLayout lBtn=new HorizontalLayout();

        lBtn.addComponent(btnUloz);
        lBtn.addComponent(btnVyberPoberatela);
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

        AutocompleteExtension<Poberatel> dokladAutocompleteExtensionProberatel = new AutocompleteExtension<>(tPoberatel);
        dokladAutocompleteExtensionProberatel.setSuggestionListSize(50);
        dokladAutocompleteExtensionProberatel.setSuggestionGenerator(
                this::navrhniPoberatela,
                this::transformujPoberatelaNaNazov,
                this::transformujPoberatelaNaNazovSoZvyraznenymQuery);

        dokladAutocompleteExtensionProberatel.addSuggestionSelectListener(event -> {
            event.getSelectedItem().ifPresent(this::vybratyPoberatel);
        });




    }
    private void nastavComponnenty(){

        Binder.Binding<Doklad, String> cisloDokladuBinding = binder.forField(tCislo)
                .withValidator(v -> !tCislo.getValue().trim().isEmpty(),
        "Číslo je povinné")
        .bind(Doklad::getCisloDokladu, Doklad::setCisloDokladu);

        Binder.Binding<Doklad, String> cisloDokladuOdmenyBinding = binder.forField(tCisloDokladuOdmeny)
                .withValidator(v -> !tCislo.getValue().trim().isEmpty(),
        "Číslo je povinné")
        .bind(Doklad::getCisloDokladuOdmeny, Doklad::setCisloDokladuOdmeny);

        Binder.Binding<Doklad, String> typDokladuBinding = binder.forField(typDokladuComboBox)
        .bind(doklad -> doklad.getTypDokladu().getDisplayValue(),
            (doklad, value) -> doklad.setTypDokladu(TypDokladu.fromDisplayName(value)));


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
    btnVyberPoberatela.addClickListener(this::vyberPoberatela);

//    btnZmaz.addClickListener(this::delete);


}

    private void vyberPoberatela(Button.ClickEvent clickEvent) {
        PoberateliaView sv = new PoberateliaView(null);
        sv.setRodicovskyView(dokladyView.NAME);
        sv.setZdrojovyView(this.dokladyView);
        UI.getCurrent().getNavigator().addView(sv.NAME, sv);
        UI.getCurrent().getNavigator().navigateTo(sv.NAME);




    }

    void edit(Doklad doklad) {
        staryDokladEditovany=dokladEditovany;
        this.dokladEditovany = doklad;
        if (doklad != null) {
            binder.readBean(this.dokladEditovany);
        }
        else{
            binder.readBean(this.dokladEditovany);}

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
            //this.btnVyberPoberatela.setEnabled(false);


        }

    }

    public void delete() {

        if (!Optional.ofNullable(dokladEditovany).isPresent()) {
            Notification.show("Nebol vybrany doklad!!!", Notification.Type.WARNING_MESSAGE);
            return;
        }

        int pocetPoloziek=DokladyNastroje.pocetPoloziek(dokladEditovany);

        ConfirmDialog.show(UI.getCurrent(), "Odstránenie dokladu", "Naozaj si prajete odstrániť doklad "+dokladEditovany.getCisloDokladu()+ "?"+
                        (pocetPoloziek==0?"":"POZOR doklad obsahuje položky!!!!!!!"),
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
        return  FirmaNastroje.zoznamFiriemIbaVelkosklady().stream()
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

    public void rezimVelkoskladu() {
            btnZmaz.setVisible(false);
            btnUloz.setVisible(false);
            btnVyberPoberatela.setVisible(false);

    }

    private List<Poberatel> navrhniPoberatela(String query, int cap) {
        return PoberatelNastroje.zoznamPoberatelovPodlaMena(query).stream()
                .filter(poberatel -> poberatel.getMeno().toLowerCase().contains(query.toLowerCase()))
                .limit(cap).collect(Collectors.toList());
    }
    /**
     * Co sa zobraziv textfielde, ked sa uz hodnota vyberie
     * */
    private String transformujPoberatelaNaNazov(Poberatel poberatel) {
        return poberatel.getMeno();

    }
    /**
     * Co sa zobrazi v dropdowne
     * */
    private String transformujPoberatelaNaNazovSoZvyraznenymQuery(Poberatel poberatel, String query) {
        return "<div class='suggestion-container'>"
                + "<span class='poberatel'>"
                + poberatel.getMeno()
//                + poberatel.getPoberatelMenoAdresa()
                .replaceAll("(?i)(" + query + ")", "<b>$1</b>")
                + "</span>"
                + "</div>";
    }

    public void vybratyPoberatel(Poberatel poberatel) {
        dokladEditovany.setPoberatel(poberatel);
        Firma firma=PoberatelNastroje.getPrvyVelkosklad(poberatel);
        if (firma!=null) {
            dokladEditovany.setFirma(firma);
            tFirma.setValue(firma.getNazov());
        }
        this.aktualnyPoberatel=poberatel;
        tPoberatel.setValue(poberatel.getMeno());

    }

    public void rezimOdmien() {
        tCislo.setEnabled(false);
        stavDokladuComboBox.setVisible(false);
        typDokladuComboBox.setVisible(false);
        btnVyberPoberatela.setVisible(true);
        Binder.Binding<Doklad, String> poberatelBinding = binder.forField(tPoberatel)
                .withValidator(nazovPoberatela -> PoberatelNastroje.prvyPoberatelPodlaMena(nazovPoberatela).isPresent(),
                        "Poberatel musi byt existujuci")
                .bind(doklad -> doklad.getPoberatel() == null ? "" : doklad.getPoberatel().getMeno(),
                        (doklad, s) -> PoberatelNastroje.prvyPoberatelPodlaMena(tPoberatel.getValue()).ifPresent(doklad::setPoberatel));

        this.rezimOdmien=true;

    }

    public void rezimBodovaci() {


        tCisloDokladuOdmeny.setVisible(false);
        tPoberatel.setVisible(false);

    }

}
