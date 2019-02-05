package sk.zpn.zaklad.view.uzivatel;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.view.uzivatel.BrowsPanel;
import sk.zpn.zaklad.view.uzivatel.EditacnyForm;

public class UzivateliaView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "uzivateliaView";

    private EditacnyForm editacnyForm;

    private BrowsPanel browsPanel;

    public UzivateliaView() {

        browsPanel=new BrowsPanel();
        editacnyForm=new EditacnyForm();
        editacnyForm.setUzivatelView(this);
        configureComponents();
        this.addComponent(browsPanel);
        this.addComponent(editacnyForm);


    }

    public  void refreshUzivatelov() {
        refreshUzivatelov(null);
    }

    void deselect() {
        browsPanel.deselect();
    }

    private void configureComponents() {

        editacnyForm.setUzivatelView(this);
        browsPanel.btnNovy.addClickListener(clickEvent -> editacnyForm.edit(new Uzivatel()));
        //todo browsPanel.addFilterListener(this::refreshUzivatelov);
        browsPanel.addSelectionListener(editacnyForm::edit);
        //editacnyForm.setVisible(true);
        refreshUzivatelov(null);
    }

    private void refreshUzivatelov(String stringFilter) {
        if (stringFilter == null) {
            browsPanel.refresh();
        } else {
            browsPanel.refresh(stringFilter);
        }
//        contactForm.setVisible(false);
    }

}

