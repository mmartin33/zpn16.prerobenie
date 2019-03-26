package sk.zpn.zaklad.view.produkty;

import com.vaadin.navigator.View;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import sk.zpn.domena.Parametre;
import sk.zpn.domena.Produkt;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.model.ProduktyNastroje;
import sk.zpn.zaklad.view.firmy.FirmyView;

import java.math.BigDecimal;
import java.util.List;

public class ProduktyView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "produktyView";

    private EditacnyForm editacnyForm;

    private BrowsPanel browsPanel;

    private List<Produkt> produktList;

    public ProduktyView() {
        GridLayout  gr=new GridLayout(2,2);
        gr.setSpacing(false);
        gr.setSizeFull();
        gr.setColumnExpandRatio(0, 0.60f);
        gr.setColumnExpandRatio(1, 0.40f);
        produktList = ProduktyNastroje.zoznamProduktovZaRok();
        browsPanel=new BrowsPanel(produktList);
        editacnyForm=new EditacnyForm();
        editacnyForm.setProduktyView(this);
        configureComponents();
        browsPanel.setHeight("100%");

        gr.addComponent(browsPanel,0,0,0,1);
        gr.addComponent(editacnyForm,1,0,1,0);

        this.addComponent(gr);
        this.setSizeFull();


    }



    void deselect() {

        browsPanel.deselect();

    }

    private void configureComponents() {

        editacnyForm.setProduktyView(this);


        browsPanel.btnNovy.addClickListener(clickEvent -> editacnyForm.edit(new Produkt()

            .setRok(ParametreNastroje.nacitajParametre().getRok())
            .setBody(BigDecimal.valueOf(1))
            .setKusy(BigDecimal.valueOf(1))
            ));
        browsPanel.btnFirmy.addClickListener(clickEvent -> spustiFirmy());
        browsPanel.addSelectionListener(editacnyForm::edit);
        refreshProduktov();
    }

    private void spustiFirmy() {
        FirmyView firmyView = new FirmyView(ProduktyView.NAME);
        UI.getCurrent().getNavigator().addView(FirmyView.NAME, firmyView);
        UI.getCurrent().getNavigator().navigateTo(FirmyView.NAME);

    }

    public void refreshProduktov() {
            browsPanel.refresh();
    }

    void pridajNovyProdukt(Produkt novyProdukt) {
        produktList.add(novyProdukt);
        this.refreshProduktov();

    }
    void odstranProdukt(Produkt produkt) {

        produktList.remove(produkt);
        this.refreshProduktov();

    }

}

