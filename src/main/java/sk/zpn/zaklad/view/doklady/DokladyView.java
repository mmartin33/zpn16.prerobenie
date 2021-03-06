package sk.zpn.zaklad.view.doklady;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import sk.zpn.domena.Doklad;
import sk.zpn.domena.Firma;
import sk.zpn.domena.StavDokladu;
import sk.zpn.domena.TypDokladu;
import sk.zpn.zaklad.model.DokladyNastroje;
import sk.zpn.zaklad.model.ParametreNastroje;

import java.util.List;

public class DokladyView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "dokladyView";

    private EditacnyForm editacnyForm;
    private Firma velkosklad;
    public BrowsPanel browsPanel;
    private GridLayout gr;
    public List<Doklad> dokladyList = null;
    public boolean rezimOdmien = false;

    public DokladyView(Firma velkosklad) {
        this.velkosklad = velkosklad;

        gr = new GridLayout(2, 2);
        gr.setSpacing(false);
        gr.setSizeFull();
        gr.setColumnExpandRatio(0, 0.60f);
        gr.setColumnExpandRatio(1, 0.40f);


        editacnyForm = new EditacnyForm();
        editacnyForm.setDokladyView(this);
        this.setSizeFull();
        configureComponents();
    }

    void deselect() {
        browsPanel.deselect();
    }

    private void configureComponents() {

        editacnyForm.setDokladyView(this);
    }

    private void delete(Button.ClickEvent clickEvent) {
        editacnyForm.delete();
    }


    public void refreshDokladov() {
        browsPanel.refresh();
    }

    void pridajNovyDoklad(Doklad novyDoklad) {
        dokladyList.add(novyDoklad);
        this.refreshDokladov(novyDoklad);

    }

    void odstranDoklad(Doklad doklad) {
        int i = dokladyList.indexOf(doklad);
        dokladyList.remove(doklad);
        this.refreshDokladov();

        Doklad d;
        if (i >= dokladyList.size())
            d = dokladyList.get(dokladyList.size() - 1);
        else
            d = dokladyList.get(i);
        this.refreshDokladov(d);


    }

    private void refreshDokladov(Doklad d) {
        if (d == null)
            refreshDokladov();
        else
            browsPanel.refresh(d);

    }

    void selectFirst() {
        browsPanel.selectFirst();
    }

    void selectNejaky(Doklad staryDokladEditovany) {
        if (staryDokladEditovany != null)
            browsPanel.selectDoklad(staryDokladEditovany);
        else
            browsPanel.selectFirst();
    }

    void selectDoklad(Doklad doklad) {
        browsPanel.selectDoklad(doklad);
    }

    public Firma getVelkosklad() {
        return velkosklad;
    }


    public void setVelkosklad(Firma velkosklad) {
        this.velkosklad = velkosklad;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (dokladyList == null) {
            if (rezimOdmien)
                dokladyList = DokladyNastroje.zoznamDokladovOdmien(getVelkosklad(),ParametreNastroje.nacitajParametre().getRok());
            else
                dokladyList = DokladyNastroje.zoznamDokladov(getVelkosklad(), ParametreNastroje.nacitajParametre().getRok());
        }
        if (browsPanel == null) {
            browsPanel = new BrowsPanel(dokladyList, getVelkosklad());
            if (gr.getComponentCount() == 0) {
                gr.addComponent(browsPanel, 0, 0, 0, 1);
                gr.addComponent(editacnyForm, 1, 0, 1, 1);
            }
            this.addComponent(gr);
            browsPanel.btnZmaz.addClickListener(this::delete);
            browsPanel.btnTlac.addClickListener(this::tlac);
            browsPanel.btnTest.addClickListener(browsPanel::test);
            browsPanel.btnNovy.addClickListener(clickEvent -> {
                deselect();
                Doklad d = new Doklad();
                d.setStavDokladu(StavDokladu.POTVRDENY);
                d.setCisloDokladu(DokladyNastroje.noveCisloDokladu(null));
                if (this.rezimOdmien) {
                    d.setCisloDokladuOdmeny(DokladyNastroje.noveCisloDokladuOdmien());
                    d.setTypDokladu(TypDokladu.ODMENY);
                    this.editacnyForm.btnVyberPoberatela.setEnabled(true);;
                }
                editacnyForm.edit(d);
            });
            browsPanel.addSelectionListener(editacnyForm::edit);
            if (rezimOdmien) {
                this.editacnyForm.rezimOdmien();
                this.browsPanel.rezimOdmien();
            } else {
                this.editacnyForm.rezimBodovaci();
                this.browsPanel.rezimBodovaci();
            }

        }

        browsPanel.setProduktyView(this);
        if (velkosklad != null) {
            this.editacnyForm.rezimVelkoskladu();
        }
        this.browsPanel.nadstavNaOznaceny();
//        refreshDokladov();


    }

    private void tlac(Button.ClickEvent clickEvent) {

//        ListDataProvider dataProvider=(ListDataProvider)  browsPanel.grid.getDataProvider();
//        List<Doklad> list = (List<Doklad>) dataProvider.getItems();
    }

    public List<Doklad> naplnList(String rok){
        if (rezimOdmien)
            dokladyList=DokladyNastroje.zoznamDokladovOdmien(getVelkosklad(),rok);
        else
            dokladyList = DokladyNastroje.zoznamDokladov(getVelkosklad(), rok);


        return this.dokladyList;
    }


    public void setRezimOdmien() {
        this.rezimOdmien = true;
    }

    public EditacnyForm getEditacnyForm() {
        return editacnyForm;
    }

    public void setEditacnyForm(EditacnyForm editacnyForm) {
        this.editacnyForm = editacnyForm;
    }
}

