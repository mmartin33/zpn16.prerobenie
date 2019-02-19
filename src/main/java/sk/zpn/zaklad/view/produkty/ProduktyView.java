package sk.zpn.zaklad.view.produkty;

import com.vaadin.navigator.View;
import com.vaadin.ui.HorizontalLayout;
import sk.zpn.domena.Produkt;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.model.ProduktyNastroje;
import java.util.List;

public class ProduktyView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "produktyView";

    private EditacnyForm editacnyForm;

    private BrowsPanel browsPanel;

    private List<Produkt> produktList;

    public ProduktyView() {
        produktList = ProduktyNastroje.zoznamProduktovZaRok();
        browsPanel=new BrowsPanel(produktList);
        editacnyForm=new EditacnyForm();
        editacnyForm.setProduktyView(this);
        configureComponents();
        this.addComponent(browsPanel);
        this.addComponent(editacnyForm);


    }



    void deselect() {

        browsPanel.deselect();

    }

    private void configureComponents() {

        editacnyForm.setProduktyView(this);


        browsPanel.btnNovy.addClickListener(clickEvent -> editacnyForm.edit(new Produkt(ParametreNastroje.nacitajParametre().getRok())));
        browsPanel.addSelectionListener(editacnyForm::edit);
        refreshProduktov();
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

