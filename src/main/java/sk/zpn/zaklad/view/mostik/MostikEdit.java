package sk.zpn.zaklad.view.mostik;

import com.sun.javafx.tk.TKDropTargetListener;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import sk.zpn.domena.FirmaProdukt;
import sk.zpn.domena.Produkt;
import sk.zpn.zaklad.model.FirmaProduktNastroje;
import sk.zpn.zaklad.model.ProduktyNastroje;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MostikEdit {
    private TextField tKat;
    private TextField tKoeficient;
    private TextField tKit;
    private Button btnUloz;
    private Button btnStorno;
    private final Binder<FirmaProdukt> binder = new Binder<>();
    private FirmaProdukt fpEditovana;
    private MostikView mostikView;
    private Window subWindow;

    public MostikEdit(FirmaProdukt fp, MostikView mv){
        fpEditovana=fp;
        mostikView =mv;
        tKat = new TextField("   KAT");
        tKat.setMaxLength(150);
        tKat.setWidth(90, Sizeable.Unit.PERCENTAGE);
        tKoeficient = new TextField("    Koeficient");
        tKoeficient.setMaxLength(10);

        //tKoeficient.setWidth("30");
        tKit = new TextField("   KIT");
        tKit.setMaxLength(50);

        btnUloz=new Button("Ulož", VaadinIcons.CHECK_CIRCLE);
        btnStorno =new Button("Koniec",VaadinIcons.CLOSE_CIRCLE);

        nastavComponnenty();
        FormLayout lEdit = new FormLayout();
        lEdit.addComponent(tKat);
        lEdit.addComponent(tKoeficient);
        lEdit.addComponent(tKit);


        HorizontalLayout lBtn = new HorizontalLayout();

        lBtn.addComponent(btnUloz);
        btnUloz.addClickListener(this::save);
        lBtn.addComponent(btnStorno);
        btnStorno.addClickListener(this::koniec);



        subWindow = new Window("Pridanie");
        GridLayout gl =new GridLayout(1,3);
        gl.setSizeFull();
        gl.setSpacing(true);
        gl.setRowExpandRatio(0, 0.05f);
        gl.setRowExpandRatio(1, 0.90f);
        gl.setRowExpandRatio(2, 0.05f);
        gl.addComponent(new Label("Mostik"));

        gl.addComponent(lEdit);
        gl.setComponentAlignment(lEdit,Alignment.MIDDLE_CENTER);
        gl.addComponent(lBtn);
        gl.setComponentAlignment(lBtn,Alignment.BOTTOM_CENTER);


        subWindow.setWidth(550, Sizeable.Unit.PIXELS);
        subWindow.setHeight(350, Sizeable.Unit.PIXELS);
        subWindow.setContent(gl);
        subWindow.setModal(true);





        subWindow.center();
        UI.getCurrent().addWindow(subWindow);

    }


    private void nastavComponnenty() {
        binder.readBean(fpEditovana);
        tKoeficient.setValue(fpEditovana.getKoeficient().toString());
        tKat.setValue(fpEditovana.getProdukt().getKat());

        Binder.Binding<FirmaProdukt, String> produktBinding = binder.forField(tKat)
                .withValidator(katProduktu -> ProduktyNastroje.prvyProduktPodlaKat(katProduktu).isPresent(),
                        "Produkt musi byt existujuci")
                .bind(firmaProdukt -> firmaProdukt.getProdukt() == null ? "" : firmaProdukt.getProdukt().getKat(),
                        (firmaProdukt, s) -> ProduktyNastroje.prvyProduktPodlaKat(tKat.getValue()).ifPresent(firmaProdukt::setProdukt));



        Binder.Binding<FirmaProdukt, String> getKit= binder.forField(tKit)
                .bind(FirmaProdukt::getKit, FirmaProdukt::setKit);


        Binder.Binding<FirmaProdukt, BigDecimal> koeficient = binder.forField(tKoeficient)
                .withConverter(new StringToBigDecimalConverter("Nie je číslo"))
                .withValidator(koef -> !tKoeficient.getValue().trim().isEmpty(),
                        "Koeficient je povinný")
                .bind(FirmaProdukt::getKoeficient, FirmaProdukt::setKoeficient);



        // tCislo.addValueChangeListener(event -> cisloDokladuBinding.validate());

        AutocompleteExtension<Produkt> firmaProduktAutocompleteExtensionProdukt = new AutocompleteExtension<>(tKat);
        firmaProduktAutocompleteExtensionProdukt.setSuggestionGenerator(
                this::navrhniProdukt,
                this::transformujProduktNaKat,
                this::transformujProduktNaKatSoZvyraznenymQuery);




    }







    private void save(Button.ClickEvent clickEvent) {
        boolean jeNova=false;
        if (binder.writeBeanIfValid(fpEditovana)) {
            if (fpEditovana.isNew()) {
                FirmaProduktNastroje.nastavAtributyPriNovej(fpEditovana);
                jeNova=true;
            }
            FirmaProdukt ulozenyFirmaProdukt = FirmaProduktNastroje.pridajFirmaProdukt(fpEditovana);

            if (jeNova) {
                mostikView.pridajNovy(ulozenyFirmaProdukt);

            }


        }
        subWindow.close();
    }
    private void koniec(Button.ClickEvent clickEvent) {
        subWindow.close();
    }

    private List<Produkt> navrhniProdukt(String query, int cap) {
        return ProduktyNastroje.zoznamProduktovZaRok().stream()
                .filter(produkt -> produkt.getKat().toLowerCase().contains(query.toLowerCase()))
                .limit(cap).collect(Collectors.toList());
    }
    /**
     * Co sa zobraziv textfielde, ked sa uz hodnota vyberie
     * */
    private String transformujProduktNaKat(Produkt produkt) {
        return produkt.getKat();

    }
    /**
     * Co sa zobrazi v dropdowne
     * */
    private String transformujProduktNaKatSoZvyraznenymQuery(Produkt produkt, String query) {
        return "<div class='suggestion-container'>"
                + "<span class='produkt'>"
                + produkt.getKat()
                +"  "
                +produkt.getNazov()
                .replaceAll("(?i)(" + query + ")", "<b>$1</b>")
                + "</span>"
                + "</div>";
    }




}
