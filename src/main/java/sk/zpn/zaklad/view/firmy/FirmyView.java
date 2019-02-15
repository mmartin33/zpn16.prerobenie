package sk.zpn.zaklad.view.firmy;

import com.vaadin.navigator.View;
import com.vaadin.ui.HorizontalLayout;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.model.FirmaNastroje;

import java.util.List;

public class FirmyView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "firmyView";

    private EditacnyForm editacnyForm;

    private BrowsPanel browsPanel;

    private List<Firma> firmaList;

    public FirmyView() {
        firmaList = FirmaNastroje.zoznamFiriem();
        browsPanel=new BrowsPanel(firmaList);
        editacnyForm=new EditacnyForm();
        editacnyForm.setFirmaView(this);
        configureComponents();
        this.addComponent(browsPanel);
        this.addComponent(editacnyForm);
    }


    void deselect() {
        browsPanel.deselect();
    }

    private void configureComponents() {

        editacnyForm.setFirmaView(this);
        browsPanel.btnNovy.addClickListener(clickEvent -> editacnyForm.edit(new Firma()));
        browsPanel.addSelectionListener(editacnyForm::edit);
        refreshFiriem();
    }

    public void refreshFiriem() {
            browsPanel.refresh();
    }

    void pridajNovuFirmu(Firma novaFirma) {
        firmaList.add(novaFirma);
        this.refreshFiriem();

    }
    void odstranFirmu(Firma firma) {

        firmaList.remove(firma);
        this.refreshFiriem();

    }

}

