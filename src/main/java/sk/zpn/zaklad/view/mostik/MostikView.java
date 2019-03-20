package sk.zpn.zaklad.view.mostik;

import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import sk.zpn.domena.FirmaProdukt;

import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.model.MostikNastroje;
import sk.zpn.zaklad.model.ParametreNastroje;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static sk.zpn.zaklad.model.UzivatelNastroje.getUzivatela;

public class MostikView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "mostikView";



    private BrowsPanel browsPanel;
    private VerticalLayout mainVerticalLayout =  new VerticalLayout();
    private HorizontalLayout upperHorizontalLayout = new HorizontalLayout();
    private Label firmaLabel =  new Label("<b>Firma: </b>", ContentMode.HTML);
    private Label rokLabel =  new Label("<b>Rok: </b>", ContentMode.HTML);


    private List<FirmaProdukt> firmaProduktList;

    public MostikView() {
        this.addComponent(mainVerticalLayout);
        Optional<Uzivatel> loggedUzivatel = getUzivatela((Long) VaadinSession.getCurrent().getAttribute("id_uzivatela"));
        loggedUzivatel.ifPresent( uzivatel -> {
            firmaLabel.setValue(firmaLabel.getValue() + uzivatel.getFirma().getNazov());
        });
        rokLabel.setValue(rokLabel.getValue() + ParametreNastroje.nacitajParametre().getRok());
//        firmaProduktList = MostikNastroje.mostikZaRokAFirmu();

        browsPanel = new BrowsPanel(new ArrayList<>());
//        editacnyForm=new EditacnyForm();
//        editacnyForm.setProduktyView(this);
        configureComponents();
        mainVerticalLayout.addComponent(upperHorizontalLayout);
        mainVerticalLayout.addComponent(browsPanel);
        upperHorizontalLayout.addComponent(firmaLabel);
        upperHorizontalLayout.addComponent(rokLabel);
//        this.addComponent(editacnyForm);


    }



    void deselect() {

        browsPanel.deselect();

    }

    private void configureComponents() {

//        editacnyForm.setProduktyView(this);


//        browsPanel.btnNovy.addClickListener(clickEvent -> editacnyForm.edit(new Produkt(ParametreNastroje.nacitajParametre().getRok())));
//        browsPanel.addSelectionListener(editacnyForm::edit);
        refreshMostika();
    }

    public void refreshMostika() {
            browsPanel.refresh();
    }

    void pridajNovyMostik(FirmaProdukt novyFirmaProdukt) {
        firmaProduktList.add(novyFirmaProdukt);
        this.refreshMostika();

    }
    void odstranMostik(FirmaProdukt firmaProdukt) {

        firmaProduktList.remove(firmaProdukt);
        this.refreshMostika();

    }

}

