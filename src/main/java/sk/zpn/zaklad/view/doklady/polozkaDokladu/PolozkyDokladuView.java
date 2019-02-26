package sk.zpn.zaklad.view.doklady.polozkaDokladu;

import com.vaadin.navigator.View;
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

    public PolozkyDokladuView() {
        polozkyDokladuList = PolozkaDokladuNastroje.zoznamPoloziekDokladov(this.doklad);
        browsPanel=new BrowsPanel(polozkyDokladuList);
        editacnyForm=new EditacnyForm();
        editacnyForm.setDokladyView(this);
        configureComponents();
        this.addComponent(browsPanel);
        this.addComponent(editacnyForm);
    }

    void deselect() {
        browsPanel.deselect();
    }

    private void configureComponents() {

        editacnyForm.setDokladyView(this);
        browsPanel.btnNovy.addClickListener(clickEvent -> {
            deselect();
            editacnyForm.edit(new Doklad());
        });
        browsPanel.addSelectionListener(editacnyForm::edit);
        refreshPoloziekDokladov();
    }

    public void refreshPoloziekDokladov() {
            browsPanel.refresh();
    }

    void pridajNovuPolozkuDokladu(PolozkaDokladu novaPolozkaDokladu) {
        polozkyDokladuList.add(novaPolozkaDokladu);
        this.refreshPoloziekDokladov();

    }
    void odstranPolozkuDokladu(PolozkaDokladu polozkaDokladu) {
        polozkyDokladuList.remove(polozkaDokladu);
        this.refreshPoloziekDokladov();
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

