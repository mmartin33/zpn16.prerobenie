package sk.zpn.zaklad.view.doklady.polozkaDokladu;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import sk.zpn.domena.Doklad;
import sk.zpn.domena.Firma;
import sk.zpn.domena.PolozkaDokladu;
import sk.zpn.zaklad.model.PolozkaDokladuNastroje;

import java.util.List;

public class PolozkyDokladuView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "polozkyDokladyView";

    private EditacnyForm editacnyForm;
    private GridLayout gr;
    private BrowsPanel browsPanel;

    private List<PolozkaDokladu> polozkyDokladuList;
    public PolozkaDokladu povodnaPolozka;
    private Doklad doklad;
    private Firma velkosklad;

    public PolozkyDokladuView(Doklad doklad,Firma velkosklad) {
        this.velkosklad=velkosklad;
        gr=new GridLayout(2,2);
        gr.setSpacing(false);
        gr.setSizeFull();
        gr.setColumnExpandRatio(0, 0.60f);
        gr.setColumnExpandRatio(1, 0.40f);

        this.doklad=doklad;
        polozkyDokladuList = PolozkaDokladuNastroje.zoznamPoloziekDokladov(this.doklad);
        browsPanel=new BrowsPanel(polozkyDokladuList,this);
        editacnyForm=new EditacnyForm();
        editacnyForm.setDokladyView(this);
        configureComponents();
        gr.addComponent(browsPanel,0,0,0,1);
        gr.addComponent(editacnyForm,1,0,1,0);
        this.addComponent(gr);
        this.setSizeFull();
    }

//    void deselect() {
//        browsPanel.deselect();
//    }

    private void configureComponents() {


        browsPanel.btnPanelovy.addClickListener(clickEvent -> {
            if (editacnyForm.isVisible()) {
                editacnyForm.setVisible(false);
                gr.setColumnExpandRatio(0, 0.100f);
                gr.setColumnExpandRatio(1, 0.00f);

            }
            else {
                editacnyForm.setVisible(true);
                gr.setColumnExpandRatio(0, 0.60f);
                gr.setColumnExpandRatio(1, 0.40f);

            }
                    ; });

        editacnyForm.setDokladyView(this);
        browsPanel.btnNovy.addClickListener(clickEvent -> {
            editacnyForm.setVisible(true);
            gr.setColumnExpandRatio(0, 0.60f);
            gr.setColumnExpandRatio(1, 0.40f);
            editacnyForm.edit(new PolozkaDokladu()); });
        browsPanel.btnNovyKopia.addClickListener(clickEvent -> {
            PolozkaDokladu novaPolozka=new PolozkaDokladu();
            novaPolozka.setDoklad(povodnaPolozka.getDoklad());
            novaPolozka.setPrevadzka(povodnaPolozka.getPrevadzka());
            novaPolozka.setPoberatel(povodnaPolozka.getPoberatel());
            novaPolozka.setMnozstvo(povodnaPolozka.getMnozstvo());
            novaPolozka.setBody(povodnaPolozka.getBody());



            editacnyForm.setVisible(true);
            gr.setColumnExpandRatio(0, 0.60f);
            gr.setColumnExpandRatio(1, 0.40f);
            editacnyForm.edit(novaPolozka); });

        browsPanel.addSelectionListener(editacnyForm::edit);

        refreshPoloziekDokladov();
    }

    public void refreshPoloziekDokladov() {
            browsPanel.refresh();
    }

    void pridajNovuPolozkuDokladu(PolozkaDokladu novaPolozkaDokladu) {
        polozkyDokladuList.add(novaPolozkaDokladu);
        this.refreshPoloziekDokladov(novaPolozkaDokladu);

    }
    void odstranPolozkuDokladu(PolozkaDokladu polozkaDokladu) {


        int i= polozkyDokladuList.indexOf(polozkaDokladu);
        polozkyDokladuList.remove(polozkaDokladu);
        this.refreshPoloziekDokladov();

        PolozkaDokladu p;
        if (i>=polozkyDokladuList.size())
            p=polozkyDokladuList.get(polozkyDokladuList.size()-1);
        else
            p=polozkyDokladuList.get(i);
        this.refreshPoloziekDokladov(p);





    }

    private void refreshPoloziekDokladov(PolozkaDokladu p) {
        if (p==null)
            refreshPoloziekDokladov();
        else
            browsPanel.refresh(p);

    }

    void selectFirst() {
        browsPanel.selectFirst();
    }

    void selectPolozkuDokladu(PolozkaDokladu polozkaDokladu) {
        browsPanel.selectDoklad(polozkaDokladu);
    }

    public Doklad getDoklad() {
        return doklad;
    }

    public void setDoklad(Doklad doklad) {
        this.doklad = doklad;
    }



    public Firma getVelkosklad() {
        return this.velkosklad ;
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

