package sk.zpn.zaklad.view.poberatelia;

import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.ItemClickListener;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import sk.zpn.domena.FirmaRegistra;
import sk.zpn.domena.FirmySlovenskoDigital;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.UdajeRegistra;
import sk.zpn.zaklad.model.PoberatelNastroje;

import java.util.EventObject;
import java.util.List;

public class RegisterForm  extends VerticalLayout implements View {
    public static String NAME=   "registerView";
    FormLayout fl =null;
    UdajeRegistra ur =null;
    HorizontalLayout hl=null;
    Button btnOK=null;
    Button btnKoniec=null;
    Button btnRegisterFiriem=null;



    private TextField tfICO;
    private TextField tfTelefon;
    private TextField tfEmail;
    private TextField tfNazovPrevadzky;
    private TextField tfUlicaPrevadzky;
    private TextField tfMestoPrevadzky;
    private TextField tfPscPrevadzky;
    private TextField tfNazovPoberatela;
    private TextField tfUlicaPoberatela;
    private TextField tfMestoPoberatela;
    private TextField tfPscPoberatela;
    private String rodicovskyView;
    public Poberatel poberatel=null;


    public RegisterForm() {
        fl =new FormLayout();
        tfICO =new TextField("IČO");
        tfICO.setWidth("200");
        tfTelefon =new TextField("Telefon");
        tfTelefon.setWidth("300");
        tfEmail =new TextField("email");
        tfEmail.setWidth("400");
        tfNazovPrevadzky =new TextField("Prevádzka - názov");
        tfNazovPrevadzky.setWidth("500");
        tfUlicaPrevadzky =new TextField("Prevádzka - ulica");
        tfUlicaPrevadzky.setWidth("500");
        tfMestoPrevadzky =new TextField("Prevádzka - mesto");
        tfMestoPrevadzky.setWidth("500");
        tfPscPrevadzky =new TextField("Prevádzka - PSČ");
        tfPscPrevadzky.setWidth("100");
        tfNazovPoberatela =new TextField("Poberateľ - meno");
        tfNazovPoberatela.setWidth("500");
        tfUlicaPoberatela =new TextField("Poberateľ - ulica");
        tfUlicaPoberatela.setWidth("500");
        tfMestoPoberatela =new TextField("Poberateľ - mesto ");
        tfMestoPoberatela.setWidth("500");
        tfPscPoberatela =new TextField("Poberateľ - PSČ");
        tfPscPoberatela.setWidth("100");
        tfNazovPrevadzky.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
//                if (tfNazovPoberatela.getValue().length()==0)
//                    tfNazovPoberatela.setValue(event.getValue());
            }
        });

        tfUlicaPrevadzky.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
//                if (tfUlicaPoberatela.getValue().length()==0)
//                    tfUlicaPoberatela.setValue(event.getValue());
            }
        });
        tfPscPrevadzky.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
