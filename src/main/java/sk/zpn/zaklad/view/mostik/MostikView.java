package sk.zpn.zaklad.view.mostik;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.FirmaProdukt;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.model.FirmaProduktNastroje;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.view.VitajteView;

import java.util.List;
import java.util.Optional;

import static sk.zpn.zaklad.model.UzivatelNastroje.getUzivatela;

public class MostikView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "mostikView";
    private BrowsPanel browsPanel;
    private VerticalLayout mainVerticalLayout =  new VerticalLayout();
    private HorizontalLayout upperLabelHorizontalLayout = new HorizontalLayout();
    private HorizontalLayout upperFilterHorizontalLayout = new HorizontalLayout();
    private HorizontalLayout tlacitkovyLayout = new HorizontalLayout();
    private String nazovFirmy = "";
    private String rok = "";
    private Label firmaLabel =  new Label("<b>Firma: </b>", ContentMode.HTML);
    private Label rokLabel =  new Label("<b>Rok: </b>", ContentMode.HTML);
    private List<FirmaProdukt> firmaProduktList;
    private Button btnZmaz = new Button("Zmaž", VaadinIcons.CLOSE_CIRCLE);;
    private Button btnSpat = new Button("Späť", VaadinIcons.ARROW_BACKWARD);

    public MostikView() {
        configureComponents();
        this.addComponent(mainVerticalLayout);
        tlacitkovyLayout.addComponent(btnZmaz);
        tlacitkovyLayout.addComponent(btnSpat);
        upperLabelHorizontalLayout.addComponent(firmaLabel);
        upperLabelHorizontalLayout.addComponent(rokLabel);
        mainVerticalLayout.addComponent(upperLabelHorizontalLayout);
        mainVerticalLayout.addComponent(upperFilterHorizontalLayout);
        mainVerticalLayout.addComponent(browsPanel);
        mainVerticalLayout.addComponent(tlacitkovyLayout);
        btnZmaz.addClickListener(this::delete);
        btnSpat.addClickListener(clickEvent ->
            UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );
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
        new FirmaProduktNastroje().generateMissingFirmaProductItems(nazovFirmy);
        firmaProduktList = FirmaProduktNastroje.getListFirmaProduktPodlaNazvuFirmy(nazovFirmy);
        browsPanel = new BrowsPanel(firmaProduktList);
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
            "Naozaj si prajete odstrániť mapovanie na produkt "+ oznacenyFirmaProdukt.getProdukt().getNazov()+"?",
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

