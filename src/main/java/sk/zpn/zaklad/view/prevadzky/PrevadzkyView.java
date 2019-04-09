package sk.zpn.zaklad.view.prevadzky;

import com.vaadin.navigator.View;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Prevadzka;
import sk.zpn.zaklad.model.PrevadzkaNastroje;



import java.util.List;

public class PrevadzkyView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "prevadzkyView";

    private EditacnyForm editacnyForm;

    private Firma firma;

    private BrowsPanel browsPanel;

    private List<Prevadzka> prevadzkyList;


    public PrevadzkyView() {


    }

    public PrevadzkyView(Firma firma) {
        if (firma!=null) {
            prevadzkyList = PrevadzkaNastroje.zoznamPrevadzka(firma);
            this.setFirma(firma);
        }
        else
            prevadzkyList = PrevadzkaNastroje.zoznamPrevadzka();
        configureComponents();
    }

    void deselect() {
        browsPanel.deselect();
    }

    private void configureComponents() {
        GridLayout gr=new GridLayout(2,2);
        gr.setSpacing(false);
        gr.setSizeFull();
        gr.setColumnExpandRatio(0, 0.60f);
        gr.setColumnExpandRatio(1, 0.40f);


        browsPanel=new BrowsPanel(prevadzkyList,this);

        editacnyForm=new EditacnyForm(this);
        editacnyForm.setPrevadzkyView(this);




        editacnyForm.setPrevadzkyView(this);
        browsPanel.btnNovy.addClickListener(clickEvent -> {
//            deselect();
            Prevadzka p= new Prevadzka();
            if (this.firma!=null)
                p.setFirma(this.firma);
            editacnyForm.edit(p);
        });
        browsPanel.addSelectionListener(editacnyForm::edit);
        refreshPrevadzok();
        gr.addComponent(browsPanel,0,0,0,1);
        gr.addComponent(editacnyForm,1,0,1,0);

        this.addComponent(gr);
        this.setSizeFull();

    }

    public void refreshPrevadzok() {
            browsPanel.refresh();
    }

    void pridajNovuPrevadzku(Prevadzka novaPrevazka) {
        prevadzkyList.add(novaPrevazka);
        this.refreshPrevadzok(novaPrevazka);

    }

    private void refreshPrevadzok(Prevadzka novaPrevazka) {
        if (novaPrevazka==null)
            refreshPrevadzok();
        else
            browsPanel.refresh(novaPrevazka);
    }

    void odstranPrevadzku(Prevadzka prevadzka) {
        prevadzkyList.remove(prevadzka);
        this.refreshPrevadzok();
    }
    void selectFirst() {
        browsPanel.selectFirst();
    }

    void selectPrevadzku(Prevadzka prevadzka) {
        browsPanel.selectPrevadzku(prevadzka);
    }


    public Firma getFirma() {
        return firma;
    }

    public void setFirma(Firma firma) {
        this.firma = firma;
    }
}

