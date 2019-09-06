package sk.zpn.zaklad.view.doklady;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import sk.zpn.domena.Doklad;
import sk.zpn.domena.Firma;
import sk.zpn.domena.StavDokladu;
import sk.zpn.zaklad.model.DokladyNastroje;

import java.util.List;

public class DokladyView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "dokladyView";

    private EditacnyForm editacnyForm;
    private Firma velkosklad;
    private BrowsPanel browsPanel;

    private List<Doklad> dokladyList=null;

    public DokladyView(Firma velkosklad) {
        this.velkosklad=velkosklad;
        GridLayout gr=new GridLayout(2,2);
        gr.setSpacing(false);
        gr.setSizeFull();
        gr.setColumnExpandRatio(0, 0.60f);
        gr.setColumnExpandRatio(1, 0.40f);

        dokladyList = DokladyNastroje.zoznamDokladov(getVelkosklad());
        browsPanel=new BrowsPanel(dokladyList,getVelkosklad());
        editacnyForm=new EditacnyForm();
        editacnyForm.setDokladyView(this);
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

        editacnyForm.setDokladyView(this);

        browsPanel.btnNovy.addClickListener(clickEvent -> {
            deselect();
            Doklad d=new Doklad();
            d.setStavDokladu(StavDokladu.POTVRDENY);
            d.setCisloDokladu(DokladyNastroje.noveCisloDokladu(null));
            editacnyForm.edit(d);
        });


        browsPanel.addSelectionListener(editacnyForm::edit);
        refreshDokladov();
    }

    public void refreshDokladov() {
            browsPanel.refresh();
    }

    void pridajNovyDoklad(Doklad novyDoklad) {
        dokladyList.add(novyDoklad);
        this.refreshDokladov(novyDoklad);

    }
    void odstranDoklad(Doklad doklad) {
        int i= dokladyList.indexOf(doklad);
        dokladyList.remove(doklad);
        this.refreshDokladov();

        Doklad d;
        if (i>=dokladyList.size())
                d=dokladyList.get(dokladyList.size()-1);
        else
                d=dokladyList.get(i);
        this.refreshDokladov(d);




    }

    private void refreshDokladov(Doklad d) {
        if (d==null)
            refreshDokladov();
        else
            browsPanel.refresh(d);

    }

    void selectFirst() {
        browsPanel.selectFirst();
    }
    void selectNejaky(Doklad staryDokladEditovany) {
        if (staryDokladEditovany!=null)
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
        rezimVelkoskladu();
    }

    public void rezimVelkoskladu() {
        if (velkosklad!=null) {
           this.editacnyForm.rezimVelkoskladu();
        }
    }
}

