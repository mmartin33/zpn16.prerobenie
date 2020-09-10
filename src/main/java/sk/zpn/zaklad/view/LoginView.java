package sk.zpn.zaklad.view;

import com.vaadin.navigator.View;
import com.vaadin.server.ClassResource;
import com.vaadin.ui.*;


//public class LoginView extends LoginForm implements View {
public class LoginView extends VerticalLayout implements View {
    private HorizontalLayout hl=null;
    public LoginForm lf=null;
    public final static String NAME = "login";

    public LoginView() {

        Image logo=new Image(null, new ClassResource("/img/zpn_logo.png"));
        lf=new LoginForm();
        lf.setUsernameCaption("Meno");
        lf.setPasswordCaption("Heslo");
        lf.setLoginButtonCaption("Prihlásenie");

        lf.setCaption("prihlásenie užívateľa");


        this.addComponent(logo);
        this.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        this.addComponent(lf);
        this.setComponentAlignment(lf, Alignment.MIDDLE_CENTER);


        Button restoreLogin = new Button("Restore login button",
                event -> lf.setEnabled(true));




    }
}
