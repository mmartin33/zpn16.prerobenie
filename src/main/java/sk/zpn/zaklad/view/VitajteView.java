package sk.zpn.zaklad.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;
import sk.zpn.domena.Firma;
import sk.zpn.domena.TypProduktov;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.zaklad.model.UzivatelNastroje;
import sk.zpn.zaklad.view.doklady.DokladyView;
import sk.zpn.zaklad.view.firmy.FirmyView;


import sk.zpn.zaklad.view.firmyVelkoskladu.FirmyVelkoskladuView;
import sk.zpn.zaklad.view.log.LogView;
import sk.zpn.zaklad.view.mostik.MostikView;
import sk.zpn.zaklad.view.nacitanieCSV.NacitanieCSVView;

import sk.zpn.zaklad.view.parametre.ParametreView;
import sk.zpn.zaklad.view.poberatelia.PoberateliaView;
import sk.zpn.zaklad.view.prevadzky.PrevadzkyView;
import sk.zpn.zaklad.view.produkty.ProduktyView;
import sk.zpn.zaklad.view.statistiky.StatDodavatelProduktView;
import sk.zpn.zaklad.view.statistiky.StatPoberatelView;
import sk.zpn.zaklad.view.uzivatel.UzivateliaView;


public class VitajteView extends MojView {
    MenuItem menuPredajcu;
    MenuItem menuSpravcu;
    MenuItem menuAdmin;
    MenuItem menuLogout;
    MenuItem menuUrcenieFiriem;
    Navigator n;
    public static final String NAME = "vitajteView";


    public VitajteView() {

    }

