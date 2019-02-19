package sk.zpn.zaklad.view.mostik;

import com.vaadin.navigator.View;
import com.vaadin.ui.HorizontalLayout;
import sk.zpn.domena.Mostik;

import sk.zpn.zaklad.model.MostikNastroje;



import java.util.List;

public class MostikView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "mostikView";



    private BrowsPanel browsPanel;

    private List<Mostik> mostikList;

    public MostikView() {

        mostikList = MostikNastroje.mostikZaRokAFirmu();
        browsPanel=new BrowsPanel(mostikList);
//        editacnyForm=new EditacnyForm();
//        editacnyForm.setProduktyView(this);
        configureComponents();
        this.addComponent(browsPanel);
//        this.addComponent(editacnyForm);


    }



    void deselect() {

        browsPanel.deselect();

    }

    private void configureComponents() {

//        editacnyForm.setProduktyView(this);


//        browsPanel.btnNovy.addClickListener(clickEvent -> editacnyForm.edit(new Produkt(ParametreNastroje.nacitajParametre().getRok())));
//        browsPanel.addSelectionListener(editacnyForm::edit);
        refreshMostika();
    }

    public void refreshMostika() {
            browsPanel.refresh();
    }

    void pridajNovyMostik(Mostik novyMostik) {
        mostikList.add(novyMostik);
        this.refreshMostika();

    }
    void odstranMostik(Mostik mostik) {

        mostikList.remove(mostik);
        this.refreshMostika();

    }

}

