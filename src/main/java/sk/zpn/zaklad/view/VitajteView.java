package sk.zpn.zaklad.view;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.zaklad.model.UzivatelNastroje;
import sk.zpn.zaklad.view.doklady.DokladyView;
import sk.zpn.zaklad.view.firmy.FirmyView;


import sk.zpn.zaklad.view.parametre.ParametreView;
import sk.zpn.zaklad.view.poberatelia.PoberateliaView;
import sk.zpn.zaklad.view.prevadzky.PrevadzkyView;
import sk.zpn.zaklad.view.produkty.ProduktyView;
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
                        PoberateliaView poberateliaView= new PoberateliaView(null);
                        UI.getCurrent().getNavigator().addView(PoberateliaView.NAME, poberateliaView);
                        n.navigateTo(PoberateliaView.NAME);
                    }
                    else if (selectedItem.getDescription().equals("produkty")) {
                        ProduktyView produktyView = new ProduktyView();
                        UI.getCurrent().getNavigator().addView(ProduktyView.NAME, produktyView);
                        n.navigateTo(ProduktyView.NAME);
                    }
                    else if (selectedItem.getDescription().equals("prevadzky")) {
                        PrevadzkyView prevadzkyView = new PrevadzkyView(null);
                        UI.getCurrent().getNavigator().addView(PrevadzkyView.NAME, prevadzkyView);
                        n.navigateTo(PrevadzkyView.NAME);
                    }
                    else if (selectedItem.getDescription().equals("doklad")) {
                        DokladyView dokladyView = new DokladyView();
                        UI.getCurrent().getNavigator().addView(DokladyView.NAME, dokladyView);
                        n.navigateTo(DokladyView.NAME);
                    }
                    else if (selectedItem.getDescription().equals("mostik")) {
//                        MostikView mostikView = new MostikView();
//                        UI.getCurrent().getNavigator().addView(MostikView.NAME, mostikView);
//                        n.navigateTo(MostikView.NAME);
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

            MenuItem menuPrevadzky = menuSpravcu.addItem("Prevadzky", VaadinIcons.BULLETS, mycommand);
            menuPrevadzky.setDescription("prevadzky");
            MenuItem menuPoberatelia = menuSpravcu.addItem("Poberatelia",VaadinIcons.USER_CHECK, mycommand);
            menuPoberatelia.setDescription("poberatelia");

            MenuItem menuProdukty = menuSpravcu.addItem("Produkty", VaadinIcons.GLASS, mycommand);
            menuProdukty.setDescription("produkty");
            MenuItem menuDoklad = menuSpravcu.addItem("Doklady", VaadinIcons.INVOICE, mycommand);
            menuDoklad.setDescription("doklad");
            MenuItem menuNezhrateDavky = menuSpravcu.addItem("Nezhrate davky", null, mycommand);
            menuNezhrateDavky.setDescription("davka");
            MenuItem menuBody = menuSpravcu.addItem("Stav bodov", null, mycommand);
            menuBody.setDescription("body");
            MenuItem menuParametre = menuSpravcu.addItem("Parametre", VaadinIcons.CONTROLLER, mycommand);
            menuParametre.setDescription("parametre");

            menuAdmin = barmenu.addItem("Spravca", null, null);
            MenuItem menuUzivatelia = menuAdmin.addItem("Užívatelia", VaadinIcons.USERS, sp_admin);
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