    public VitajteView(Navigator n) {
        this.n = n;

        MenuBar barmenu = new MenuBar();
        this.addComponent(barmenu);
        HorizontalLayout infoPanel = new HorizontalLayout();
        local(this);


//        ExternalResource resource = new ExternalResource("/img/zpn_logo.jpg");
//        infoPanel.addComponentsAndExpand(new Image(null,resource));
//            this.addComponent(infoPanel);
        // A top-level menu item that opens a submenu

        MenuBar.Command mycommand = new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                System.out.println(selectedItem.getText());// + MyUI.getUser().getPopis());
                if (selectedItem.getDescription().equals("firmy")) {
                    FirmyView firmyView = new FirmyView(null);
                    UI.getCurrent().getNavigator().addView(FirmyView.NAME, firmyView);
                    n.navigateTo(FirmyView.NAME);


                    n.navigateTo(FirmyView.NAME);
                } else if (selectedItem.getDescription().equals("parametre")) {
                    n.navigateTo(ParametreView.NAME);
                } else if (selectedItem.getDescription().equals("poberatelia")) {
                    PoberateliaView poberateliaView = new PoberateliaView(null);
                    UI.getCurrent().getNavigator().addView(PoberateliaView.NAME, poberateliaView);
                    n.navigateTo(PoberateliaView.NAME);
                } else if (selectedItem.getDescription().equals("produkty")) {
                    ProduktyView produktyView = new ProduktyView();
                    produktyView.setTypProduktov(TypProduktov.BODOVACI);
                    UI.getCurrent().getNavigator().addView(ProduktyView.NAME, produktyView);
                    n.navigateTo(ProduktyView.NAME);
                } else if (selectedItem.getDescription().equals("odmeny")) {
                    ProduktyView produktyView = new ProduktyView();
                    produktyView.setTypProduktov(TypProduktov.ODMENA);
                    UI.getCurrent().getNavigator().addView(ProduktyView.NAME, produktyView);
                    n.navigateTo(ProduktyView.NAME);

                } else if (selectedItem.getDescription().equals("prevadzky")) {
                    PrevadzkyView prevadzkyView = new PrevadzkyView(null);
                    UI.getCurrent().getNavigator().addView(PrevadzkyView.NAME, prevadzkyView);
                    n.navigateTo(PrevadzkyView.NAME);
                } else if (selectedItem.getDescription().equals("doklad")) {
                    DokladyView dokladyView = new DokladyView(null);
                    UI.getCurrent().getNavigator().addView(DokladyView.NAME, dokladyView);
                    n.navigateTo(DokladyView.NAME);
                } else if (selectedItem.getDescription().equals("dokladyVelkoskladu")) {
                    Firma velkosklad = UzivatelNastroje.getPrihlasenehoUzivatela().getFirma();
                    DokladyView dokladyView = new DokladyView(velkosklad);
                    UI.getCurrent().getNavigator().addView(DokladyView.NAME, dokladyView);
                    n.navigateTo(DokladyView.NAME);
                } else if (selectedItem.getDescription().equals("PoberateliaVelkoskladu")) {
                    Firma velkosklad = UzivatelNastroje.getPrihlasenehoUzivatela().getFirma();
                    PoberateliaView pobView = new PoberateliaView(null);
                    pobView.setVelkosklad(velkosklad);
                    UI.getCurrent().getNavigator().addView(PoberateliaView.NAME, pobView);
                    n.navigateTo(PoberateliaView.NAME);
                } else if (selectedItem.getDescription().equals("dokladyOdmien")) {
                    DokladyView dokladyView = new DokladyView(null);
                    dokladyView.setRezimOdmien();
                    UI.getCurrent().getNavigator().addView(DokladyView.NAME, dokladyView);
                    n.navigateTo(DokladyView.NAME);
                } else if (selectedItem.getDescription().equals("odosli")) {
                    NacitanieCSVView nacitanieCSVView = new NacitanieCSVView();
                    UI.getCurrent().getNavigator().addView(NacitanieCSVView.NAME, nacitanieCSVView);
                    n.navigateTo(NacitanieCSVView.NAME);
                } else if (selectedItem.getDescription().equals("mostik")) {
                    MostikView mostikView = new MostikView();
                    UI.getCurrent().getNavigator().addView(MostikView.NAME, mostikView);
                    n.navigateTo(MostikView.NAME);
                } else if (selectedItem.getDescription().equals("urcenieFiriem")) {
                    FirmyVelkoskladuView firmyVelkoskladuView = new FirmyVelkoskladuView();
                    UI.getCurrent().getNavigator().addView(FirmyVelkoskladuView.NAME, firmyVelkoskladuView);
                    n.navigateTo(FirmyVelkoskladuView.NAME);
                } else if (selectedItem.getDescription().equals("bodyDodavatelovAProduktov")) {
                    StatDodavatelProduktView statDodavatelProduktView = new StatDodavatelProduktView();
                    UI.getCurrent().getNavigator().addView(StatDodavatelProduktView.NAME, statDodavatelProduktView);
                    n.navigateTo(statDodavatelProduktView.NAME);
                } else if (selectedItem.getDescription().equals("bodyDodavatelovAProduktovPodlaBodov")) {
                    StatDodavatelProduktView statDodavatelProduktView = new StatDodavatelProduktView();
                    statDodavatelProduktView.setBodovyRezim();
                    UI.getCurrent().getNavigator().addView(StatDodavatelProduktView.NAME, statDodavatelProduktView);
                    n.navigateTo(statDodavatelProduktView.NAME);
                } else if (selectedItem.getDescription().equals("body")) {
                    StatPoberatelView statPoberatelView = new StatPoberatelView(null);
                    UI.getCurrent().getNavigator().addView(StatPoberatelView.NAME, statPoberatelView);
                    n.navigateTo(statPoberatelView.NAME);
                } else if (selectedItem.getDescription().equals("bodyPoberatelovVelkoskladu")) {
                    Firma velkosklad = UzivatelNastroje.getPrihlasenehoUzivatela().getFirma();
                    StatPoberatelView statPoberatelView = new StatPoberatelView(velkosklad);
                    UI.getCurrent().getNavigator().addView(StatPoberatelView.NAME, statPoberatelView);
                    n.navigateTo(statPoberatelView.NAME);
                }
            }
        };
        MenuBar.Command odhlasenie = new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                VaadinSession.getCurrent().setAttribute("id_uzivatela", null);
                VaadinSession.getCurrent().setAttribute("meno", null);
                UI.getCurrent().getNavigator().navigateTo(LoginView.NAME);
            }
        };
        MenuBar.Command sp_admin = new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {

                if (selectedItem.getDescription().equals("uzivatelia")) {
                    //UI.getCurrent().getNavigator().navigateTo(FirmyView.NAME);
                    System.out.println("Vybrate:" + selectedItem.getDescription());

                    n.navigateTo(UzivateliaView.NAME);
                } else if (selectedItem.getDescription().equals("log")) {
                    LogView LogView = new LogView();
                    UI.getCurrent().getNavigator().addView(LogView.NAME, LogView);
                    n.navigateTo(LogView.NAME);

                }
            }
        };

        menuLogout = barmenu.addItem("Odhlasenie", null, odhlasenie);
        menuPredajcu = barmenu.addItem("Pre predajcu", null, null);
        MenuItem menuMostik = menuPredajcu.addItem("Párovaci mostik", VaadinIcons.RESIZE_V, mycommand);
        menuMostik.setDescription("mostik");
        MenuItem menuOdosli = menuPredajcu.addItem("Odošli", VaadinIcons.UPLOAD, mycommand);
        menuOdosli.setDescription("odosli");

        menuUrcenieFiriem = menuPredajcu.addItem("Definínicia firiem pre zber", VaadinIcons.USER_STAR, mycommand);
        menuUrcenieFiriem.setDescription("urcenieFiriem");




        MenuItem menuDokladyVelkoskladu = menuPredajcu.addItem("Doklady - veľkoskladu prihláseného užívateľa", VaadinIcons.RECORDS, mycommand);
        menuDokladyVelkoskladu.setDescription("dokladyVelkoskladu");

        MenuItem menuBodyPoberatelovVelkoskladu = menuPredajcu.addItem("Stav bodov do XLS - poberateľov veľkoskladu prihláseného užívateľa", VaadinIcons.TABLE, mycommand);
        menuBodyPoberatelovVelkoskladu.setDescription("bodyPoberatelovVelkoskladu");

        MenuItem menuPoberateliaVelkoskladu = menuPredajcu.addItem("Poberatelia - veľkoskladu prihláseného užívateľa", VaadinIcons.USER_CHECK, mycommand);
        menuPoberateliaVelkoskladu.setDescription("PoberateliaVelkoskladu");


        menuSpravcu = barmenu.addItem("Spravca ZPN", null, null);
        MenuItem menuFirmy = menuSpravcu.addItem("Firmy", VaadinIcons.BUILDING, mycommand);
        menuFirmy.setDescription("firmy");

        MenuItem menuPrevadzky = menuSpravcu.addItem("Prevadzky", VaadinIcons.BUILDING_O, mycommand);
        menuPrevadzky.setDescription("prevadzky");
        MenuItem menuPoberatelia = menuSpravcu.addItem("Poberatelia", VaadinIcons.USER_CHECK, mycommand);
        menuPoberatelia.setDescription("poberatelia");

        MenuItem menuProdukty = menuSpravcu.addItem("Produkty", VaadinIcons.GLASS, mycommand);
        menuProdukty.setDescription("produkty");

        MenuItem menuOdmeny = menuSpravcu.addItem("Odmeny", VaadinIcons.RASTER, mycommand);
        menuOdmeny.setDescription("odmeny");

        MenuItem menuDoklad = menuSpravcu.addItem("Doklady", VaadinIcons.RECORDS, mycommand);
        menuDoklad.setDescription("doklad");

        MenuItem menuDokladyOdmien = menuSpravcu.addItem("Doklady odmien", VaadinIcons.RANDOM, mycommand);
        menuDokladyOdmien.setDescription("dokladyOdmien");

        MenuItem menuBody = menuSpravcu.addItem("Stav bodov - poberateľov", VaadinIcons.PIGGY_BANK, mycommand);
        menuBody.setDescription("body");


