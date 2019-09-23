package sk.zpn.zaklad.view.doklady.polozkaDokladu;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.event.FieldEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang.StringUtils;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import sk.zpn.domena.*;
import sk.zpn.zaklad.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class EditacnyForm extends VerticalLayout {

    private TextField tProdukt;
    private TextField tPrevadzka;
    private TextField tPoberatel;
    private TextField tBody;
    private TextField tMnozstvo;
    private TextField tPoznamka;


    protected Button btnUloz;


    private final Binder <PolozkaDokladu> binder = new Binder<>();
    private PolozkaDokladu staraEditovana;
    private PolozkaDokladu polozkaEditovana;
    private PolozkyDokladuView polozkyDokladyView;
    private Poberatel aktualnyPoberatel;
    private Produkt aktualnyProdukt;

    public EditacnyForm(){
        this.setSpacing(true);
        tProdukt = new TextField("Produkt");
        tProdukt.setWidth("400");
        tPrevadzka = new TextField("Prevádzka" );
        tPrevadzka.setWidth("400");
        tPoberatel = new TextField("Poberateľ");
        tPoberatel.setWidth("400");
        tBody = new TextField("Body");
        tMnozstvo = new TextField("Množstvo");
        tMnozstvo.addBlurListener(new FieldEvents.BlurListener() {

            @Override
            public void blur(FieldEvents.BlurEvent event) {
                prepocitajBody();
            }
        });
        tPoznamka = new TextField("Poznamka");
        tPoznamka.setWidth("400");
        btnUloz=new Button("Ulož", VaadinIcons.CHECK_CIRCLE);

        nastavComponnenty();
        HorizontalLayout hl=new HorizontalLayout();

        FormLayout lEdit=new FormLayout();
        lEdit.setSpacing(true);


        lEdit.addComponent(tProdukt);

        lEdit.addComponent(tPrevadzka);
        lEdit.addComponent(tPoberatel);
        lEdit.addComponent(tBody);
        lEdit.addComponent(tMnozstvo);
        lEdit.addComponent(tPoznamka);


        HorizontalLayout lBtn=new HorizontalLayout();
        lBtn.addComponent(btnUloz);



         this.addComponent(lEdit);
         this.addComponent(lBtn);

        AutocompleteExtension<Prevadzka> dokladAutocompleteExtension = new AutocompleteExtension<>(tPrevadzka);
        dokladAutocompleteExtension.setSuggestionListSize(50);

        dokladAutocompleteExtension.setSuggestionGenerator(
            this::navrhniPrevadzku,
            this::transformujPrevadzkuNaNazov,
            this::transformujPrevádzkuNaNazovSoZvyraznenymQuery);


        dokladAutocompleteExtension.addSuggestionSelectListener(event -> {
            event.getSelectedItem().ifPresent(this::vybrataPrevadzka);;
        });




        AutocompleteExtension<Produkt> dokladAutocompleteExtensionProdukt = new AutocompleteExtension<>(tProdukt);
        dokladAutocompleteExtensionProdukt.setSuggestionListSize(50);
        dokladAutocompleteExtensionProdukt.setSuggestionDelay(1);
        dokladAutocompleteExtensionProdukt.addSuggestionSelectListener(event -> {
            event.getSelectedItem().ifPresent(this::vybratyProdukt);;
        });
        dokladAutocompleteExtensionProdukt.setSuggestionGenerator(
            this::navrhniProdukt,
            this::transformujProduktNaNazov,
            this::transformujProduktNaNazovSoZvyraznenymQuery);

        AutocompleteExtension<Poberatel> dokladAutocompleteExtensionProberatel = new AutocompleteExtension<>(tPoberatel);
        dokladAutocompleteExtensionProberatel.setSuggestionListSize(50);
        dokladAutocompleteExtensionProdukt.setSuggestionDelay(1);
        dokladAutocompleteExtensionProberatel.setSuggestionGenerator(
            this::navrhniPoberatela,
            this::transformujPoberatelaNaNazov,
            this::transformujPoberatelaNaNazovSoZvyraznenymQuery);

        dokladAutocompleteExtensionProberatel.addSuggestionSelectListener(event -> {
            event.getSelectedItem().ifPresent(this::vybratyPoberatel);
        });



    }

    private void vybratyProdukt(Produkt produkt) {
        tBody.setValue(produkt.getBodyBigInteger().toString());
        aktualnyProdukt=produkt;


    }

    private void vybrataPrevadzka(Prevadzka prevadzka) {
        tPoberatel.setValue(prevadzka.getPoberatel().getMeno());
        aktualnyPoberatel=prevadzka.getPoberatel();
        polozkaEditovana.setPoberatel(aktualnyPoberatel);
    }

    private void vybratyPoberatel(Poberatel poberatel) {
        if (tPrevadzka.getValue()==null ||tPrevadzka.getValue()=="") {
            Prevadzka prvaPrevadzkaPoberatela=PrevadzkaNastroje.prvaPrevadzkaPoberatela(poberatel);
            tPrevadzka.setValue(prvaPrevadzkaPoberatela.getNazov());
            polozkaEditovana.setPrevadzka(prvaPrevadzkaPoberatela);
        }
        polozkaEditovana.setPoberatel(poberatel);
        this.aktualnyPoberatel=poberatel;
    }


    private void naplnPoberatela (Prevadzka prevadzka){
        this.aktualnyPoberatel=prevadzka.getPoberatel();
        tPoberatel.setValue(this.aktualnyPoberatel.getMeno());
    }
    private void nastavComponnenty(){


        Binder.Binding<PolozkaDokladu, String> prevadzkaBinding = binder.forField(tPrevadzka)
                .withValidator(nazovPrevadzky -> PrevadzkaNastroje.prvaPrevadzkaPodlaNazvu(nazovPrevadzky).isPresent(),
                    "Prevádzka musi byt existujuca")
                .bind(polozkaDokladu -> polozkaDokladu.getPrevadzka() == null ? "" : polozkaDokladu.getPrevadzka().getNazov(),
                    (polozkaDokladu, s) -> PrevadzkaNastroje.prvaPrevadzkaPodlaNazvu(tPrevadzka.getValue()).ifPresent(polozkaDokladu::setPrevadzka));


        Binder.Binding<PolozkaDokladu, String> poberatelBinding = binder.forField(tPoberatel)
                //.withValidator(nazovPoberatel -> this.aktualnyPoberatel != null, "Poberateľ musi byť vyplnený")
//                .withValidator(nazovPoberatela -> PoberatelNastroje.poberatelPodlaId(this.aktualnyPoberatel.getId())!=null,
//                        "Poberateľ musi byt existujuci")
                .withValidator(nazovPoberatela -> PoberatelNastroje.prvyPoberatelPodlaMena(nazovPoberatela).isPresent(),
                        "Poberate musi byt existujuci")



//                .bind(polozkaDokladu -> polozkaDokladu.getPoberatel() == null ? "" : polozkaDokladu.getPoberatel().getMeno(),
//                        (polozkaDokladu, s) -> PoberatelNastroje.poberatelPodlaId(this.aktualnyPoberatel.getId()).ifPresent(polozkaDokladu::setPoberatel));
                .bind(polozkaDokladu -> polozkaDokladu.getPoberatel() == null ? "" : polozkaDokladu.getPoberatel().getMeno(),
                        (polozkaDokladu, s) -> PoberatelNastroje.prvyPoberatelPodlaMena(tPoberatel.getValue()).ifPresent(polozkaDokladu::setPoberatel));




        Binder.Binding<PolozkaDokladu, String> poznamkaBinding = binder.forField(tPoznamka)
                .bind(PolozkaDokladu::getPoznamka, PolozkaDokladu::setPoznamka);

        Binder.Binding<PolozkaDokladu, BigDecimal> bodyBinding = binder.forField(tBody)
                .withConverter(new StringToBigDecimalConverter("Nie je číslo"))
                .bind(PolozkaDokladu::getBody, PolozkaDokladu::setBody);




   // tCislo.addValueChangeListener(event -> cisloDokladuBinding.validate());

    btnUloz.setStyleName(ValoTheme.BUTTON_PRIMARY);
    btnUloz.addClickListener(this::save);





}

    private boolean prepocitajBody() {
        if (aktualnyProdukt==null)
            return true;
        if (polozkaEditovana.getBody()==null  )
            return true;

        polozkaEditovana.setBody(new BigDecimal(VypoctyUtil.vypocitajBody(new BigDecimal(tMnozstvo.getValue()),
                BigDecimal.ONE,
                aktualnyProdukt.getKusy(),
                aktualnyProdukt.getBody())));
        tBody.setValue(polozkaEditovana.getBody().toBigInteger().toString());
        return true;
    }

    void edit(PolozkaDokladu polozkaDokladu) {
        staraEditovana=polozkaEditovana;
        polozkaEditovana = polozkaDokladu;


        if (polozkaDokladu != null) {
            binder.readBean(polozkaDokladu);
            aktualnyPoberatel=polozkaDokladu.getPoberatel();
            if (polozkaDokladu.getId()==null){
                polozkaDokladu.setProdukt(null);
                if (polozkyDokladyView.getDoklad().jeDokladDavka())
                    polozkaDokladu.setMnozstvo(new BigDecimal(1));
                else
                    polozkaDokladu.setMnozstvo(null);
            }

        }
        else{
            binder.readBean(polozkaDokladu);}
    }

    public void save(Button.ClickEvent event) {
        if (binder.writeBeanIfValid(polozkaEditovana)) {
            boolean jeDokladNovy = polozkaEditovana.isNew();
            polozkaEditovana.setDoklad(polozkyDokladyView.getDoklad());
            PolozkaDokladu ulozenaPolozka = PolozkaDokladuNastroje.ulozPolozkuDokladu(polozkaEditovana);
            String msg = String.format("Ulozeny .");

            Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
            if (jeDokladNovy){
                polozkyDokladyView.pridajNovuPolozkuDokladu(ulozenaPolozka);
                polozkyDokladyView.povodnaPolozka =ulozenaPolozka;
            }
            polozkyDokladyView.refreshPoloziekDokladov();
            polozkyDokladyView.selectPolozkuDokladu(ulozenaPolozka);

        }

    }


    public void setDokladyView(PolozkyDokladuView polozkyDokladuView) {
        this.polozkyDokladyView = polozkyDokladuView;
    }

    private List<Prevadzka> navrhniPrevadzku(String query, int cap) {
        return  PrevadzkaNastroje.zoznamPrevadzokPodlaMena(query).stream()
                .filter(prevadzka -> prevadzka.getNazov().toLowerCase().contains(query.toLowerCase()))
                .limit(30).collect(Collectors.toList());
    }
    /**
     * Co sa zobraziv textfielde, ked sa uz hodnota vyberie
     * */
    private String transformujPrevadzkuNaNazov(Prevadzka prevadzka) {
        return prevadzka.getNazov();
    }
    /**
     * Co sa zobrazi v dropdowne
     * */
    private String transformujPrevádzkuNaNazovSoZvyraznenymQuery(Prevadzka prevadzka, String query) {
        return "<div class='suggestion-container'>"
                + "<span class='prevadzka'>"
                + prevadzka.getNazov()
                .replaceAll("(?i)(" + query + ")", "<b>$1</b>")
                + "</span>"
                + "</div>";
    }
    private List<Produkt> navrhniProdukt(String query, int cap) {
        return ProduktyNastroje.zoznamProduktovZaRok(null).stream()
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
    private void clearEditacnyForm() {
        tProdukt.clear();
        tPrevadzka.clear();
        tPoberatel.clear();
        tBody.clear();
        tMnozstvo.clear();
        tPoznamka.clear();
    }


    public void zakazNepotrebne(){
        if (polozkyDokladyView.getDoklad().jeDokladDavka()){
            tMnozstvo.setVisible(true);
            Binder.Binding<PolozkaDokladu, BigDecimal> mnozstvoBinding = binder.forField(tMnozstvo)
                    .withValidator(mnozstvo -> this.prepocitajBody(),
                            "OK")
                    .withConverter(new StringToBigDecimalConverter("Nie je číslo"))
                    .bind(PolozkaDokladu::getMnozstvo, PolozkaDokladu::setMnozstvo);

            tProdukt.setVisible(true);
            Binder.Binding<PolozkaDokladu, String> produktBinding = binder.forField(tProdukt)
                    .withValidator(nazovProduktu -> ProduktyNastroje.prvyProduktPodlaNazvu(nazovProduktu).isPresent(),
                            "Produkt musi byt existujuci")
                    .bind(polozkaDokladu -> polozkaDokladu.getProdukt() == null ? "" : polozkaDokladu.getProdukt().getNazov(),
                            (polozkaDokladu, s) -> ProduktyNastroje.prvyProduktPodlaNazvu(tProdukt.getValue()).ifPresent(polozkaDokladu::setProdukt));


        }
        else{
            tMnozstvo.setVisible(false);
            tProdukt.setVisible(false);}

    }

    public void rezimVelkoskladu() {
        btnUloz.setVisible(false);

    }
}


