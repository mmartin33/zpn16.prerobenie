package sk.zpn.zaklad.view.nacitanieCSV;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import sk.zpn.domena.VysledokImportu;


public class NacitanieCSVView extends HorizontalLayout implements View {

    public static final String NAME = "nacitanieDbfView";
    private final UploadCSV ue;
    private VysledokImportu vysledokImportu;
    private BrowsPanelVysledkov browsPanelVysledkov;

    public NacitanieCSVView(){

        ue=new UploadCSV(this);
        ue.init();

        this.addComponent(ue);
        browsPanelVysledkov=new BrowsPanelVysledkov();
        this.addComponent(browsPanelVysledkov);
        browsPanelVysledkov.setVisible(false);



    }

    public VysledokImportu getVysledokImportu() {
        return vysledokImportu;
    }

    public void setVysledokImportu(VysledokImportu vysledokImportu) {
        this.vysledokImportu = vysledokImportu;
        this.browsPanelVysledkov.setVysledokImportu(vysledokImportu);


    }
}