//                if (tfPscPoberatela.getValue().length()==0)
//                    tfPscPoberatela.setValue(event.getValue());
            }
        });
        tfMestoPrevadzky.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> event) {
//                if (tfMestoPoberatela.getValue().length()==0)
//                    tfMestoPoberatela.setValue(event.getValue());
            }
        });

        fl.addComponent(tfICO);
        fl.addComponent(tfTelefon);
        fl.addComponent(tfEmail);
        fl.addComponent(tfNazovPrevadzky);
        fl.addComponent(tfUlicaPrevadzky);
        fl.addComponent(tfMestoPoberatela);
        fl.addComponent(tfNazovPoberatela);
        fl.addComponent(tfPscPrevadzky);
        fl.addComponent(tfUlicaPoberatela);
        fl.addComponent(tfMestoPoberatela);
        fl.addComponent(tfPscPoberatela);
        hl = new HorizontalLayout();
        btnOK=new Button("Založ", VaadinIcons.CHECK_CIRCLE);
        btnKoniec=new Button("Koniec",VaadinIcons.CLOSE_CIRCLE);
        btnRegisterFiriem=new Button("Register firiem Digital slovensko",VaadinIcons.OPEN_BOOK);

        btnOK.addClickListener(this::uloz);
        btnKoniec.addClickListener(this::koniec);
        btnRegisterFiriem.addClickListener(this::register);

        hl.addComponent(btnOK);
        hl.addComponent(btnKoniec);
        hl.addComponent(btnRegisterFiriem);
        this.addComponent(fl);
        this.addComponent(hl);

    }

    private void register(Button.ClickEvent clickEvent) {
        if ((tfNazovPrevadzky.getValue().length() < 1) && (tfICO.getValue().length() < 3))
            return;
        Window subWindow = new Window("Firmy");
        subWindow.setWidth(900,Unit.PIXELS);
        subWindow.setHeight(1900,Unit.PIXELS);
        FilterGrid<FirmaRegistra> grid;
        FirmySlovenskoDigital fsd = new FirmySlovenskoDigital();

        List<FirmaRegistra> firmaList = fsd.nacitanieDat(tfNazovPrevadzky.getValue(), tfICO.getValue(), false);

        grid = new FilterGrid<>();
        grid.setItems(firmaList);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setWidth(100, Unit.PERCENTAGE);
        grid.setHeight(100, Unit.PERCENTAGE);
        grid.setHeightByRows(15);

        grid.addItemClickListener(new ItemClickListener<FirmaRegistra>() {
                                      @Override

                                      public void itemClick(Grid.ItemClick<FirmaRegistra> event) {
                                          if (event.getMouseEventDetails().isDoubleClick()) {
                                              tfNazovPrevadzky.setValue(event.getItem().getNazov());
                                              //tfMestoPoberatela.setValue(event.getItem().getNazov());
                                              tfICO.setValue(event.getItem().getIco());
                                              tfPscPoberatela.setValue(event.getItem().getPsc());
                                              tfPscPrevadzky.setValue(event.getItem().getPsc());
                                              tfUlicaPoberatela.setValue(event.getItem().getUlica()+" "+event.getItem().getCisloDomu());
                                              tfUlicaPrevadzky.setValue(event.getItem().getUlica()+" "+event.getItem().getCisloDomu());
                                              tfMestoPoberatela.setValue(event.getItem().getObec());
                                              tfMestoPrevadzky.setValue(event.getItem().getObec());
                                              subWindow.close();
                                          } }
                                  }
        );


        // definitionn of columns
        FilterGrid.Column<FirmaRegistra, String> colIco = grid.addColumn(FirmaRegistra::getIco).setCaption("IČO").setId("ico");
        FilterGrid.Column<FirmaRegistra, String> colNazov = grid.addColumn(FirmaRegistra::getNazov).setCaption("Názov").setId("nazov");
        FilterGrid.Column<FirmaRegistra, String> colUlica = grid.addColumn(FirmaRegistra::getUlica).setCaption("Ulica").setId("ulica");
        FilterGrid.Column<FirmaRegistra, String> colPsc = grid.addColumn(FirmaRegistra::getPsc).setCaption("Psč").setId("psc");
        FilterGrid.Column<FirmaRegistra, String> colMesto = grid.addColumn(FirmaRegistra::getObec).setCaption("Mesto/obec").setId("mesto");


        // filters
        colIco.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colNazov.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colUlica.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colPsc.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        colMesto.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        grid.setColumnOrder(colIco, colNazov, colUlica, colPsc, colMesto);

        VerticalLayout vl = new VerticalLayout();
        vl.addComponentsAndExpand(grid);
        subWindow.setContent(vl);
        subWindow.setModal(true);


        subWindow.center();
        UI.getCurrent().addWindow(subWindow);

    }

    private void koniec(Button.ClickEvent clickEvent) {

        koniec();
    }

    private void uloz(Button.ClickEvent clickEvent) {
        UdajeRegistra ur=new UdajeRegistra();
        ur.setEmail(tfEmail.getValue());
        ur.setICO(tfICO.getValue());
        ur.setNazovPoberatela(tfNazovPoberatela.getValue());
        ur.setUlicaPoberatela(tfUlicaPoberatela.getValue());
        ur.setMestoPoberatela(tfMestoPoberatela.getValue());
        ur.setPscPoberatel(tfPscPoberatela.getValue());
        ur.setNazovPrevadzky(tfNazovPrevadzky.getValue());
        ur.setUlicaPrevadzky(tfUlicaPrevadzky.getValue());
        ur.setMestoPrevadzky(tfMestoPrevadzky.getValue());
        ur.setPscPrevadzky(tfPscPrevadzky.getValue());
        ur.setTelefon(tfTelefon.getValue());
        this.setPoberatel(PoberatelNastroje.registrujPoberatela(ur));
        this.fireEvent(new Event(this));
        koniec();



    }

    private void setPoberatel(Poberatel registrujPoberatela) {
        this.poberatel=registrujPoberatela;
    }

    private void koniec() {
        if (this.getRodicovskyView() != null)

            UI.getCurrent().getNavigator().navigateTo(getRodicovskyView());

    }

    private String getRodicovskyView() {
        return this.rodicovskyView;
    }

    public void setRodicovskyView(String viewName) {
        this.rodicovskyView=viewName;
    }

    public Poberatel getPoberatela() {
        return this.getPoberatela();
    }
}
