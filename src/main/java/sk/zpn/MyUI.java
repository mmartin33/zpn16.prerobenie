package sk.zpn;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.NavigationStateManager;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import sk.zpn.zaklad.model.Pripojenie;
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

    public Navigator navigator;
    LoginView login;
    MainView  mainView;
    Pripojenie p;
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        p =new Pripojenie();

        login = new LoginView();
        mainView=new MainView(navigator);

        login.addLoginListener(e-> {
            String name = e.getLoginParameter("username");
            String pass = e.getLoginParameter("password");

            if (UzivatelNastroje.overUzivatela(name,pass)) {
                System.out.println("uzivatel overeny");
                VaadinSession.getCurrent().setAttribute("uzivatel","name");
                navigator.navigateTo(MainView.NAME);
            }
            else
                Notification.show("Nepodarilo sa !");
        });


        navigator = new Navigator(this, this);
        VaadinSession.getCurrent().setAttribute("navigator","navigator");
        navigator.addView(MainView.NAME, mainView);
        navigator.addView(LoginView.NAME, login);
        //navigator.navigateTo(LoginView.NAME);
        navigator.navigateTo(MainView.NAME);

        navigator.addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {

                if (jeUzivatelPrihlaseny()) {
                    System.out.println("je prihlaseny");
                    navigator.navigateTo(MainView.NAME);
                    return false;
                }
                else{
                    navigator.navigateTo(LoginView.NAME);
                    return false;
                   }

            }
        });


        //navigator.navigateTo(LoginView.NAME);
    }

    public final static boolean jeUzivatelPrihlaseny() {
        if (VaadinSession.getCurrent().getAttribute("uzivatel")==null)
            System.out.println("Nie je prihlaseny ziadny uzivatel");
        return VaadinSession.getCurrent().getAttribute("uzivatel") != null;
    }

    public static MyUI get() {
        UI ui = UI.getCurrent();
        if (ui instanceof MyUI) {
            return (MyUI) ui;
        }
        return null;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    @Override
    public Navigator getNavigator() {
        return navigator;
    }

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }
}
