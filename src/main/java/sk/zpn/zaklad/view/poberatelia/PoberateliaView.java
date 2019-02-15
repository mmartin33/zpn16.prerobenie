package sk.zpn.zaklad.view.poberatelia;

import com.vaadin.navigator.View;
import com.vaadin.ui.HorizontalLayout;
import sk.zpn.domena.Poberatel;
import sk.zpn.zaklad.model.PoberatelNastroje;


import java.util.List;

public class PoberateliaView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "poberateliaView";

    private EditacnyForm editacnyForm;

    private BrowsPanel browsPanel;

    private List<Poberatel> poberatelList;

    public PoberateliaView() {
        poberatelList = PoberatelNastroje.zoznamPoberatelov();
        browsPanel=new BrowsPanel(poberatelList);
        editacnyForm=new EditacnyForm();
        editacnyForm.setPoberatelView(this);
        configureComponents();
        this.addComponent(browsPanel);
        this.addComponent(editacnyForm);


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

}

