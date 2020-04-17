package sk.zpn.zaklad.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.LoginForm;


//public class LoginView extends LoginForm implements View {
public class LoginView extends LoginForm implements View {
    private HorizontalLayout hl=null;
    private LoginForm lf=null;
    public final static String NAME = "login";

    public LoginView() {
        hl=new HorizontalLayout();
        hl.addComponent(new Label("Hejejej"));
//        lf.setLoginButtonCaption("Prihlásenie");
//        lf.setPasswordCaption("heslo");
//        lf.setUsernameCaption("meno");
//        hl.addComponent(lf);
        lf=new LoginForm();
        Button restoreLogin = new Button("Restore login button",
                event -> lf.setEnabled(true));




    }
}
