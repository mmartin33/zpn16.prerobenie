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

    void deselect() {
        browsPanel.deselect();
    }

    private void configureComponents() {

        editacnyForm.setUzivatelView(this);
        browsPanel.btnNovy.addClickListener(clickEvent -> {
            deselect();
            editacnyForm.edit(new Uzivatel());
        });
        browsPanel.addSelectionListener(editacnyForm::edit);
        refreshUzivatelov();
    }

    public void refreshUzivatelov() {
            browsPanel.refresh();
    }

    void pridajNovehoUzivatela(Uzivatel novyUzivatel) {
        uzivatelList.add(novyUzivatel);
        this.refreshUzivatelov();

    }
    void odstranUzivatela(Uzivatel uzivatel) {
        uzivatelList.remove(uzivatel);
        this.refreshUzivatelov();
    }

    void selectFirst() {
        browsPanel.selectFirst();
    }

    void selectUzivatel(Uzivatel uzivatel) {
        browsPanel.selectUzivatel(uzivatel);
    }

}

