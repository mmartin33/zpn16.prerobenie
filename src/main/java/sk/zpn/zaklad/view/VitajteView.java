package sk.zpn.zaklad.view;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.zaklad.model.UzivatelNastroje;
import sk.zpn.zaklad.view.firmy.FirmyView;

import sk.zpn.zaklad.view.parametre.ParametreView;
import sk.zpn.zaklad.view.poberatelia.PoberateliaView;
import sk.zpn.zaklad.view.uzivatel.UzivateliaView;


public class VitajteView extends MojView {
        MenuItem menuPredajcu;
        MenuItem menuSpravcu;
        MenuItem menuAdmin;
        MenuItem menuLogout;
        Navigator n;
    public static final String NAME = "vitajteView";


    public VitajteView() {

    }
    public VitajteView(Navigator n) {
            this.n=n;
            MenuBar barmenu = new MenuBar();
            this.addComponent(barmenu);
            // A top-level menu item that opens a submenu

            MenuBar.Command mycommand = new MenuBar.Command() {
                public void menuSelected(MenuItem selectedItem) {
                    System.out.println(selectedItem.getText());// + MyUI.getUser().getPopis());
                    if (selectedItem.getDescription().equals("firmy")) {
                        n.navigateTo(FirmyView.NAME);
                    }
                    else if (selectedItem.getDescription().equals("parametre")) {
                        n.navigateTo(ParametreView.NAME);
                    }
                    else if (selectedItem.getDescription().equals("poberatelia")) {
                        n.navigateTo(PoberateliaView.NAME);
                    }
                }
            };
            MenuBar.Command odhlasenie = new MenuBar.Command() {
                public void menuSelected(MenuItem selectedItem) {
                    VaadinSession.getCurrent().setAttribute("id_uzivatela",null);
                    VaadinSession.getCurrent().setAttribute("meno",null);
                    UI.getCurrent().getNavigator().navigateTo(LoginView.NAME);
                }
            };
            MenuBar.Command sp_admin = new MenuBar.Command() {
                public void menuSelected(MenuItem selectedItem) {

                    if (selectedItem.getDescription().equals("uzivatelia")){
                        //UI.getCurrent().getNavigator().navigateTo(FirmyView.NAME);
                        System.out.println("Vybrate:"+selectedItem.getDescription());

                        n.navigateTo(UzivateliaView.NAME);
                    }
                }
            };

            menuLogout = barmenu.addItem("Odhlasenie", null, odhlasenie);
            menuPredajcu = barmenu.addItem("Pre predajcu", null, null);
            MenuItem menuMostik = menuPredajcu.addItem("Párovaci postik", null, mycommand);
            menuMostik.setDescription("mostik");
            MenuItem menuOdosli = menuPredajcu.addItem("Odošli", null, mycommand);
            menuOdosli.setDescription("odosli");
            MenuItem menuStav = menuPredajcu.addItem("Stav bodov", null, mycommand);
            menuStav.setDescription("stavbodov");

            menuSpravcu = barmenu.addItem("Spravca ZPN", null, null);
            MenuItem menuFirmy = menuSpravcu.addItem("Firmy", null, mycommand);
            menuFirmy.setDescription("firmy");
            MenuItem menuPoberatelia = menuSpravcu.addItem("Poberatelia", null, mycommand);
            menuPoberatelia.setDescription("poberatelia");
            MenuItem menuNezhrateDavky = menuSpravcu.addItem("Nezhrate davky", null, mycommand);
            menuNezhrateDavky.setDescription("davka");
            MenuItem menuProdukty = menuSpravcu.addItem("Stav bodov", null, mycommand);
            menuProdukty.setDescription("body");
            MenuItem menuParametre = menuSpravcu.addItem("Parametre", null, mycommand);
            menuParametre.setDescription("parametre");

            menuAdmin = barmenu.addItem("Spravca", null, null);
            MenuItem menuUzivatelia = menuAdmin.addItem("Užívatelia", null, sp_admin);
            menuUzivatelia.setDescription("uzivatelia");






        }



    @Override
        protected void init() {
            addStyleName("welcome");
            setTitle("Vitajte", "Ste prihlásený ako " +VaadinSession.getCurrent().getAttribute("meno"));
            if  (!UzivatelNastroje.TypUzivatela().isPresent()) {
                menuAdmin.setVisible(false);
                menuSpravcu.setVisible(false);
                menuPredajcu.setVisible(false);
                return;
            }

            if  (TypUzivatela.PREDAJCA.equals(UzivatelNastroje.TypUzivatela().get())) {
                menuAdmin.setVisible(false);
                menuSpravcu.setVisible(false);
                return;
            }

        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            super.enter(event);
        }
    }


