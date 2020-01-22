package sk.zpn.zaklad.view.poberatelia;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.Prevadzka;
import sk.zpn.zaklad.model.PoberatelNastroje;


import java.util.List;

public class PoberateliaView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "poberateliaView";

    private EditacnyForm editacnyForm;
    private BrowsPanel browsPanel;
    private Firma velkosklad;
    private Prevadzka prevadzka;
    private String rodicovskyView;
    private View zdrojovyView;
    GridLayout gr;

    private List<Poberatel> poberatelList;
    public PoberateliaView(Prevadzka prevadzka) {

        gr = new GridLayout(2,2);
        gr.setSpacing(false);
        gr.setSizeFull();
        gr.setColumnExpandRatio(0, 0.60f);
        gr.setColumnExpandRatio(1, 0.40f);


        this.prevadzka=prevadzka;
        editacnyForm=new EditacnyForm();
        editacnyForm.setPoberatelView(this);
        this.setSizeFull();


    }



    void deselect() {
        browsPanel.deselect();
    }

    private void configureComponents() {

        editacnyForm.setPoberatelView(this);
        browsPanel.btnNovy.addClickListener(clickEvent -> editacnyForm.edit(new Poberatel()));
        browsPanel.addSelectionListener(editacnyForm::edit);
        refreshPoberatelov();
    }

    public void refreshPoberatelov() {
            browsPanel.refresh();
    }

    void pridajNovehoPoberatela(Poberatel novyPoberatel) {
        poberatelList.add(novyPoberatel);
        this.refreshPoberatelov(novyPoberatel);

    }

    void odstranPoberatela(Poberatel poberatel) {

      int i= poberatelList.indexOf(poberatel);
        poberatelList.remove(poberatel);
        Poberatel p;
        if (i>=poberatelList.size())
            p=poberatelList.get(poberatelList.size()-1);
        else
            p=poberatelList.get(i);
        this.refreshPoberatelov(p);




    }

    private void refreshPoberatelov(Poberatel p) {
        if (p==null)
            refreshPoberatelov();
        else
            browsPanel.refresh(p);
    }

    public Prevadzka getPrevadzka() {
        return prevadzka;
    }

    public EditacnyForm getEditacnyForm() {
        return editacnyForm;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        poberatelList=naplnList(velkosklad,null);
        browsPanel=new BrowsPanel(poberatelList,this);
        configureComponents();
        gr.addComponent(browsPanel,0,0,0,1);
        gr.addComponent(editacnyForm,1,0,1,0);

        this.addComponent(gr);
        if (velkosklad!=null) {
            editacnyForm.rezimVelkoskladu();
            browsPanel.rezimVelkoskladu();
        }

    }

    public List<Poberatel> naplnList(Firma velkosklad,Prevadzka prevadzkaPoberatela){
        if (this.velkosklad==null)
            return PoberatelNastroje.zoznamPoberatelov(prevadzkaPoberatela);
        else
            return  PoberatelNastroje.zoznamPoberatelovVelkoskladu(velkosklad,prevadzkaPoberatela);

    }
    public Firma getVelkosklad() {
        return velkosklad;
    }

    public void setVelkosklad(Firma velkosklad) {
        this.velkosklad = velkosklad;
    }
    public void setRodicovskyView(String view) {
        this.rodicovskyView = view;
    }

    public String getRodicovskyView() {
        return rodicovskyView;
    }

    public View getZdrojovyView() {
        return zdrojovyView;
    }

    public void setZdrojovyView(View zdrojovyView) {
        this.zdrojovyView = zdrojovyView;
    }
}

