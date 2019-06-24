package sk.zpn.zaklad.view.doklady.polozkaDokladu;

import com.vaadin.navigator.View;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import sk.zpn.domena.Doklad;
import sk.zpn.domena.PolozkaDokladu;
import sk.zpn.zaklad.model.PolozkaDokladuNastroje;

import java.util.List;

public class PolozkyDokladuView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "polozkyDokladyView";

    private EditacnyForm editacnyForm;

    private BrowsPanel browsPanel;

    private List<PolozkaDokladu> polozkyDokladuList;
    private Doklad doklad;

    public PolozkyDokladuView(Doklad doklad) {
        GridLayout gr=new GridLayout(2,2);
        gr.setSpacing(false);
        gr.setSizeFull();
        gr.setColumnExpandRatio(0, 0.60f);
        gr.setColumnExpandRatio(1, 0.40f);

        this.doklad=doklad;
        polozkyDokladuList = PolozkaDokladuNastroje.zoznamPoloziekDokladov(this.doklad);
        browsPanel=new BrowsPanel(polozkyDokladuList,this);
        editacnyForm=new EditacnyForm();
        editacnyForm.setDokladyView(this);
        configureComponents();
        gr.addComponent(browsPanel,0,0,0,1);
        gr.addComponent(editacnyForm,1,0,1,0);
        this.addComponent(gr);
        this.setSizeFull();
    }
    public PolozkyDokladuView() {
    }

//    void deselect() {
//        browsPanel.deselect();
//    }

    private void configureComponents() {

        editacnyForm.setDokladyView(this);
        browsPanel.btnNovy.addClickListener(clickEvent -> {
  //          deselect();
            editacnyForm.edit(new PolozkaDokladu()); });

        browsPanel.btnPanelovy.addClickListener(clickEvent -> {
            if (editacnyForm.isVisible())
                editacnyForm.setVisible(false);
            else
                editacnyForm.setVisible(true);
                    ; });

        browsPanel.addSelectionListener(editacnyForm::edit);
        refreshPoloziekDokladov();
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
}

