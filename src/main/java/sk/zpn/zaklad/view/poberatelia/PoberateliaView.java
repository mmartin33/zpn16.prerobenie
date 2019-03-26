package sk.zpn.zaklad.view.poberatelia;

import com.vaadin.navigator.View;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.Prevadzka;
import sk.zpn.zaklad.model.PoberatelNastroje;


import java.util.List;

public class PoberateliaView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "poberateliaView";

    private EditacnyForm editacnyForm;

    private BrowsPanel browsPanel;

    private Prevadzka prevadzka;

    private List<Poberatel> poberatelList;
    public PoberateliaView(Prevadzka prevadzka) {
        GridLayout gr=new GridLayout(2,2);
        gr.setSpacing(false);
        gr.setSizeFull();
        gr.setColumnExpandRatio(0, 0.60f);
        gr.setColumnExpandRatio(1, 0.40f);


        this.prevadzka=prevadzka;
        poberatelList = PoberatelNastroje.zoznamPoberatelov();
        browsPanel=new BrowsPanel(poberatelList,this);
        editacnyForm=new EditacnyForm();
        editacnyForm.setPoberatelView(this);
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
        this.refreshPoberatelov();

    }

    void odstranPoberatela(Poberatel poberatel) {

        poberatelList.remove(poberatel);
        this.refreshPoberatelov();

    }

    public Prevadzka getPrevadzka() {
        return prevadzka;
    }
}

