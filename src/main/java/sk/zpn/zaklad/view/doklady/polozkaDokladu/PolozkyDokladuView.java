package sk.zpn.zaklad.view.doklady.polozkaDokladu;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import sk.zpn.domena.Doklad;
import sk.zpn.domena.Firma;
import sk.zpn.domena.PolozkaDokladu;
import sk.zpn.domena.TypDokladu;
import sk.zpn.zaklad.model.DokladyNastroje;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.model.PolozkaDokladuNastroje;

import java.math.BigDecimal;
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
    private boolean rezimOdmien=false;
    private boolean rezimRegistracia=false;



    private boolean klasickyRezim=false;

    public PolozkyDokladuView(Doklad doklad,Firma velkosklad) {
        this.doklad=doklad;
        this.velkosklad=velkosklad;
        gr=new GridLayout(2,2);
        gr.setSpacing(false);
        gr.setSizeFull();
        gr.setColumnExpandRatio(0, 0.60f);
        gr.setColumnExpandRatio(1, 0.40f);


        editacnyForm=new EditacnyForm();
        editacnyForm.setDokladyView(this);

        configureComponents();
    }

//    void deselect() {
//        browsPanel.deselect();
//    }



    private void configureComponents() {




        editacnyForm.setDokladyView(this);
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

        boolean prvyvstup=false;
        if (polozkyDokladuList==null) {
            polozkyDokladuList = PolozkaDokladuNastroje.zoznamPoloziekDokladov(this.doklad);
        }
        if (browsPanel==null) {
            prvyvstup = true;
            browsPanel = new BrowsPanel(polozkyDokladuList, this);
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
            browsPanel.btnNovy.addClickListener(clickEvent -> {
                editacnyForm.setVisible(true);
                gr.setColumnExpandRatio(0, 0.60f);
                gr.setColumnExpandRatio(1, 0.40f);
                PolozkaDokladu novaPolozka=new PolozkaDokladu();
                if (this.rezimOdmien) {
                    novaPolozka.setPoberatel(this.doklad.getPoberatel());
                }
                if (this.rezimRegistracia) {
                    novaPolozka.setBody(new BigDecimal(ParametreNastroje.nacitajParametre().getBodyZaRegistraciu()));
                }
                editacnyForm.edit(novaPolozka); });
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
            gr.addComponent(browsPanel,0,0,0,1);
            gr.addComponent(editacnyForm,1,0,1,0);
            this.addComponent(gr);
            this.setSizeFull();

            refreshPoloziekDokladov();

        }
        if (this.klasickyRezim)
            klasickyRezim();

        else if(this.rezimOdmien)
            if (prvyvstup)
                rezimOdmien();

        //if (velkosklad!=null)
            rezimVelkoskladu();
        if ((doklad.getTypDokladu()== TypDokladu.REGISTRACIA)|| (doklad.getTypDokladu()== TypDokladu.PREVOD)){
            rezimRegistracia();
        }
        browsPanel.aktualizujInfoPanle(DokladyNastroje.sumaBodov(this.doklad));
    }

    private void rezimRegistracia() {
        this.rezimRegistracia=true;
        browsPanel.rezimRegistracia();
        editacnyForm.rezimRegistracia();
    }

    public void rezimVelkoskladu() {
        this.editacnyForm.zakazNepotrebne();
        if (velkosklad!=null) {

            this.editacnyForm.rezimVelkoskladu();
        }
    }

    public void aktualizujInfo() {
        browsPanel.aktualizujInfoPanle(DokladyNastroje.sumaBodov(this.doklad));
    }

    public void rezimOdmien() {
        this.rezimOdmien = true;
        browsPanel.rezimOdmien();
        editacnyForm.rezimOdmien();

    }

    public void klasickyRezim() {

        browsPanel.setKlasickyRezim();
        browsPanel.klasickyRezim();
        editacnyForm.klasickyRezim();

    }

    public EditacnyForm getEditacnyForm() {
        return editacnyForm;
    }

    public void setOtvorenyDoklad(Doklad otvorenyDoklad) {
        this.doklad=doklad;
    }
    public void setKlasickyRezim() {
        this.klasickyRezim = true;
    }

    public void setRezimOdmien() {
        this.rezimOdmien=true;
    }
}

