package sk.zpn.zaklad.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import sk.zpn.MyUI;

public class MainView extends VerticalLayout implements View {
    public final static String NAME = "";

    public MainView() {

        if (MyUI.jeUzivatelPrihlaseny()) {
            final TextField name = new TextField();
            name.setCaption("Type your name here:");
            addComponents(new Label("Prihlásený"));
        } else {
            Button button = new Button("Prihlásenie");
            button.addClickListener(e -> UI.getCurrent().getNavigator().navigateTo(LoginView.NAME));
            addComponent(button);
        }
    }


    //                Window window = new Window();
//                window.setCaption("Okno");
//                window.setWidth("50%");
//                window.setHeight("50%");
//                window.center();
//
//                UI.getCurrent().addWindow(window);

}
