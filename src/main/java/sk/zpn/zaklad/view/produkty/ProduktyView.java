package sk.zpn.zaklad.view.produkty;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import sk.zpn.domena.Produkt;
import sk.zpn.domena.TypProduktov;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.model.ProduktyNastroje;
import sk.zpn.zaklad.view.firmy.FirmyView;

import java.math.BigDecimal;
import java.util.List;

public class ProduktyView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "produktyView";

    private EditacnyForm editacnyForm;
    private TypProduktov typProduktov;
    private BrowsPanel browsPanel;
    GridLayout  gr;
    private List<Produkt> produktList;
    private String rodicovskyView;

    public ProduktyView() {
        gr=new GridLayout(2,2);
        gr.setSpacing(false);
        gr.setSizeFull();
        gr.setColumnExpandRatio(0, 0.60f);
        gr.setColumnExpandRatio(1, 0.40f);

        configureComponents();
    }



    void deselect() {

        browsPanel.deselect();

    }

    private void configureComponents() {

//        editacnyForm.setProduktyView(this);


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
        this.refreshProduktov(novyProdukt);

    }
    void odstranProdukt(Produkt produkt) {

        int i= produktList.indexOf(produkt);
        produktList.remove(produkt);
        Produkt p;
        if (i>=produktList.size())
                p=produktList.get(produktList.size()-1);
        else
                p=produktList.get(i);
        this.refreshProduktov(p);


    }

    private void refreshProduktov(Produkt p) {

        if (p==null)
            refreshProduktov();
        else
            browsPanel.refresh(p);
    }

    public TypProduktov getTypProduktov() {
        return typProduktov;
    }

    public void setTypProduktov(TypProduktov typProduktov) {
        this.typProduktov = typProduktov;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {


        produktList = ProduktyNastroje.zoznamProduktovZaRok(null,this.typProduktov);
        browsPanel=new BrowsPanel(produktList);
        browsPanel.setHeight("100%");
        browsPanel.setProduktyView(this);
        editacnyForm=new EditacnyForm();
        editacnyForm.setProduktyView(this);
        gr.addComponent(browsPanel,0,0,0,1);
        gr.addComponent(editacnyForm,1,0,1,0);

        this.addComponent(gr);
        this.setSizeFull();

        browsPanel.btnNovy.addClickListener(clickEvent -> editacnyForm.edit(new Produkt()
                .setRok(this.typProduktov==TypProduktov.BODOVACI?ParametreNastroje.nacitajParametre().getRok():null)
                .setBody(BigDecimal.valueOf(1))
                .setKusy(BigDecimal.valueOf(1))
                .setKat(ProduktyNastroje.getMaxKIT(this.typProduktov))
                .setTypProduktov(this.typProduktov)
        ));
        browsPanel.btnFirmy.addClickListener(clickEvent -> spustiFirmy());
        browsPanel.addSelectionListener(editacnyForm::edit);
        if (this.typProduktov==TypProduktov.ODMENA)
            this.editacnyForm.tCena.setVisible(true);
        refreshProduktov();


    }

    public void setRodicovskyView(String polozkyDokladuView) {
        this.rodicovskyView =polozkyDokladuView;
    }

    public String getRodicovskyView() {
        return rodicovskyView;
    }
}

