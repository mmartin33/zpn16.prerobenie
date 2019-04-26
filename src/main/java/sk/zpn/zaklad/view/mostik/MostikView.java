package sk.zpn.zaklad.view.mostik;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.Firma;
import sk.zpn.domena.FirmaProdukt;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.model.FirmaNastroje;
import sk.zpn.zaklad.model.FirmaProduktNastroje;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.model.UzivatelNastroje;
import sk.zpn.zaklad.view.VitajteView;

import java.util.List;
import java.util.Optional;

import static sk.zpn.zaklad.model.UzivatelNastroje.getUzivatela;


public class MostikView extends HorizontalLayout implements View {

    // ContactForm is an example of a custom component class
    public static final String NAME = "mostikView";
    private BrowsPanel browsPanel;

    private final Binder<Firma> binder = new Binder<>();
    private GridLayout mainGridLayout =  new GridLayout(1,4);
    private HorizontalLayout upperLabelHorizontalLayout = new HorizontalLayout();
    private HorizontalLayout tlacitkovyLayout = new HorizontalLayout();
    private String nazovFirmy = "";
    private String rok = "";
    private Label firmaLabel =  new Label("<b>Firma: </b>", ContentMode.HTML);
    private Label rokLabel =  new Label("<b>Rok: </b>", ContentMode.HTML);
    private TextField txtFirma;
    private List<FirmaProdukt> firmaProduktList;
    private Button btnZmaz = new Button("Zmaž", VaadinIcons.CLOSE_CIRCLE);;
    private Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);

    public MostikView() {

        mainGridLayout.setSizeFull();
        mainGridLayout.setRowExpandRatio(0, 0.05f);
        mainGridLayout.setRowExpandRatio(1, 0.05f);
        mainGridLayout.setRowExpandRatio(2, 0.85f);
        mainGridLayout.setRowExpandRatio(3, 0.05f);

        mainGridLayout.setSpacing(false);
        configureComponents();
        tlacitkovyLayout.addComponent(btnZmaz);
        tlacitkovyLayout.addComponent(btnSpat);
        upperLabelHorizontalLayout.addComponent(firmaLabel);
        upperLabelHorizontalLayout.addComponent(rokLabel);
        upperLabelHorizontalLayout.addComponent(new Label("Dvojklikom opraviť KIT a koeficient "));

        mainGridLayout.addComponent(upperLabelHorizontalLayout);
        mainGridLayout.addComponent(txtFirma);
        mainGridLayout.addComponent(browsPanel);
        mainGridLayout.setComponentAlignment(browsPanel,Alignment.MIDDLE_LEFT);
        mainGridLayout.addComponent(tlacitkovyLayout);
        mainGridLayout.setComponentAlignment(tlacitkovyLayout,Alignment.BOTTOM_LEFT);
        this.addComponent(mainGridLayout);
        this.setSizeFull();
        this. setVisible(true);


        btnZmaz.addClickListener(this::delete);
        btnSpat.addClickListener(clickEvent ->
            UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );
        btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
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


//        Binder.Binding<Firma, String> firmaBinding = binder.forField(txtFirma)
//                .withValidator(nazovFirmy -> FirmaNastroje.prvaFirmaPodlaNazvu(nazovFirmy).isPresent(),
//                        "Firma musi byt existujuca");
//                .bind(doklad -> doklad.getFirma() == null ? "" : doklad.getFirma().getNazov(),
//                        (doklad, s) -> FirmaNastroje.prvaFirmaPodlaNazvu(tFirma.getValue()).ifPresent(doklad::setFirma));


        txtFirma=new TextField();

        txtFirma.setWidth("200");
        txtFirma.setValue(nazovFirmy);
        txtFirma.setEnabled(false);
        if (UzivatelNastroje.jeUzivatelAdminAleboSpravca())
            txtFirma.setEnabled(true);
        FirmaProduktNastroje.generateMissingFirmaProductItems(nazovFirmy);
        firmaProduktList = FirmaProduktNastroje.getListFirmaProduktPodlaNazvuFirmy(nazovFirmy);
        browsPanel = new BrowsPanel(firmaProduktList, nazovFirmy);
        refreshMostika();
    }

    public void refreshMostika() {
            browsPanel.refresh();
    }

    void pridajNovZaznamyMostika(FirmaProdukt novyFirmaProdukt) {
        firmaProduktList.add(novyFirmaProdukt);
        this.refreshMostika();

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

}