//        MenuItem menuBodyDodavatelovProduktov = menuSpravcu.addItem("Stav bodov - dodávateľov a produktov", VaadinIcons.PIGGY_BANK, mycommand);
//        menuBodyDodavatelovProduktov.setDescription("bodyDodavatelovAProduktov");

        MenuItem menuBodyDodavatelovProduktovPodlaBodov = menuSpravcu.addItem("Stav bodov - dodávateľov a produktov podľa bodov", VaadinIcons.PIGGY_BANK, mycommand);
        menuBodyDodavatelovProduktovPodlaBodov.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
        menuBodyDodavatelovProduktovPodlaBodov.setDescription("bodyDodavatelovAProduktovPodlaBodov");

        MenuItem menuParametre = menuSpravcu.addItem("Parametre", VaadinIcons.COG_O, mycommand);
        menuParametre.setDescription("parametre");

        menuAdmin = barmenu.addItem("Spravca", null, null);
        MenuItem menuUzivatelia = menuAdmin.addItem("Užívatelia", VaadinIcons.USERS, sp_admin);
        menuUzivatelia.setDescription("uzivatelia");
        MenuItem menuLog = menuAdmin.addItem("Log aplikácie", VaadinIcons.LIST, sp_admin);
        menuLog.setDescription("log");


    }

    void local(VerticalLayout layout) {
//    ExternalResource resource =
//    new ExternalResource("/img/zpn_logo.png");
//    Embedded image = new Embedded("", resource);
        layout.addComponent(new Image(null, new ClassResource("/img/zpn_logo.png")));

    }


    @Override
    protected void init() {
        addStyleName("welcome");
        setTitle("Vitajte", "Ste prihlásený ako " + VaadinSession.getCurrent().getAttribute("meno") + " >>verzia:200108<<");
        if (!UzivatelNastroje.TypUzivatela().isPresent()) {
            menuAdmin.setVisible(false);
            menuSpravcu.setVisible(false);
            menuPredajcu.setVisible(false);
            return;
        }

        if (TypUzivatela.PREDAJCA.equals(UzivatelNastroje.TypUzivatela().get())) {
            menuAdmin.setVisible(false);
            menuSpravcu.setVisible(false);
            if (UzivatelNastroje.getPrihlasenehoUzivatela().getUrcujeFirmyNaKtoreSaPridelujuBody())
                menuUrcenieFiriem.setEnabled(true);
            else
                menuUrcenieFiriem.setEnabled(false);
            return;
        }

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        super.enter(event);
    }
}


