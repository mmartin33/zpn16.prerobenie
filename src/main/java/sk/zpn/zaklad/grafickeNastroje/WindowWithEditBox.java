package sk.zpn.zaklad.grafickeNastroje;

import com.vaadin.server.Sizeable;
import com.vaadin.shared.Registration;
import com.vaadin.ui.*;


public class WindowWithEditBox extends Window {
    Button btnOK;
    Button btnStorno;
    TextField txtField;
    String text=null;

    public WindowWithEditBox(String nadpis,String hodnota) {
        super(nadpis);
        this.setWidth(600, Sizeable.Unit.PIXELS);
        this.setHeight(200, Sizeable.Unit.PIXELS);
        btnOK =new Button("Ulož");
        btnStorno =new Button("Konec bez uloženia");
        txtField =new TextField("Poznámka");
        if (hodnota!=null)
            txtField.setValue(hodnota);
        txtField.setWidth("500");
        btnOK.addClickListener(this::ok);
        btnStorno.addClickListener(this::storno);


        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.addComponent(btnOK);
        btnLayout.addComponent(btnStorno);

        VerticalLayout panel = new VerticalLayout();
        panel.addComponent(txtField);
        panel.addComponent(btnLayout);

        this.setContent(panel);
        this.setModal(true);


        this.center();
        UI.getCurrent().addWindow(this);


    }

    private void storno(Button.ClickEvent clickEvent) {
        this.close();
    }

    private void ok(Button.ClickEvent clickEvent) {
        text=txtField.getValue();
        this.close();
    }



    public String getText() {
        return text;
    }
}
