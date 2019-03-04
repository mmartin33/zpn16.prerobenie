package sk.zpn.zaklad.view.prevadzky;

import com.vaadin.navigator.View;
import com.vaadin.ui.HorizontalLayout;
import sk.zpn.domena.Prevadzka;
import sk.zpn.zaklad.model.PrevadzkaNastroje;



import java.util.List;

public class PrevadzkyView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "prevadzkyView";

    private EditacnyForm editacnyForm;

    private BrowsPanel browsPanel;

    private List<Prevadzka> prevadzkyList;

    public PrevadzkyView() {
        prevadzkyList = PrevadzkaNastroje.zoznamPrevadzka();
        browsPanel=new BrowsPanel(prevadzkyList);
        editacnyForm=new EditacnyForm();
        editacnyForm.setPrevadzkyView(this);
        configureComponents();
        this.addComponent(browsPanel);
        this.addComponent(editacnyForm);
    }

    void deselect() {
        browsPanel.deselect();
    }

    private void configureComponents() {

        editacnyForm.setPrevadzkyView(this);
        browsPanel.btnNovy.addClickListener(clickEvent -> {
            deselect();
            editacnyForm.edit(new Prevadzka());
        });
        browsPanel.addSelectionListener(editacnyForm::edit);
        refreshPrevadzok();
    }

    public void refreshPrevadzok() {
            browsPanel.refresh();
    }

    void pridajNovuPrevadzku(Prevadzka novaPrevazka) {
        prevadzkyList.add(novaPrevazka);
        this.refreshPrevadzok();

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

}

