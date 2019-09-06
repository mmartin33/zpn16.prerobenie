package sk.zpn.zaklad.view.doklady.polozkaDokladu;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.components.grid.MultiSelectionModelImpl;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import org.vaadin.dialogs.ConfirmDialog;
import sk.zpn.domena.Firma;
import sk.zpn.domena.PolozkaDokladu;
import sk.zpn.zaklad.model.PolozkaDokladuNastroje;
import sk.zpn.zaklad.view.doklady.DokladyView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private FilterGrid<PolozkaDokladu> grid;
    private HorizontalLayout infoPanel;
    private List<PolozkaDokladu> polozkyDokladuList;
    private Label lblInfopanelu;
    private PolozkyDokladuView polozkyDokladuView;
    public Button btnNovy;
    public Button btnNovyKopia;
    public Button btnPanelovy;
    private Firma velkosklad;


    public BrowsPanel(List<PolozkaDokladu> polozkyDokladuList, PolozkyDokladuView pdv) {
        this.velkosklad=pdv.getVelkosklad();
        GridLayout gl =new GridLayout(1,4);
        gl.setSizeFull();
        gl.setRowExpandRatio(0, 0.02f);
        gl.setRowExpandRatio(1, 0.90f);
        gl.setRowExpandRatio(2, 0.02f);
        gl.setRowExpandRatio(3, 0.02f);
        infoPanel=new HorizontalLayout();
        lblInfopanelu= new Label();
        infoPanel.addComponent(lblInfopanelu);

        this.polozkyDokladuView=pdv;
        this.polozkyDokladuList = polozkyDokladuList;
        grid = new FilterGrid<>();
        grid.setItems(this.polozkyDokladuList);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
//        MultiSelectionModelImpl<?> model = (MultiSelectionModelImpl<?>) grid.getSelectionModel();
//        model.setSelectAllCheckBoxVisibility(MultiSelectionModel.SelectAllCheckBoxVisibility.VISIBLE);



        // definitionn of columns
        FilterGrid.Column<PolozkaDokladu, String> colProduktKod = grid.addColumn(PolozkaDokladu::getProduktKod).setCaption("Kód produktu").setId("kodProduktu");
        FilterGrid.Column<PolozkaDokladu, String> colProduktNazov = grid.addColumn(PolozkaDokladu::getProduktNazov).setCaption("Názov produktu").setId("nazovProduktu");
        FilterGrid.Column<PolozkaDokladu, String> colPrevadzkaNazov = grid.addColumn(PolozkaDokladu::getPrevadzkaNazov).setCaption("Prevádzka").setId("nazovPrevadzky");
        FilterGrid.Column<PolozkaDokladu, String> colPoberatel = grid.addColumn(PolozkaDokladu::getPoberatelMenoAdresa).setCaption("poberatel").setId("menoPoberatela");
        FilterGrid.Column<PolozkaDokladu, BigInteger> colBody = grid.addColumn(PolozkaDokladu::getBodyBigInteger).setCaption("Body za predaj").setId("body");
        FilterGrid.Column<PolozkaDokladu, String> colBodyZaProdukt = grid.addColumn(PolozkaDokladu::getBodyZaProdukt).setCaption("Body/za MN").setId("bodyNaProdukte");
        FilterGrid.Column<PolozkaDokladu, BigInteger> colMnozstvo = grid.addColumn(PolozkaDokladu::getMnozstvoBigInteger).setCaption("Množstvo").setId("mnozstvo");
        FilterGrid.Column<PolozkaDokladu, String> colPoznamka = grid.addColumn(PolozkaDokladu::getPoznamka).setCaption("Poznámka").setId("poznamka");

        // filters
        colProduktKod.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colProduktNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colPrevadzkaNazov.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colPoberatel.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        colPoznamka.setFilter(new TextField(), StringComparator.containsIgnoreCase());
        Button btnSpat=new Button("Späť", VaadinIcons.ARROW_BACKWARD);
        btnSpat.addClickListener(clickEvent ->
                UI.getCurrent().getNavigator().navigateTo(DokladyView.NAME)
        );
        btnPanelovy=new Button("Zobraz/schovaj detail ", VaadinIcons.ARROWS_CROSS);

        Button btnZmaz=new Button("Zmaž", VaadinIcons.CLOSE_CIRCLE);
        btnZmaz.addClickListener(clickEvent ->
                zmaz()
        );


        HorizontalLayout tlacitkovy=new HorizontalLayout();
        btnNovy=new Button("Novy",VaadinIcons.FILE_O);
        btnNovyKopia=new Button("Novy kópia",VaadinIcons.FILE_O);



        btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        btnNovy.setClickShortcut(ShortcutAction.KeyCode.N,
                new int[] { ShortcutAction.ModifierKey.ALT });


        if (!jeRezimVelkoskladu()) {
            tlacitkovy.addComponent(btnNovy);
            tlacitkovy.addComponent(btnNovyKopia);
            tlacitkovy.addComponent(btnZmaz);
        }
        tlacitkovy.addComponent(btnSpat);//666
        tlacitkovy.addComponent(btnPanelovy);//666

        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String formatovanyDatum =simpleDateFormat.format(polozkyDokladuView.getDoklad().getDatum());


        gl.addComponent(new Label("Prehľad položiek dokladu:  "+polozkyDokladuView.getDoklad().getCisloDokladu()+" z dátumu:"+formatovanyDatum));


        gl.addComponents(grid);
        gl.setComponentAlignment(grid,Alignment.MIDDLE_LEFT);
        gl.addComponent(infoPanel);
        //gl.setComponentAlignment(infoPanel,Alignment.BOTTOM_LEFT);
        gl.addComponent(tlacitkovy);
        gl.setComponentAlignment(tlacitkovy,Alignment.BOTTOM_LEFT);
        gl.setVisible(true);
        aktualizujInfoPanle(0);
        grid.setSizeFull();
        this.setSizeFull();
        this.addComponentsAndExpand(gl);

        //grid.scrollToEnd();




    }


    private void aktualizujInfoPanle(int pocet){
        lblInfopanelu.setValue("Počet všetkých položiek: "+polozkyDokladuList.size());
    }

    private void zmaz() {
        if (grid.getSelectedItems().size()<=0)
            return;
        String otazka=new String("Naozaj si prajete odstrániť položku");
        if (grid.getSelectedItems().size()>1)
            otazka="Naozaj si prajete odstrániť položky";
        ConfirmDialog.show(UI.getCurrent(), "Odstránenie", otazka+"?",
                "Áno", "Nie", new ConfirmDialog.Listener() {

                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            // Confirmed to continue

                            for(Object o : grid.getSelectionModel().getSelectedItems()){
                                PolozkaDokladuNastroje.zmazPolozkyDoklady((PolozkaDokladu) o);
                                polozkyDokladuList.remove(o);
                            }
                            grid.getDataProvider().refreshAll();

                        }
                    }
                });

    }


    void refresh() {
        grid.getDataProvider().refreshAll();
        System.out.println("Refresh browsu all");

    }


    void addSelectionListener(Consumer<PolozkaDokladu> listener) {
        grid.asMultiSelect()
                .addValueChangeListener(e -> {
                    Set<PolozkaDokladu> polozkaDokladuSet = e.getValue();
                    listener.accept(polozkaDokladuSet.iterator().next());
                });
    }

//    void deselect() {
//        PolozkaDokladu value = grid.asSingleSelect().getValue();
//        if (value != null) {
//            grid.getSelectionModel().deselect(value);
//        }
//    }

    void selectFirst() {
        List<PolozkaDokladu> prvaPolozkaDokladu = grid.getDataCommunicator().fetchItemsWithRange(0,1);
        if(prvaPolozkaDokladu.size() > 0){
            grid.asMultiSelect().select(prvaPolozkaDokladu.get(0));
        }
    }

    void selectDoklad(PolozkaDokladu polozkaDokladu) {
        Optional.ofNullable(polozkaDokladu).ifPresent(grid.asMultiSelect()::select);
    }

    public void refresh(PolozkaDokladu p) {
        grid.getDataProvider().refreshAll();
        grid.select(p);

    }
    public boolean jeRezimVelkoskladu() {
        return this.velkosklad==null?false:true;
    }
}






