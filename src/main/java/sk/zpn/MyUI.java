package sk.zpn;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.NavigationStateManager;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import sk.zpn.zaklad.model.UzivatelNastroje;
import sk.zpn.zaklad.view.LoginView;
import sk.zpn.zaklad.view.MainView;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    Navigator navigator;
    LoginView login;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        login = new LoginView();
        login.addLoginListener(e-> {
            String name = e.getLoginParameter("username");
            String pass = e.getLoginParameter("password");

            if (UzivatelNastroje.overUzivatela(name,pass)) {
                VaadinSession.getCurrent().setAttribute("uzivatel","name");
                navigator.navigateTo(MainView.NAME);
            }
            else
                Notification.show("Nepodarilo sa !");
        });


        navigator = new Navigator(this, this);
        navigator.addView(MainView.NAME, MainView.class);
        navigator.addView(LoginView.NAME, login);
        navigator.navigateTo(MainView.NAME);
    }

    public final static boolean jeUzivatelPrihlaseny() {
        return VaadinSession.getCurrent().getAttribute("user") != null;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
