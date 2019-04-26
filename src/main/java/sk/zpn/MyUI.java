package sk.zpn;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import sk.zpn.zaklad.model.LogPrihlaseniaNastroje;
import sk.zpn.zaklad.model.Pripojenie;
import sk.zpn.zaklad.model.UzivatelNastroje;
import sk.zpn.zaklad.view.firmy.FirmyView;

import sk.zpn.zaklad.view.LoginView;
import sk.zpn.zaklad.view.mostik.MostikView;
import sk.zpn.zaklad.view.parametre.ParametreView;
import sk.zpn.zaklad.view.poberatelia.PoberateliaView;
import sk.zpn.zaklad.view.produkty.ProduktyView;
import sk.zpn.zaklad.view.uzivatel.UzivateliaView;
import sk.zpn.zaklad.view.VitajteView;

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

    public boolean jeSpravca;
    LoginView login;
    VitajteView  vitajteView;
    UzivateliaView uzivateliaView;
//    PoberateliaView poberateliaView;
    ProduktyView produktyView;
    MostikView mostikView;
    //FirmyView firmyView;
    Pripojenie p;
    ParametreView parametre;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        p =new Pripojenie();
        navigator = new Navigator(this, this);

        login = new LoginView();
        vitajteView = new VitajteView(navigator);
        uzivateliaView=new UzivateliaView();
//        poberateliaView=new PoberateliaView();
        //produktyView=new ProduktyView();
        //mostikView=new MostikView();
        //firmyView=new FirmyView();
        parametre=new ParametreView();

        navigator.addView(VitajteView.NAME, vitajteView);
        navigator.addView(UzivateliaView.NAME, uzivateliaView);
        //navigator.addView(FirmyView.NAME, firmyView);
//        navigator.addView(PoberateliaView.NAME, poberateliaView);
        //navigator.addView(ProduktyView.NAME, produktyView);
        //navigator.addView(MostikView.NAME, mostikView);
        navigator.addView(LoginView.NAME, login);
        navigator.addView(ParametreView.NAME, parametre);

        boolean testRezim=true;

        if (testRezim){
            String name = "nkovacik";
            String pass = "nkovacik3569";
//            String name = "m";
//            String pass = "m789";
            UzivatelNastroje.overUzivatela(name,pass);
        }
        login.addLoginListener(e-> {
            String name = e.getLoginParameter("username");
            String pass = e.getLoginParameter("password");


            if (UzivatelNastroje.overUzivatela(name,pass)) {
                System.out.println("uzivatel overeny"+VaadinSession.getCurrent().getAttribute("meno")+VaadinSession.getCurrent().getAttribute("id_uzivatela"));
                new LogPrihlaseniaNastroje().uloz();

                navigator.navigateTo(VitajteView.NAME);
            }
            else
                Notification.show("Nepodarilo sa !");
        });

        VaadinSession.getCurrent().setAttribute("navigator","navigator");
        if (testRezim)
            navigator.navigateTo(VitajteView.NAME);
        else
            navigator.navigateTo(LoginView.NAME);

        navigator.addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                System.out.println(navigator.getCurrentView().toString());
                if (navigator.getCurrentView() instanceof LoginView) {
                    {
                        System.out.println(navigator.getCurrentView().toString());
                        return true;
                    }

                }
                if (jeUzivatelPrihlaseny()) {
                    System.out.println("ano");
//                    navigator.navigateTo(vitajteView.NAME);

                }
                else{
                    System.out.println("Nie ");
                    navigator.navigateTo(LoginView.NAME);

                   }
                return true;
            }
        });


        //navigator.navigateTo(LoginView.NAME);
    }
    public final static boolean jeUzivatelPrihlaseny() {
        if (VaadinSession.getCurrent().getAttribute("meno")==null)
            System.out.println("Nie je prihlaseny ziadny uzivatel");
        return VaadinSession.getCurrent().getAttribute("meno") != null;
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
