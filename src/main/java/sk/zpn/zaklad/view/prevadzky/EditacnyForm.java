package sk.zpn.zaklad.view.prevadzky;

import com.vaadin.data.Binder;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.log4j.Logger;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.*;
import sk.zpn.zaklad.model.FirmaNastroje;
import sk.zpn.zaklad.model.PoberatelNastroje;

import sk.zpn.zaklad.model.PrevadzkaNastroje;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EditacnyForm extends VerticalLayout {

    private static final Logger logger = Logger.getLogger(EditacnyForm.class);

    private TextField tNazov;
    private TextField tMesto;
    private TextField tPsc;
    private TextField tUlica;
    private TextField tNazovFirmy;
    private TextField tMenoPoberatela;



    protected Button btnUloz;
    protected Button btnZmaz;
    private final Binder<Prevadzka> binder = new Binder<>();
    private Prevadzka prevadzkaEditovana;
    private PrevadzkyView prevadzkyView;
    private Poberatel aktualnyPoberatel;
    private Firma aktualnaFirma;

    public EditacnyForm(){
    }

    public EditacnyForm(PrevadzkyView components){
        this.aktualnyPoberatel= null;
        this.prevadzkyView=components;
        tNazov = new TextField("Prevádzka");
        tNazov.setWidth("400");

        tNazovFirmy = new TextField("Firma");
        tNazovFirmy.setWidth("400");
        if (this.prevadzkyView.getFirma()!=null)
            tNazovFirmy.setEnabled(false);

        tMesto = new TextField("Mesto");
        tMesto.setWidth("400");

        tPsc = new TextField("Psč");
        tPsc.setWidth("150");

        tUlica = new TextField("Ulica");
        tUlica.setWidth("400");

        tMenoPoberatela = new TextField("Poberateľ" );
                tMenoPoberatela.setWidth("400");



        btnUloz=new Button("Ulož", VaadinIcons.CHECK_CIRCLE);
        btnUloz.setClickShortcut(ShortcutAction.KeyCode.U,
                new int[]{ShortcutAction.ModifierKey.ALT});

        btnZmaz =new Button("Zmaž",VaadinIcons.CLOSE_CIRCLE);
        nastavComponnenty();
        FormLayout lEdit=new FormLayout();

        lEdit.addComponent(tNazov);
        lEdit.addComponent(tNazovFirmy);
        lEdit.addComponent(tMesto);
        lEdit.addComponent(tPsc);
        lEdit.addComponent(tUlica);
        lEdit.addComponent(tMenoPoberatela);


        HorizontalLayout lBtn=new HorizontalLayout();
        lBtn.addComponent(btnUloz);
        lBtn.addComponent(btnZmaz);



         this.addComponent(lEdit);
         this.addComponent(lBtn);

        AutocompleteExtension<Firma> firmaAutocompleteExtension = new AutocompleteExtension<>(tNazovFirmy);
        firmaAutocompleteExtension.setSuggestionListSize(50);
        firmaAutocompleteExtension.setSuggestionGenerator(
            this::navrhniFirmu,
            this::transformujFirmuNaNazov,
            this::transformujFirmuNaNazovSoZvyraznenymQuery);
        firmaAutocompleteExtension.addSuggestionSelectListener(event -> {
            event.getSelectedItem().ifPresent(firma -> this.aktualnaFirma = firma);
        });


        AutocompleteExtension<Poberatel> poberatelAutocompleteExtension = new AutocompleteExtension<>(tMenoPoberatela);
        poberatelAutocompleteExtension.setSuggestionGenerator(
            this::navrhniPoberatela,
            this::transformujPoberatelaNaMeno,
            this::transformujPoberatelaNaMenoSoZvyraznenymQuery);

        poberatelAutocompleteExtension.addSuggestionSelectListener(event -> {
            event.getSelectedItem().ifPresent(poberatel -> this.aktualnyPoberatel = poberatel);
        });


    }
    private void nastavComponnenty(){

    Binder.Binding<Prevadzka, String> nazovBinding = binder.forField(tNazov)
        .withValidator(v -> !tNazov.getValue().trim().isEmpty(),
        "Názov prevádzky je povinný")
        .bind(Prevadzka::getNazov, Prevadzka::setNazov);


//        Binder.Binding<Prevadzka, String> firmaBinding = binder.forField(tNazovFirmy)
//                .withValidator(v ->  this.aktualnaFirma != null,"Názov firmy je povinný")
//             //  .withValidator(nazovFirmy -> FirmaNastroje.firmaPodlaID(this.aktualnaFirma.getId()).isPresent(),"Firma musi byt existujuca")
//                .bind(prevadzka -> prevadzka.getFirma() == null ? "" : prevadzka.getFirma().getNazov(),
//                        (firma, s) -> FirmaNastroje.firmaPodlaID(this.aktualnaFirma.getId()).ifPresent(firma::setFirma));

        Binder.Binding<Prevadzka, String> firmaBinding = binder.forField(tNazovFirmy)
                .withValidator(nazovFirmy -> !tNazovFirmy.getValue().trim().isEmpty(),
                        "Firma je povinna")
                .withValidator(nazovFirmy -> FirmaNastroje.prvaFirmaPodlaNazvu(nazovFirmy).isPresent(),
                        "Firma musi byt existujuca")
                .bind(prevadzka -> prevadzka.getFirma() == null ? "" : prevadzka.getFirma().getNazov(),
                        (prevadzka, s) -> FirmaNastroje.prvaFirmaPodlaNazvu(tNazovFirmy.getValue()).ifPresent(prevadzka::setFirma));





        Binder.Binding<Prevadzka, String> MestoBinding = binder.forField(tMesto)
                .bind(Prevadzka::getMesto, Prevadzka::setMesto);
        Binder.Binding<Prevadzka, String> PscBinding = binder.forField(tPsc)
                .bind(Prevadzka::getPsc, Prevadzka::setPsc);
        Binder.Binding<Prevadzka, String> UlicaBinding = binder.forField(tUlica)
                .bind(Prevadzka::getUlica, Prevadzka::setUlica);

        Binder.Binding<Prevadzka, String> poberatelBinding = binder.forField(tMenoPoberatela)
            .withValidator(nazovPoberatel -> this.aktualnyPoberatel != null, "Poberteľ musi bzt vyplneny")
            .withValidator(nazovPoberatela -> PoberatelNastroje.poberatelPodlaId(this.aktualnyPoberatel.getId()).isPresent(),
        "Poberateľ musi byt existujuci")
            .bind(prevadzka -> prevadzka.getPoberatel() == null ? "" : prevadzka.getPoberatel().getMeno(),
                (prevadzka, s) -> PoberatelNastroje.poberatelPodlaId(this.aktualnyPoberatel.getId()).ifPresent(prevadzka::setPoberatel));




    tNazov.addValueChangeListener(event -> nazovBinding.validate());
    tNazovFirmy.addValueChangeListener(event -> firmaBinding.validate());
    //toto by sa malo doriest asi dame poberatelovi iba meno
    tMenoPoberatela.addValueChangeListener(event -> poberatelBinding.validate());

    btnUloz.setStyleName(ValoTheme.BUTTON_PRIMARY);
    btnUloz.addClickListener(this::save);

    btnZmaz.addClickListener(this::delete);


}
    void edit(Prevadzka prevadzka) {
        prevadzkaEditovana = prevadzka;
        aktualnyPoberatel=prevadzka.getPoberatel();
        if (prevadzka != null) {
            binder.readBean(prevadzka);
        }
        else{
            binder.readBean(prevadzka);}
    }

    public void save(Button.ClickEvent event) {
        if (binder.writeBeanIfValid(prevadzkaEditovana)) {
            boolean jePrevadzkaNova = prevadzkaEditovana.isNew();
            Prevadzka ulozenaPrevadzka = PrevadzkaNastroje.ulozPrevadzka(prevadzkaEditovana);
            String msg = String.format("Ulozena .",
                    prevadzkaEditovana.getNazov());

            Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
            if (jePrevadzkaNova){
                prevadzkyView.pridajNovuPrevadzku(ulozenaPrevadzka);
            }
            prevadzkyView.refreshPrevadzok();
            prevadzkyView.selectPrevadzku(ulozenaPrevadzka);

        }

    }

    public void delete(Button.ClickEvent event) {
        if (!Optional.ofNullable(prevadzkaEditovana).isPresent()) {
            return;
        }

        ConfirmDialog.show(UI.getCurrent(), "Odstránenie prevadzky", "Naozaj si prajete odstrániť prevadzky "+ prevadzkaEditovana.getNazov()+"?",
                "Áno", "Nie", new ConfirmDialog.Listener() {

                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue
                            PrevadzkaNastroje.zmazPrevadzku(prevadzkaEditovana);
                            prevadzkyView.odstranPrevadzku(prevadzkaEditovana);
                            Notification.show("Doklad odstránený", Notification.Type.TRAY_NOTIFICATION);
                            clearEditacnyForm();
                            prevadzkyView.selectFirst();
                        }
                    }
        });

    }

    public void setPrevadzkyView(PrevadzkyView prevadzkyView) {
        this.prevadzkyView = prevadzkyView;
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
    private List<Poberatel> navrhniPoberatela(String query, int cap) {

        List<Poberatel> poberiatelia = PoberatelNastroje.zoznamPoberatelov(null).stream()
                .filter(poberatel -> poberatel.getMeno().toLowerCase().contains(query.toLowerCase()))
                .limit(cap).collect(Collectors.toList());
        poberiatelia.forEach(poberatel -> {
                logger.info(poberatel.getId().toString() + "  " + poberatel.getMesto());
        });
        return poberiatelia;
    }
    /**
     * Co sa zobraziv textfielde, ked sa uz hodnota vyberie
     * */
    private String transformujPoberatelaNaMeno(Poberatel poberatel) {
        logger.info("Vybraty poberate "+ poberatel.getPoberatelMenoAdresa()+ poberatel.getId());
        return poberatel.getMeno();

    }
    /**
     * Co sa zobrazi v dropdowne
     * */
    private String transformujPoberatelaNaMenoSoZvyraznenymQuery(Poberatel poberatel, String query) {
        return "<div class='suggestion-container'>"
                + "<span class='poberatel'>"
                + poberatel.getPoberatelMenoAdresa()
//                + poberatel.getMeno()
                .replaceAll("(?i)(" + query + ")", "<b>$1</b>")
                + "</span>"
                + "</div>";
    }
    private void clearEditacnyForm() {
        tNazov.clear();
        tUlica.clear();
        tPsc.clear();
        tMesto.clear();
        tMenoPoberatela.clear();
        tNazovFirmy.clear();


    }
}
