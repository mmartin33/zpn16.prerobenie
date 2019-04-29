package sk.zpn.zaklad.view.mostik;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import sk.zpn.domena.FirmaProdukt;

public class MostikEdit {
    private MostikEdit(FirmaProdukt fm){
        Window subWindow = new Window("Pridanie");
        GridLayout gl =new GridLayout(1,3);
        gl.setSizeFull();
        gl.setRowExpandRatio(0, 0.05f);
        gl.setRowExpandRatio(1, 0.90f);
        gl.setRowExpandRatio(2, 0.05f);
        gl.addComponent(new Label("Mostik"));



        subWindow.setWidth(900, Sizeable.Unit.PIXELS);
        subWindow.setHeight(1900, Sizeable.Unit.PIXELS);
        subWindow.setContent(gl);
        subWindow.setModal(true);


    }
}
