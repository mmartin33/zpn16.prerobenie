package sk.zpn.zaklad.view.firmy;

import com.vaadin.navigator.View;
import com.vaadin.ui.GridLayout;
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
        GridLayout gr=new GridLayout(2,2);
        gr.setSpacing(false);
        gr.setSizeFull();
        gr.setColumnExpandRatio(0, 0.60f);
        gr.setColumnExpandRatio(1, 0.40f);

        firmaList = FirmaNastroje.zoznamFiriem();
        browsPanel=new BrowsPanel(firmaList);
        editacnyForm=new EditacnyForm();
        editacnyForm.setFirmaView(this);
        configureComponents();
        gr.addComponent(browsPanel,0,0,0,1);
        gr.addComponent(editacnyForm,1,0,1,0);
        this.addComponent(gr);
        this.setSizeFull();
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
        this.refreshFiriem(novaFirma);

    }
    void odstranFirmu(Firma firma) {



        int i= firmaList.indexOf(firma);
        firmaList.remove(firma);
        this.refreshFiriem();

        Firma f;
        if (i>=firmaList.size())
            f=firmaList.get(firmaList.size()-1);
        else
            f=firmaList.get(i);
        this.refreshFiriem(f);




    }

    private void refreshFiriem(Firma firma) {
        if (firma==null)
            refreshFiriem();
        else
            browsPanel.refresh(firma);

    }

}

