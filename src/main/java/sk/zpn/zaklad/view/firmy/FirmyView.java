package sk.zpn.zaklad.view.firmy;

import com.vaadin.navigator.View;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.model.FirmaNastroje;
import sk.zpn.zaklad.view.VitajteView;

import java.util.List;

public class FirmyView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "firmyView";
    private EditacnyForm editacnyForm;
    private BrowsPanel browsPanel;
    private List<Firma> firmaList;
    private String povodnyView;

    public FirmyView() {
        init();

    }
    public FirmyView(String povodnyView) {
        this.povodnyView=povodnyView;
        init();
    }
    private void init(){
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
        browsPanel.btnSpat.addClickListener(clickEvent -> spat());
        browsPanel.addSelectionListener(editacnyForm::edit);
        refreshFiriem();
    }

    private void spat() {
        if (povodnyView!=null)
            UI.getCurrent().getNavigator().navigateTo(povodnyView);
        else
            UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME);
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

