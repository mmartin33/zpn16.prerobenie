package sk.zpn.zaklad.view.nacitanieDBF;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;


public class NacitanieDbfView extends HorizontalLayout implements View {

    public static final String NAME = "nacitanieDbfView";
    private final UploadDBF ue;


    public NacitanieDbfView(){

        ue=new UploadDBF();
        ue.init("basic");
        this.addComponent(ue);
    }

}

