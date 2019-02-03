package sk.zpn.zaklad.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import sk.zpn.domena.Firma;

import sk.zpn.zaklad.model.FirmaNastroje;

import java.util.List;

public class FirmyView extends VerticalLayout implements View {


    public static final String NAME = "firmyView";


    public FirmyView() {
        List<Firma> u = FirmaNastroje.zoznamFiriem();
        Grid<Firma> grid = new Grid<>();
        grid.setItems(u);
        grid.addColumn(Firma::getNazov).setCaption("Meno").setId("nazov");
        grid.addColumn(Firma::getIco).setCaption("IČO").setId("ico");
        grid.addColumn(Firma::getDic).setCaption("Dič").setId("dic");
        grid.addColumn(Firma::getIc_dph).setCaption("IČ DPH").setId("icdph");
        grid.setColumnOrder(grid.getColumn("nazov"), grid.getColumn("ico"), grid.getColumn("dic"), grid.getColumn("icdph"));
        SingleSelectionModel<Firma> singleSelect =
                (SingleSelectionModel<Firma>) grid.getSelectionModel();
        singleSelect.setDeselectAllowed(false);
        grid.addItemClickListener(event ->
                Notification.show("Value: " + event.getItem()));
        this.addComponent(new Label("Prehľad firiem"));
        HorizontalLayout hp = new HorizontalLayout();
        Button btnSpat = new Button("Späť");
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
        );
        hp.addComponentsAndExpand(btnSpat);
        this.addComponent(hp);
        this.addComponent(grid);
    }


}



