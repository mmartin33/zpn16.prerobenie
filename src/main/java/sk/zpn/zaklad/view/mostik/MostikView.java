package sk.zpn.zaklad.view.mostik;

import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.FirmaProdukt;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.model.FirmaProduktNastroje;
import sk.zpn.zaklad.model.ParametreNastroje;

import java.util.List;
import java.util.Optional;

import static sk.zpn.zaklad.model.UzivatelNastroje.getUzivatela;

public class MostikView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "mostikView";
    private BrowsPanel browsPanel;
    private VerticalLayout mainVerticalLayout =  new VerticalLayout();
    private HorizontalLayout upperHorizontalLayout = new HorizontalLayout();
    private String nazovFirmy = "";
    private String rok = "";
    private Label firmaLabel =  new Label("<b>Firma: </b>", ContentMode.HTML);
    private Label rokLabel =  new Label("<b>Rok: </b>", ContentMode.HTML);
    private List<FirmaProdukt> firmaProduktList;

    public MostikView() {
        this.addComponent(mainVerticalLayout);
        Optional<Uzivatel> loggedUzivatel = getUzivatela((Long) VaadinSession.getCurrent().getAttribute("id_uzivatela"));
        loggedUzivatel.ifPresent( uzivatel -> {
            nazovFirmy =  uzivatel.getFirma().getNazov();
            firmaLabel.setValue(firmaLabel.getValue() + nazovFirmy);
        });
        rok = ParametreNastroje.nacitajParametre().getRok();
        rokLabel.setValue(rokLabel.getValue() + rok);
        firmaProduktList = FirmaProduktNastroje.getFirmaProduktPodlaNazvuFirmy(nazovFirmy);

        if(firmaProduktList.isEmpty()) {
            importSablonuIfRequested();
        }
        browsPanel = new BrowsPanel(firmaProduktList);
        configureComponents();
        mainVerticalLayout.addComponent(upperHorizontalLayout);
        mainVerticalLayout.addComponent(browsPanel);
        upperHorizontalLayout.addComponent(firmaLabel);
        upperHorizontalLayout.addComponent(rokLabel);


    }



    void deselect() {
        browsPanel.deselect();
    }

    private void configureComponents() {
        refreshMostika();
    }

    public void refreshMostika() {
            browsPanel.refresh();
    }

    void pridajNovZaznamyMostika(FirmaProdukt novyFirmaProdukt) {
        firmaProduktList.add(novyFirmaProdukt);
        this.refreshMostika();

    }
    void odstranZaznamMostika(FirmaProdukt firmaProdukt) {
        firmaProduktList.remove(firmaProdukt);
        this.refreshMostika();

    }

    private void importSablonuIfRequested() {
        ConfirmDialog.show(UI.getCurrent(), "Import šablóny s produktami",
            String.format("Pre firmu %s a rok: %s neboli nájdené žiadne produkty.\n Želáte si naimportovať šablónu produktov?", this.nazovFirmy, this.rok),
                "Áno", "Nie", new ConfirmDialog.Listener() {

                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            boolean generovanieUspesne = new FirmaProduktNastroje().vygenerujSablonu(nazovFirmy);
                            if(generovanieUspesne) {
                                Notification.show("Import prebehol úspešne", Notification.Type.TRAY_NOTIFICATION);
                                firmaProduktList.addAll(FirmaProduktNastroje.getFirmaProduktPodlaNazvuFirmy(nazovFirmy));
                                refreshMostika();
                            } else {
                                Notification.show("Import zlyhal!", Notification.Type.WARNING_MESSAGE);
                            }
                        }
                    }
                });
    }

}

