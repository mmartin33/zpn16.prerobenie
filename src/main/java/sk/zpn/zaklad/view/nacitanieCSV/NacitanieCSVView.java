package sk.zpn.zaklad.view.nacitanieCSV;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import sk.zpn.domena.Firma;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.domena.importy.ParametreImportu;
import sk.zpn.domena.importy.VysledokImportu;
import sk.zpn.zaklad.model.FirmaNastroje;
import sk.zpn.zaklad.model.UzivatelNastroje;
import sk.zpn.zaklad.view.VitajteView;

import java.sql.Date;
import java.time.LocalDate;

import java.util.List;
import java.util.stream.Collectors;


public class NacitanieCSVView extends VerticalLayout implements View {

    public static final String NAME = "nacitanieDbfView";
    private final UploadCSV ue;
    private VysledokImportu vysledokImportu;
    private BrowsPanelVysledkov browsPanelVysledkov;
    private FormLayout frmVstupneUdaje;
    private final Binder<Firma> binderHF = new Binder<>();
    private TextField tfFirma;
    private DateField tfobdobie;
    private String nazovFirmy = "";
    private Button btnOK;
    private Button btnSpat;

    public NacitanieCSVView(){

        ue=new UploadCSV(this);
        ue.init();
        ue.setVisible(false);
        frmVstupneUdaje=new FormLayout();

        tfFirma=new TextField("Velkosklad");
        tfFirma.setWidth(450, Sizeable.Unit.PIXELS);
        tfobdobie=new DateField("Dátum dokladu:");
        btnOK =new Button("Pokračuj", VaadinIcons.CHECK_CIRCLE);
        btnSpat =new Button("Späť",VaadinIcons.CLOSE_CIRCLE);
        HorizontalLayout lBtn=new HorizontalLayout();
        lBtn.addComponent(btnOK);
        lBtn.addComponent(btnSpat);

        btnOK.addClickListener(this::pokracujNaVyberSuboru);
        btnSpat.addClickListener(this::spat);
        btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        frmVstupneUdaje.addComponent(new Label("Údaje pre doklad dávky. Ich potvrdením sa dostanete k výberu súboru"));
        frmVstupneUdaje.addComponent(tfFirma);
        frmVstupneUdaje.addComponent(tfobdobie);
        frmVstupneUdaje.addComponent(lBtn);
        this.addComponent(frmVstupneUdaje);
        this.addComponent(ue);
        browsPanelVysledkov=new BrowsPanelVysledkov();
        browsPanelVysledkov.setFirma(nazovFirmy);

        this.addComponentsAndExpand(browsPanelVysledkov);
        browsPanelVysledkov.setVisible(false);
        this.init();



    }

    private void spat(Button.ClickEvent clickEvent) {
        UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME);

    }

    private void pokracujNaVyberSuboru(Button.ClickEvent clickEvent) {
        Firma firma=FirmaNastroje.prvaFirmaPodlaNazvu(tfFirma.getValue()).get();
        if ((firma!=null) && (tfobdobie.getValue().toString()!="")){
            browsPanelVysledkov.setFirma(nazovFirmy);
            frmVstupneUdaje.setVisible(false);
            ue.setVisible(true);
            ParametreImportu parametreImportu=new ParametreImportu(firma, Date.valueOf(tfobdobie.getValue()));
            ue.nastavAdresar(firma.getIco());
            ue.parametreImportu=parametreImportu;
        }


    }

    public VysledokImportu getVysledokImportu() {
        return vysledokImportu;
    }

    public void init(){
        if (UzivatelNastroje.getPrihlasenehoUzivatela().getTypUzivatela()!=TypUzivatela.SPRAVCA_ZPN) {
            tfFirma.setValue(UzivatelNastroje.getPrihlasenehoUzivatela().getFirma().getNazov());
            nazovFirmy = UzivatelNastroje.getPrihlasenehoUzivatela().getFirma().getNazov();
            tfFirma.setEnabled(false);
        }
            tfFirma.setEnabled(true);



            tfobdobie.setValue(LocalDate.now());
            binderHF.readBean(FirmaNastroje.prvaFirmaPodlaNazvu(nazovFirmy).get());
            Binder.Binding<Firma, String> nazovBinding = binderHF.forField(tfFirma)
                    .withValidator(v -> !tfFirma.getValue().trim().isEmpty(),
                            "Názov je poviný")
                    .bind(Firma::getNazov, Firma::setNazov);


            AutocompleteExtension<Firma> dokladAutocompleteExtension = new AutocompleteExtension<>(tfFirma);
            dokladAutocompleteExtension.setSuggestionListSize(50);
            dokladAutocompleteExtension.setSuggestionGenerator(
                    this::navrhniFirmu,
                    this::transformujFirmuNaNazov,
                    this::transformujFirmuNaNazovSoZvyraznenymQuery);

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


    public void setVysledokImportu(VysledokImportu vysledokImportu) {
        this.vysledokImportu = vysledokImportu;
        this.removeComponent(frmVstupneUdaje);
        this.removeComponent(ue);

        this.browsPanelVysledkov.setVysledokImportu(vysledokImportu);


    }
}

