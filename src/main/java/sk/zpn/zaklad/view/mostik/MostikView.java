package sk.zpn.zaklad.view.mostik;

import com.vaadin.navigator.View;
import com.vaadin.ui.HorizontalLayout;
import sk.zpn.domena.Mostik;

import sk.zpn.domena.ZaznamCsv;
import sk.zpn.zaklad.model.DavkaCsvImporter;
import sk.zpn.zaklad.model.MostikNastroje;


import java.io.IOException;
import java.util.List;

public class MostikView extends HorizontalLayout implements View {
    // ContactForm is an example of a custom component class
    public static final String NAME = "mostikView";



    private BrowsPanel browsPanel;

    private List<Mostik> mostikList;

    public MostikView() {
        List<ZaznamCsv> zaznam=null;
        try {
            zaznam=DavkaCsvImporter.nacitajCsvDavku("//c:/klient/zpn1901.csv");
            System.out.println(zaznam.size());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}

