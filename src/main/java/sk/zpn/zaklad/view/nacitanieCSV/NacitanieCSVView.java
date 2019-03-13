package sk.zpn.zaklad.view.nacitanieCSV;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import sk.zpn.domena.ZaznamCsv;
import sk.zpn.zaklad.model.DavkaCsvImporter;

import java.io.IOException;
import java.util.List;


public class NacitanieCSVView extends HorizontalLayout implements View {

    public static final String NAME = "nacitanieDbfView";
    private final UploadCSV ue;


    public NacitanieCSVView(){

        ue=new UploadCSV();
        ue.init();
        this.addComponent(ue);






    }

}

