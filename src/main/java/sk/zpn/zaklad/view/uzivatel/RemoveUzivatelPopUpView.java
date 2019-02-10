package sk.zpn.zaklad.view.uzivatel;

import com.vaadin.ui.*;

public class RemoveUzivatelPopUpView extends VerticalLayout {

    private Label label;
    private Button okBtn;
    private Button cancelBtn;

    public RemoveUzivatelPopUpView() {
        label = new Label("Naozaj si prajete odstrániť uzivatela?");
        okBtn = new Button("áno");
        cancelBtn = new Button("nie");

        VerticalLayout verticalLayout = new VerticalLayout();
        this.addComponent(verticalLayout);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        verticalLayout.addComponent(label);
        verticalLayout.addComponent(horizontalLayout);

        horizontalLayout.addComponent(okBtn);
        horizontalLayout.addComponent(cancelBtn);




    }


}
