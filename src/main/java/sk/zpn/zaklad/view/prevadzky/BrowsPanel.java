package sk.zpn.zaklad.view.prevadzky;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Prevadzka;
import sk.zpn.zaklad.view.VitajteView;
import sk.zpn.zaklad.view.firmy.FirmyView;
import sk.zpn.zaklad.view.poberatelia.PoberateliaView;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BrowsPanel extends VerticalLayout {


    private final Button btnPoberatelia;
    private FilterGrid<Prevadzka> grid;
    private List<Prevadzka> prevadzkaList;

    private PrevadzkyView prevadzkyView;
    private PoberateliaView poberateliaView;

    public Button btnNovy;

        public BrowsPanel(List<Prevadzka> prevadzkaList, PrevadzkyView prevadzkyView) {
            GridLayout gl =new GridLayout(1,3);
            gl.setSizeFull();
            gl.setRowExpandRatio(0, 0.05f);
            gl.setRowExpandRatio(1, 0.90f);
            gl.setRowExpandRatio(2, 0.05f);


            this.prevadzkyView=prevadzkyView;
            this.prevadzkaList = prevadzkaList;
            grid = new FilterGrid<>();
            grid.setItems(this.prevadzkaList);

            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            grid.setHeightByRows(5);


            // definitionn of columns
            FilterGrid.Column<Prevadzka, String> colnazov = grid.addColumn(Prevadzka::getNazov).setCaption("Názov").setId("nazov");
            FilterGrid.Column<Prevadzka, String> colFirmaNazov = grid.addColumn(Prevadzka::getFirmaNazov).setCaption("Firma").setId("nazovFirmy");
            FilterGrid.Column<Prevadzka, String> colUlica = grid.addColumn(Prevadzka::getUlica).setCaption("Ulica").setId("ulica");
            FilterGrid.Column<Prevadzka, String> colMesto = grid.addColumn(Prevadzka::getMesto).setCaption("Mesto").setId("mesto");
            FilterGrid.Column<Prevadzka, String> colPsc = grid.addColumn(Prevadzka::getPsc).setCaption("PSČ").setId("psc");
            FilterGrid.Column<Prevadzka, String> colPoberatelNazov = grid.addColumn(Prevadzka::getPoberatelMenoAdresa).setCaption("poberatel").setId("menoPoberatela");



            // filters

            grid.setColumnOrder(colnazov,colFirmaNazov,colPsc,colPoberatelNazov,colUlica, colMesto);

            Button btnSpat=new Button("Späť", VaadinIcons.ARROW_BACKWARD);
            btnSpat.addClickListener(clickEvent ->
            { if (prevadzkyView.getFirma()!=null)
                UI.getCurrent().getNavigator().navigateTo(FirmyView.NAME);
            else
                UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME);

            }
            );



            HorizontalLayout tlacitkovy=new HorizontalLayout();
            btnNovy=new Button("Novy",VaadinIcons.FILE_O);



            btnPoberatelia=new Button("Poberatelia", VaadinIcons.BOOK);
            btnPoberatelia.addClickListener(clickEvent -> {
                if (grid.getSelectedItems()!=null) {


                    poberateliaView = new PoberateliaView((Prevadzka) grid.getSelectedItems().iterator().next());

                    UI.getCurrent().getNavigator().addView(PoberateliaView.NAME, poberateliaView);
                    UI.getCurrent().getNavigator().navigateTo(PoberateliaView.NAME);
                }
                    }
            );

            tlacitkovy.addComponent(btnNovy);
            tlacitkovy.addComponent(btnPoberatelia);
            tlacitkovy.addComponent(btnSpat);//666


            String nadpis=new String("Prehľad prevadzok");
            if (this.prevadzkyView!=null)
                if (this.prevadzkyView.getFirma()!=null)
                    nadpis=nadpis+" firmy:"+this.prevadzkyView.getFirma().getNazov();


            btnSpat.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
            btnNovy.setClickShortcut(ShortcutAction.KeyCode.N,
                    new int[] { ShortcutAction.ModifierKey.ALT });


            gl.addComponent(new Label(nadpis));


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

        void addSelectionListener(Consumer<Prevadzka> listener) {
            grid.asSingleSelect()
                    .addValueChangeListener(e -> listener.accept(e.getValue()));
        }

        void deselect() {
            Prevadzka value = grid.asSingleSelect().getValue();
            if (value != null) {
                grid.getSelectionModel().deselect(value);
            }
        }

    void selectFirst() {
        List<Prevadzka> prvaPrevadzkaList = grid.getDataCommunicator().fetchItemsWithRange(0,1);
        if(prvaPrevadzkaList.size() > 0){
            grid.asSingleSelect().select(prvaPrevadzkaList.get(0));
        }
    }
    void selectPrevadzku(Prevadzka prevadzka) {
        Optional.ofNullable(prevadzka).ifPresent(grid.asSingleSelect()::select);
    }



    public PrevadzkyView getPrevadzkyView() {
        return prevadzkyView;
    }

    public void setPrevadzkyView(PrevadzkyView prevadzkyView) {
        this.prevadzkyView = prevadzkyView;
    }

    public void refresh(Prevadzka novaPrevazka) {
        grid.getDataProvider().refreshAll();
        grid.select(novaPrevazka);

    }
}






