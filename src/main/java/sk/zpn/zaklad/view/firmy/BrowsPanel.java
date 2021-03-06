package sk.zpn.zaklad.view.firmy;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter.StringComparator;
import sk.zpn.domena.Firma;

import sk.zpn.zaklad.grafickeNastroje.MFilteredGrid;
import sk.zpn.zaklad.view.prevadzky.PrevadzkyView;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private MFilteredGrid<Firma> grid;
    private List<Firma> firmaList;
    private PrevadzkyView prevadzkyView;

    public Button btnNovy;
    public Button btnSpat;
    public Button btnPrevadzky;


        public BrowsPanel(List<Firma> firmaList) {
            GridLayout gl =new GridLayout(1,3);
            gl.setSizeFull();
            gl.setRowExpandRatio(0, 0.05f);
            gl.setRowExpandRatio(1, 0.90f);

            this.firmaList = firmaList;
            grid = new MFilteredGrid<>();
            grid.setItems(this.firmaList);

            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            grid.setHeightByRows(15);

            // definitionn of columns
            MFilteredGrid.Column<Firma, String> colIco = grid.addColumn(Firma::getIco).setCaption("IČO").setId("ico");
            MFilteredGrid.Column<Firma, String> colNazov = grid.addColumn(Firma::getNazov).setCaption("Názov").setId("nazov");
            MFilteredGrid.Column<Firma, String> colUlica = grid.addColumn(Firma::getUlica).setCaption("Ulica").setId("ulica");
            MFilteredGrid.Column<Firma, String> colMesto = grid.addColumn(Firma::getMesto).setCaption("Mesto").setId("mesto");
            MFilteredGrid.Column<Firma, String> colDic = grid.addColumn(Firma::getDic).setCaption("DIČ").setId("dic");
            MFilteredGrid.Column<Firma, String> colIcDPH = grid.addColumn(Firma::getIc_dph).setCaption("IČ DPH").setId("icdph");
            MFilteredGrid.Column<Firma, String> colPsc = grid.addColumn(Firma::getPsc).setCaption("PSČ").setId("psc");
            MFilteredGrid.Column<Firma, String> colTelefon = grid.addColumn(Firma::getTelefon).setCaption("Telefón").setId("telefon");
            MFilteredGrid.Column<Firma, BigDecimal> colPociatovnyStav = grid.addColumn(Firma::getPociatocnyStav).setCaption("PS Bodov").setId("ps_body");


            grid.registrujZmenuStlpcov("firmy");


            // filters
            colIco.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colNazov.setFilter(new TextField(), InMemoryFilter.StringComparator.containsIgnoreCase());
            colIcDPH.setFilter(new TextField(), StringComparator.containsIgnoreCase());
            colDic.setFilter(new TextField(), StringComparator.containsIgnoreCase());


            btnSpat=new Button("Späť", VaadinIcons.ARROW_BACKWARD);
//            btnSpat.addClickListener(clickEvent ->
//                    UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME)
//            );

            btnPrevadzky=new Button("Prevadzky", VaadinIcons.BOOK);
            btnPrevadzky.addClickListener(clickEvent -> {
                        if (grid.getSelectedItems()!=null) {


                            prevadzkyView = new PrevadzkyView((Firma) grid.getSelectedItems().iterator().next());

                            UI.getCurrent().getNavigator().addView(PrevadzkyView.NAME, prevadzkyView);
                            UI.getCurrent().getNavigator().navigateTo(PrevadzkyView.NAME);
                        }
                    }
            );

            HorizontalLayout tlacitkovy=new HorizontalLayout();
            btnNovy=new Button("Novy",VaadinIcons.FILE_O);

            btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
            btnNovy.setClickShortcut(ShortcutAction.KeyCode.N,
                    new int[] { ShortcutAction.ModifierKey.ALT });


            tlacitkovy.addComponent(btnNovy);
            tlacitkovy.addComponent(btnPrevadzky);

            tlacitkovy.addComponent(btnSpat);//666



            gl.addComponent(new Label("Prehľad firiem"));


            gl.addComponents(grid);
            gl.setComponentAlignment(grid,Alignment.MIDDLE_LEFT);

            gl.addComponent(tlacitkovy);
            gl.setComponentAlignment(tlacitkovy,Alignment.BOTTOM_LEFT);
            gl.setVisible(true);
            grid.setSizeFull();
            this.setSizeFull();
            this.addComponentsAndExpand(gl);



        }


        void refresh() {
            grid.getDataProvider().refreshAll();
            System.out.println("Refresh browsu all");

        }

    void addEditListener(Runnable editListener) {
        btnNovy.addClickListener(e -> editListener.run());

    }

        void addSelectionListener(Consumer<Firma> listener) {
            grid.asSingleSelect()
                    .addValueChangeListener(e -> listener.accept(e.getValue()));
        }

        void deselect() {
            Firma value = grid.asSingleSelect().getValue();
            if (value != null) {
                grid.getSelectionModel().deselect(value);
            }
        }

    public void refresh(Firma firma) {
        grid.getDataProvider().refreshAll();
        grid.select(firma);

    }
}






