package sk.zpn.zaklad.view.mostik;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import org.vaadin.addons.autocomplete.AutocompleteExtension;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.Firma;
import sk.zpn.domena.FirmaProdukt;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.model.FirmaNastroje;
import sk.zpn.zaklad.model.FirmaProduktNastroje;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.model.UzivatelNastroje;
import sk.zpn.zaklad.view.VitajteView;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static sk.zpn.zaklad.model.UzivatelNastroje.getUzivatela;


public class MostikView extends HorizontalLayout implements View {

    // ContactForm is an example of a custom component class
    public static final String NAME = "mostikView";
    private BrowsPanel browsPanel;


    private GridLayout mainGridLayout =  new GridLayout(1,4);
    private HorizontalLayout upperLabelHorizontalLayout = new HorizontalLayout();
    private HorizontalLayout tlacitkovyLayout = new HorizontalLayout();
    private String nazovFirmy = "";
    private String rok = "";
    private Label firmaLabel =  new Label("<b>Firma: </b>", ContentMode.HTML);
    private Label rokLabel =  new Label("<b>Rok: </b>", ContentMode.HTML);
    private TextField txtFirma;
    private final Binder<Firma> binderHF = new Binder<>();

    private Button btnFirma;
    private List<FirmaProdukt> firmaProduktList;
    private Button btnZmaz = new Button("Zmaž", VaadinIcons.CLOSE_CIRCLE);;
    private Button btnNova = new Button("Pridaj", VaadinIcons.FILE_O);;
    private Button btnKitTak = new Button("Vyplniť Kat podľa Kit", VaadinIcons.CONTROLLER);;
    private Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);


    public MostikView() {

        mainGridLayout.setSizeFull();
        mainGridLayout.setRowExpandRatio(0, 0.05f);
        mainGridLayout.setRowExpandRatio(1, 0.05f);
        mainGridLayout.setRowExpandRatio(2, 0.85f);
        mainGridLayout.setRowExpandRatio(3, 0.05f);

        mainGridLayout.setSpacing(true);
        configureComponents();
        tlacitkovyLayout.addComponent(btnNova);
        tlacitkovyLayout.addComponent(btnZmaz);
        tlacitkovyLayout.addComponent(btnKitTak);
        tlacitkovyLayout.addComponent(btnSpat);
        upperLabelHorizontalLayout.addComponent(firmaLabel);
        upperLabelHorizontalLayout.addComponent(rokLabel);
        upperLabelHorizontalLayout.addComponent(new Label("Dvojklikom opraviť KIT a koeficient "));

        mainGridLayout.addComponent(upperLabelHorizontalLayout);
        HorizontalLayout hornyFilter =new HorizontalLayout();

        txtFirma.setWidth(10,Sizeable.Unit.PERCENTAGE);
        btnFirma.setWidth(10,Sizeable.Unit.PERCENTAGE);
        hornyFilter.addComponent(txtFirma);
        hornyFilter.addComponent(btnFirma);

        mainGridLayout.addComponent(hornyFilter);
        mainGridLayout.addComponent(browsPanel);
        mainGridLayout.setComponentAlignment(browsPanel,Alignment.MIDDLE_LEFT);
        mainGridLayout.addComponent(tlacitkovyLayout);
        //mainGridLayout.setComponentAlignment(tlacitkovyLayout,Alignment.BOTTOM_LEFT);
        this.addComponent(mainGridLayout);
        this.setSizeFull();
        this.setVisible(true);


        btnZmaz.addClickListener(this::delete);
        btnKitTak.addClickListener(this::dajDoKitKat);
        btnNova.addClickListener(this::novy);
        btnSpat.addClickListener(clickEvent ->
            UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );
        btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    }

    private void dajDoKitKat(Button.ClickEvent clickEvent) {

        ConfirmDialog.show(UI.getCurrent(), "Hromadná aktualizácia KIT",
                "Naozaj si prajete prepísať KAT do KIT ?",
                "Áno", "Nie", new ConfirmDialog.Listener() {
                    public void onConfirmet(ConfirmDialog dialog) {

                    }
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            dialog.setVisible(false);
                            dialog.setEnabled(false);
                            dialog.close();
                            Notification.show("Prebieha aktualizácia", Notification.Type.TRAY_NOTIFICATION);
                            // Confirmed to continue
                            FirmaProduktNastroje.prepisKATdoKIT(firmaProduktList);
                            firmaProduktList.clear();
                            firmaProduktList=FirmaProduktNastroje.getListFirmaProduktPodlaNazvuFirmy(nazovFirmy);
                            browsPanel.setFirmaProduktList(firmaProduktList);
                            browsPanel.refresh();
                            Notification.show("Aktualizované", Notification.Type.TRAY_NOTIFICATION);
                            browsPanel.selectFirst();
                        }
                    }
                });

    }

    private void novy(Button.ClickEvent clickEvent) {
        if (browsPanel.getOznacenyFirmaProdukt().getProdukt()==null)
            return;
        FirmaProdukt novyFirmaProdukt=new FirmaProdukt();
        novyFirmaProdukt.setKoeficient(new BigDecimal(1));
        novyFirmaProdukt.setProdukt(browsPanel.getOznacenyFirmaProdukt().getProdukt());

        MostikEdit me=new MostikEdit(novyFirmaProdukt,this);


    }

    void deselect() {
        browsPanel.deselect();
    }

    private void configureComponents() {

        Optional<Uzivatel> loggedUzivatel = getUzivatela((Long) VaadinSession.getCurrent().getAttribute("id_uzivatela"));
        loggedUzivatel.ifPresent( uzivatel -> {
            nazovFirmy =  uzivatel.getFirma().getNazov();
            firmaLabel.setValue(firmaLabel.getValue() + nazovFirmy);
        });
        rok = ParametreNastroje.nacitajParametre().getRok();
        rokLabel.setValue(rokLabel.getValue() + rok);




        btnFirma=new Button("Prezobraz");
        btnFirma.setWidth("150");
        btnFirma.addClickListener(this::aktivujHF);
        btnFirma.setEnabled(false);
        txtFirma=new TextField();
        txtFirma.setWidth("400");
        txtFirma.setValue(nazovFirmy);
        txtFirma.setEnabled(false);
        if (UzivatelNastroje.jeUzivatelAdminAleboSpravca()) {
            txtFirma.setEnabled(true);
            btnFirma.setEnabled(true);
        }
        FirmaProduktNastroje.generateMissingFirmaProductItems(nazovFirmy);
        firmaProduktList = FirmaProduktNastroje.getListFirmaProduktPodlaNazvuFirmy(nazovFirmy);
        browsPanel = new BrowsPanel(firmaProduktList, nazovFirmy);


        binderHF.readBean(FirmaNastroje.prvaFirmaPodlaNazvu(nazovFirmy).get());

        Binder.Binding<Firma, String> nazovBinding = binderHF.forField(txtFirma)
                .withValidator(v -> !txtFirma.getValue().trim().isEmpty(),
                        "Názov je poviný")
                .bind(Firma::getNazov, Firma::setNazov);


        AutocompleteExtension<Firma> dokladAutocompleteExtension = new AutocompleteExtension<>(txtFirma);
        dokladAutocompleteExtension.setSuggestionGenerator(
                this::navrhniFirmu,
                this::transformujFirmuNaNazov,
                this::transformujFirmuNaNazovSoZvyraznenymQuery);


        refreshMostika();
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


    private void aktivujHF(Button.ClickEvent clickEvent) {
        if (txtFirma.getValue().length()>0) {
            nazovFirmy = txtFirma.getValue();
            browsPanel.refresh(nazovFirmy);
            firmaProduktList.clear();
            firmaProduktList=FirmaProduktNastroje.getListFirmaProduktPodlaNazvuFirmy(nazovFirmy);
            browsPanel.setFirmaProduktList(firmaProduktList);
            browsPanel.refresh();


        }

    }

    public void refreshMostika() {
            browsPanel.refresh();
    }


    void delete(Button.ClickEvent event) {
        FirmaProdukt oznacenyFirmaProdukt = browsPanel.getOznacenyFirmaProdukt();
        if (!Optional.ofNullable(oznacenyFirmaProdukt).isPresent()) {
            return;
        }

        ConfirmDialog.show(UI.getCurrent(), "Odstránenie mapovania medzi KIT a KAT",
            "Naozaj si prajete odstrániť prepoj na produkt "+ oznacenyFirmaProdukt.getProdukt().getNazov()+"?",
            "Áno", "Nie", new ConfirmDialog.Listener() {

                public void onClose(ConfirmDialog dialog) {
                    if (dialog.isConfirmed()) {
                        // Confirmed to continue

                        FirmaProduktNastroje.zmazFirmaProdukt(oznacenyFirmaProdukt);
                        browsPanel.odstranZaznam(oznacenyFirmaProdukt);
                        Notification.show("Mapovanie odstránené", Notification.Type.TRAY_NOTIFICATION);
                        browsPanel.selectFirst();
                    }
                }
            });

    }

    public void pridajNovy(FirmaProdukt novaFirmaProdukt) {
        firmaProduktList.add(novaFirmaProdukt);
        if (novaFirmaProdukt!=null)
            browsPanel.pridaj(novaFirmaProdukt);

    }


    public void refresh() {browsPanel.refresh(); }
}

