package sk.zpn.zaklad.view.produkty;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Produkt;
import sk.zpn.domena.TypProduktov;
import sk.zpn.zaklad.model.FirmaNastroje;
import sk.zpn.zaklad.model.ProduktyNastroje;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class EditacnyForm extends VerticalLayout {


    private TextField tRok;
    private TextField tKod;
    private TextField tNazov;
    private TextField tKusy;
    private TextField tBody;
    private TextField tFirma;
    public TextField tCena;




    protected Button btnUloz;
    protected Button btnZmaz;

    private final Binder<Produkt> binder = new Binder<>();
    private Produkt produktEditovany;
    private ProduktyView produktyView;


    public EditacnyForm() {
        tRok = new TextField("Rok");
        tRok.setWidth("100");
        tKod = new TextField("Kat");
        tKod.setWidth("200");
        tNazov = new TextField("Názov");
        tNazov.setWidth("400");
        tBody = new TextField("Body");
        tBody.setWidth("100");
        tKusy = new TextField("Kusy");
        tKusy.setWidth("100");
        tFirma = new TextField("Firma");
        tFirma.setWidth("400");
        tCena = new TextField("Cena");
        tCena.setWidth("100");
        btnUloz=new Button("Ulož", VaadinIcons.CHECK_CIRCLE);
        btnZmaz =new Button("Zmaž",VaadinIcons.CLOSE_CIRCLE);

        nastavComponnenty();
        FormLayout lEdit = new FormLayout();

        lEdit.addComponent(tRok);
        lEdit.addComponent(tKod);
        lEdit.addComponent(tNazov);
        lEdit.addComponent(tBody);
        lEdit.addComponent(tKusy);
        lEdit.addComponent(tFirma);
        lEdit.addComponent(tFirma);
        lEdit.addComponent(tCena);
        tCena.setVisible(false);
        HorizontalLayout lBtn = new HorizontalLayout();
        lBtn.addComponent(btnUloz);
        lBtn.addComponent(btnZmaz);



        this.addComponent(lEdit);
        this.addComponent(lBtn);

        AutocompleteExtension<Firma> firmaAutocompleteExtension = new AutocompleteExtension<>(tFirma);
        firmaAutocompleteExtension.setSuggestionListSize(50);
        firmaAutocompleteExtension.setSuggestionGenerator(
                this::navrhniFirmu,
                this::transformujFirmuNaNazov,
                this::transformujFirmuNaNazovSoZvyraznenymQuery);
    }

    private void nastavComponnenty() {

        tRok.setEnabled(false);
        Binder.Binding<Produkt, String> rokBinding = binder.forField(tRok)
                .bind(Produkt::getRok, Produkt::setRok);
        Binder.Binding<Produkt, String> kodBinding = binder.forField(tKod)
                .withValidator(kod -> !tKod.getValue().trim().isEmpty(),
                "Kod je povinny")
//                .withValidator(kod -> (!ProduktyNastroje.uzExistujeKat(tKod.getValue())||(!produktEditovany.isNew())),
//                        "Kod uz existuje")

                .bind(Produkt::getKat, Produkt::setKat);
        Binder.Binding<Produkt, String> nazovBinding = binder.forField(tNazov)
                .bind(Produkt::getNazov, Produkt::setNazov);
        Binder.Binding<Produkt, BigDecimal> bodyBinding = binder.forField(tBody)
                .withConverter(new StringToBigDecimalConverter("Nie je číslo"))
                .withValidator(body -> !tBody.getValue().trim().isEmpty(),
                        "Body su povinne")
                .bind(Produkt::getBody, Produkt::setBody);
        Binder.Binding<Produkt, BigDecimal> kusyBinding = binder.forField(tKusy)
                .withConverter(new StringToBigDecimalConverter("Nie je číslo"))
                .withValidator(kusy -> !tKusy.getValue().trim().isEmpty(),
                        "Kusy su povinne")
                .bind(Produkt::getKusy, Produkt::setKusy);


        Binder.Binding<Produkt, String> firmaBinding = binder.forField(tFirma)
                .withValidator(nazovFirmy -> !tFirma.getValue().trim().isEmpty(),
                        "Firma je povinna")
                .withValidator(nazovFirmy -> FirmaNastroje.prvaFirmaPodlaNazvu(nazovFirmy).isPresent(),
                        "Firma musi byt existujuca")
                .bind(poberatel -> poberatel.getFirma() == null ? "" : poberatel.getFirma().getNazov(),
                        (poberatel, s) -> FirmaNastroje.prvaFirmaPodlaNazvu(tFirma.getValue()).ifPresent(poberatel::setFirma));
        Binder.Binding<Produkt, BigDecimal> cenaBinding = binder.forField(tCena)
                .withConverter(new StringToBigDecimalConverter("Nie je číslo"))
                .withValidator(body -> !tCena.getValue().trim().isEmpty(),
                        "Body su povinne")
                .bind(Produkt::getCena, Produkt::setCena);



        tKod.addValueChangeListener(event -> kodBinding.validate());
        tNazov.addValueChangeListener(event -> nazovBinding.validate());
        tNazov.addValueChangeListener(event -> nazovBinding.validate());
        tBody.addValueChangeListener(event -> bodyBinding.validate());
        tKusy.addValueChangeListener(event -> kusyBinding.validate());


        //btnUloz.setStyleName(ValoTheme.BUTTON_PRIMARY);

        btnUloz.addClickListener(this::save);
        btnZmaz.addClickListener(this::delete);


        setVisible(true);


    }

    void edit(Produkt produkt) {
        produktEditovany = produkt;
        if (produkt != null) {
            System.out.println("Zvoleny " + produktEditovany.getNazov());
            binder.readBean(produkt);
        } else {
            System.out.println("Zvolený nový");
            binder.readBean(produkt);
        }

    }


    public void save(Button.ClickEvent event) {
        if (binder.writeBeanIfValid(produktEditovany)) {
            boolean jeProduktNovy = produktEditovany.isNew();
            Produkt ulozenyProdukt = ProduktyNastroje.ulozProdukt(produktEditovany);
            String msg = String.format("Ulozeny .",
                    produktEditovany.getNazov());

            Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
            if (jeProduktNovy) {
                produktyView.pridajNovyProdukt(ulozenyProdukt);
            }
            produktyView.refreshProduktov();

        }

    }

    public void delete(Button.ClickEvent event) {

        ConfirmDialog.show(UI.getCurrent(), "Odstránenie", "Naozaj si prajete odstrániť produkt " + produktEditovany.getNazov() + "?",
                "Áno", "Nie", new ConfirmDialog.Listener() {

                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue
                            ProduktyNastroje.zmazProdukt(produktEditovany);
                            produktyView.odstranProdukt(produktEditovany);
                            Notification.show("Produkt odstránený", Notification.Type.TRAY_NOTIFICATION);
                        }
                    }
                });

    }

    public void setProduktyView(ProduktyView produktyView) {
        this.produktyView = produktyView;
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

}

