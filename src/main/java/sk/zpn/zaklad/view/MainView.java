package sk.zpn.zaklad.view;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import sk.zpn.MyUI;

public class MainView extends VerticalLayout implements MojView {
    public final static String NAME = "";
    private Navigator navigator;
    public MainView(Navigator navigator) {
        this();
        this.navigator=navigator;
    }



    public MainView() {

        setSizeFull();

        com.vaadin.ui.Label title = new com.vaadin.ui.Label("Menu");
        title.addStyleName(ValoTheme.MENU_TITLE);

        com.vaadin.ui.Button view1 = new com.vaadin.ui.Button("View 1", e -> MyUI.getCurrent().getNavigator().navigateTo("view1"));
        view1.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        com.vaadin.ui.Button view2 = new Button("View 2", e -> MyUI.getCurrent().getNavigator().navigateTo("view2"));
        view2.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);

        CssLayout menu = new CssLayout(title, view1, view2);
        menu.addStyleName(ValoTheme.MENU_ROOT);

        CssLayout viewContainer = new CssLayout();

        addComponents(menu, viewContainer);

        setExpandRatio(viewContainer, 1);

//        setContent(mainLayout);

        //                Window window = new Window();
//                window.setCaption("Okno");
//                window.setWidth("50%");
//                window.setHeight("50%");
//                window.center();
//
//                UI.getCurrent().addWindow(window);

    }
//    public  void enter(ViewChangeListener.ViewChangeEvent event) {
//        System.out.println("AAAAAAA");
//       if (MyUI.jeUzivatelPrihlaseny()) {
//            System.out.println("je prihlaseny");
//            MyUI.getn.navigateTo(MainView.NAME);
//        }
//        else{
//            MyUI.navigator.navigateTo(LoginView.NAME);
//        }
//
//    }
}