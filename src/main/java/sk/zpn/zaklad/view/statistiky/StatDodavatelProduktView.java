package sk.zpn.zaklad.view.statistiky;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import sk.zpn.domena.Firma;
import sk.zpn.zaklad.model.FirmaNastroje;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.model.StatDodavatelProdukt;
import sk.zpn.zaklad.view.VitajteView;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatDodavatelProduktView extends VerticalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "statDodavatelProduktView";
    private Button btnAktivujFilter;

    private Button btnSpat;
    private Map <String, Firma> zoznamDodavatelov;
    private final Binder<Firma> binderHF = new Binder<>();
    private TextField tfFirma;

    private String nazovFirmy = "";
    DateField dfOd;
    DateField dfDo;
    TextField txtRok;
    LocalDate dod;
    LocalDate ddo;
    String rok;
    private boolean bodovyRezim=false;

    public StatDodavatelProduktView() {

        HorizontalLayout hornyFilter =new HorizontalLayout();
        HorizontalLayout hornyFilter2 =new HorizontalLayout();

        rok= ParametreNastroje.nacitajParametre().getRok();
        dod = LocalDate.of(LocalDate.now().getYear(),1,1);
        ddo = LocalDate.of(LocalDate.now().getYear(),12,31);
        dfOd=new DateField("Od:");
        dfDo=new DateField("do:");
        txtRok=new TextField("Rok:");
        txtRok.setValue(rok);
        dfOd.setValue(dod);
        dfDo.setValue(ddo);
        dfOd.setWidth(15, Unit.PERCENTAGE);
        dfDo.setWidth(15, Unit.PERCENTAGE);
        txtRok.setWidth(15, Unit.PERCENTAGE);
        tfFirma=new TextField("Velkosklad");
        tfFirma.setWidth(450, Sizeable.Unit.PIXELS);

        btnAktivujFilter=new Button("Vytvor XLS");

        Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnSpat.setHeight(100, Unit.PERCENTAGE);
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );

        btnAktivujFilter.setWidth(10, Unit.PERCENTAGE);
        btnAktivujFilter.setHeight(100, Unit.PERCENTAGE);
        btnAktivujFilter.addClickListener(this::aktivujFilter);
        hornyFilter.addComponent(dfOd);
        hornyFilter.addComponent(dfDo);
        hornyFilter.addComponent(txtRok);
        hornyFilter2.addComponent(tfFirma);
        hornyFilter2.addComponent(btnAktivujFilter);
        hornyFilter2.addComponent(btnSpat);


        GridLayout gl =new GridLayout(1,2);
        gl.setSpacing(false);
        gl.setSizeFull();
        gl.setColumnExpandRatio(0,1f);
        gl.setRowExpandRatio(0, 1f);


        this.addComponent(new Label("Dodávatelia "));
        this.addComponent(hornyFilter);
        this.addComponent(hornyFilter2);


        this.setSizeFull();
        this.addComponentsAndExpand(gl);

        gl.setVisible(true);

        this.addComponentsAndExpand(gl);
        configureComponents();
        this.init();



    }

    private void aktivujFilter(Button.ClickEvent clickEvent) {
        Firma firma=FirmaNastroje.prvaFirmaPodlaNazvu(tfFirma.getValue()).get();
        StatDodavatelProdukt.load(bodovyRezim,dfOd.getValue(), dfDo.getValue(),Integer.parseInt(txtRok.getValue()),firma);
        }


    private void init(){
        nazovFirmy=FirmaNastroje.zoznamFiriemIbaDodavatelia().get(0).getNazov();
        tfFirma.setValue(nazovFirmy);
        binderHF.readBean(FirmaNastroje.prvaFirmaPodlaNazvu(nazovFirmy).get());
        Binder.Binding<Firma, String> nazovBinding = binderHF.forField(tfFirma)
                .withValidator(v -> !tfFirma.getValue().trim().isEmpty(),
                        "Názov je poviný")
                .bind(Firma::getNazov, Firma::setNazov);
        AutocompleteExtension<Firma> dokladAutocompleteExtension = new AutocompleteExtension<>(tfFirma);
        dokladAutocompleteExtension.setSuggestionListSize(50);
        dokladAutocompleteExtension.addSuggestionSelectListener(event -> {
            event.getSelectedItem().ifPresent(this::vybratyDodavtel);;
        });
        dokladAutocompleteExtension.setSuggestionGenerator(
                this::navrhniFirmu,
                this::transformujFirmuNaNazov,
                this::transformujFirmuNaNazovSoZvyraznenymQuery);

    }

    private void vybratyDodavtel(Firma firma) {
        System.out.println(firma.getNazov());
    }


    private void configureComponents() {





    }





    private List<Firma> navrhniFirmu(String query, int cap) {
        return  FirmaNastroje.zoznamFiriemIbaDodavatelia().stream()
                .filter(firma -> firma.getNazov().toLowerCase().contains(query.toLowerCase()))
                .onClose(() -> {
                    System.out.println("-- closing stream --");
                })
                .limit(100).collect(Collectors.toList());

//                .limit(cap).collect(Collectors.toList());
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


    public void setBodovyRezim() {
        this.bodovyRezim=true;
    }
}

