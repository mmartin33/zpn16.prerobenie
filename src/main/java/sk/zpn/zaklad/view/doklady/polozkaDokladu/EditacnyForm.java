package sk.zpn.zaklad.view.doklady.polozkaDokladu;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.*;
import sk.zpn.zaklad.model.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EditacnyForm extends VerticalLayout {

    private TextField tProdukt;
    private TextField tFirma;
    private TextField tPoberatel;
    private TextField tBody;
    private TextField tMnozstvo;
    private TextField tPoznamka;


    protected Button btnUloz;
    protected Button btnZmaz;

    private final Binder <PolozkaDokladu> binder = new Binder<>();
    private PolozkaDokladu polozkaEditovana;
    private PolozkyDokladuView polozkyDokladyView;

    public EditacnyForm(){
        tProdukt = new TextField("Produkt");
        tProdukt.setWidth("400");
        tFirma = new TextField("Firma");
        tFirma.setWidth("400");
        tPoberatel = new TextField("Poberateľ");
        tPoberatel.setWidth("400");
        tBody = new TextField("Body");
        tMnozstvo = new TextField("Množstvo");
        tPoznamka = new TextField("Poznamka");
        tPoznamka.setWidth("400");
        btnUloz=new Button("Ulož", VaadinIcons.CHECK_CIRCLE);
        btnZmaz =new Button("Zmaž",VaadinIcons.CLOSE_CIRCLE);
        nastavComponnenty();
        FormLayout lEdit=new FormLayout();

        lEdit.addComponent(tProdukt);
        lEdit.addComponent(tFirma);
        lEdit.addComponent(tPoberatel);
        lEdit.addComponent(tBody);
        lEdit.addComponent(tMnozstvo);
        lEdit.addComponent(tPoznamka);


        HorizontalLayout lBtn=new HorizontalLayout();
        lBtn.addComponent(btnUloz);
        lBtn.addComponent(btnZmaz);


         this.addComponent(lEdit);
         this.addComponent(lBtn);

        AutocompleteExtension<Firma> dokladAutocompleteExtension = new AutocompleteExtension<>(tFirma);
        dokladAutocompleteExtension.setSuggestionGenerator(
            this::navrhniFirmu,
            this::transformujFirmuNaNazov,
            this::transformujFirmuNaNazovSoZvyraznenymQuery);

        AutocompleteExtension<Produkt> dokladAutocompleteExtensionProdukt = new AutocompleteExtension<>(tProdukt);
        dokladAutocompleteExtensionProdukt.setSuggestionGenerator(
            this::navrhniProdukt,
            this::transformujProduktNaNazov,
            this::transformujProduktNaNazovSoZvyraznenymQuery);

        AutocompleteExtension<Poberatel> dokladAutocompleteExtensionProberatel = new AutocompleteExtension<>(tPoberatel);
        dokladAutocompleteExtensionProberatel.setSuggestionGenerator(
            this::navrhniPoberatela,
            this::transformujPoberatelaNaNazov,
            this::transformujPoberatelaNaNazovSoZvyraznenymQuery);


    }
    private void nastavComponnenty(){



        Binder.Binding<PolozkaDokladu, String> produktBinding = binder.forField(tProdukt)
                .withValidator(nazovProduktu -> ProduktyNastroje.prvyProduktPodlaNazvu(nazovProduktu).isPresent(),
                    "Produkt musi byt existujuci")
                .bind(polozkaDokladu -> polozkaDokladu.getProduktNazov() == null ? "" : polozkaDokladu.getProdukt().getNazov(),
                    (polozkaDokladu, s) -> ProduktyNastroje.prvyProduktPodlaNazvu(tProdukt.getValue()).ifPresent(polozkaDokladu::setProdukt));

        Binder.Binding<PolozkaDokladu, String> firmaBinding = binder.forField(tFirma)
                .withValidator(nazovFirmy -> FirmaNastroje.prvaFirmaPodlaNazvu(nazovFirmy).isPresent(),
                    "Firma musi byt existujuca")
                .bind(polozkaDokladu -> polozkaDokladu.getFirma() == null ? "" : polozkaDokladu.getFirma().getNazov(),
                    (polozkaDokladu, s) -> FirmaNastroje.prvaFirmaPodlaNazvu(tFirma.getValue()).ifPresent(polozkaDokladu::setFirma));

        Binder.Binding<PolozkaDokladu, String> poberatelBinding = binder.forField(tPoberatel)
                .withValidator(menoPoberatela -> PoberatelNastroje.prvyPoberatelPodlaMena(menoPoberatela).isPresent(),
                    "poberatel musi byt existujuci")
                .bind(polozkaDokladu -> polozkaDokladu.getFirma() == null ? "" : polozkaDokladu.getFirma().getNazov(),
                    (polozkaDokladu, s) -> FirmaNastroje.prvaFirmaPodlaNazvu(tFirma.getValue()).ifPresent(polozkaDokladu::setFirma));


        Binder.Binding<PolozkaDokladu, String> poznamkaBinding = binder.forField(tPoznamka)
                .bind(PolozkaDokladu::getPoznamka, PolozkaDokladu::setPoznamka);

        Binder.Binding<PolozkaDokladu, Double> bodyBinding = binder.forField(tBody)
                .withConverter(new StringToDoubleConverter("Nie je číslo"))
                .bind(PolozkaDokladu::getBody, PolozkaDokladu::setBody);

        Binder.Binding<PolozkaDokladu, Double> mnozstvoBinding = binder.forField(tMnozstvo)
                .withConverter(new StringToDoubleConverter("Nie je číslo"))
                .bind(PolozkaDokladu::getMnozstvo, PolozkaDokladu::setMnozstvo);



   // tCislo.addValueChangeListener(event -> cisloDokladuBinding.validate());

    btnUloz.setStyleName(ValoTheme.BUTTON_PRIMARY);
    btnUloz.addClickListener(this::save);

    btnZmaz.addClickListener(this::delete);


}
    void edit(PolozkaDokladu polozkaDokladu) {
        polozkaEditovana = polozkaDokladu;
        if (polozkaDokladu != null) {
            binder.readBean(polozkaDokladu);
        }
        else{
            binder.readBean(polozkaDokladu);}
    }

    public void save(Button.ClickEvent event) {
        if (binder.writeBeanIfValid(polozkaEditovana)) {
            boolean jeDokladNovy = polozkaEditovana.isNew();
            PolozkaDokladu ulozenaPolozka = PolozkaDokladuNastroje.ulozpolozkuDokladu(polozkaEditovana);
            String msg = String.format("Ulozeny .");

            Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
            if (jeDokladNovy){
                polozkyDokladyView.pridajNovuPolozkuDokladu(ulozenaPolozka);
            }
            polozkyDokladyView.refreshPoloziekDokladov();
            polozkyDokladyView.selectPolozkuDokladu(ulozenaPolozka);

        }

    }

    public void delete(Button.ClickEvent event) {
        if (!Optional.ofNullable(polozkyDokladyView).isPresent()) {
            return;
        }

        ConfirmDialog.show(UI.getCurrent(), "Odstránenie dokladu", "Naozaj si prajete odstrániť položku "+"?",
                "Áno", "Nie", new ConfirmDialog.Listener() {

                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue
                            PolozkaDokladuNastroje.zmazPolozkyDoklady(polozkaEditovana);
                            polozkyDokladyView.odstranPolozkuDokladu(polozkaEditovana);
                            Notification.show("Doklad odstránený", Notification.Type.TRAY_NOTIFICATION);
                            clearEditacnyForm();
                            polozkyDokladyView.selectFirst();
                        }
                    }
        });

    }

    public void setDokladyView(PolozkyDokladuView polozkyDokladuView) {
        this.polozkyDokladyView = polozkyDokladuView;
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
    private List<Produkt> navrhniProdukt(String query, int cap) {
        return ProduktyNastroje.zoznamProduktovZaRok().stream()
                .filter(produkt -> produkt.getNazov().toLowerCase().contains(query.toLowerCase()))
                .limit(cap).collect(Collectors.toList());
    }
    /**
     * Co sa zobraziv textfielde, ked sa uz hodnota vyberie
     * */
    private String transformujProduktNaNazov(Produkt produkt) {
        return produkt.getNazov();

    }
    /**
     * Co sa zobrazi v dropdowne
     * */
    private String transformujProduktNaNazovSoZvyraznenymQuery(Produkt produkt, String query) {
        return "<div class='suggestion-container'>"
                + "<span class='produkt'>"
                + produkt.getNazov()
                .replaceAll("(?i)(" + query + ")", "<b>$1</b>")
                + "</span>"
                + "</div>";
    }

    private List<Poberatel> navrhniPoberatela(String query, int cap) {
        return PoberatelNastroje.zoznamPoberatelov().stream()
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
                .replaceAll("(?i)(" + query + ")", "<b>$1</b>")
                + "</span>"
                + "</div>";
    }
    private void clearEditacnyForm() {
        tProdukt.clear();
        tFirma.clear();
        tPoberatel.clear();
        tBody.clear();
        tMnozstvo.clear();
        tPoznamka.clear();
    }
}
