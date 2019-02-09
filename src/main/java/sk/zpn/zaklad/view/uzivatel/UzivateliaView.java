package sk.zpn.zaklad.view.uzivatel;

import com.vaadin.navigator.View;
import com.vaadin.ui.HorizontalLayout;
import sk.zpn.domena.Uzivatel;
import sk.zpn.zaklad.model.UzivatelNastroje;

import java.util.List;

public class UzivateliaView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "uzivateliaView";

    private EditacnyForm editacnyForm;

    private BrowsPanel browsPanel;

    private List<Uzivatel> uzivatelList;

    public UzivateliaView() {
        uzivatelList = UzivatelNastroje.zoznamUzivatelov();
        browsPanel=new BrowsPanel(uzivatelList);
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

    void pridajNovehoUzivatela(Uzivatel novyUzivatel) {
        uzivatelList.add(novyUzivatel);

    }
    void odstranUzivatela(Uzivatel novyUzivatel) {
        uzivatelList.remove(novyUzivatel);

    }

}

