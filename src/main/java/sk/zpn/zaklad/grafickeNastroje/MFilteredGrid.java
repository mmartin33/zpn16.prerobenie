package sk.zpn.zaklad.grafickeNastroje;


import com.vaadin.data.provider.DataProvider;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.ColumnReorderEvent;
import com.vaadin.flow.component.grid.ColumnResizeEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.flow.component.html.Label;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;

import sk.zpn.SystemoveParametre;
import sk.zpn.domena.prostredie.UlozenyGrid;

import sk.zpn.nastroje.SaveToExcelLink;
import sk.zpn.zaklad.model.ProstredieUti;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

    public class MFilteredGrid<T> extends Grid<T> {
    //GridContextMenu gcm;
    private List<UlozenyGrid> ulozenyGrid;
    private String kluc;

    public MFilteredGrid() {
        pridajContextMenu();
    }

    public MFilteredGrid(Class<T> beanType) {
        super(beanType);
    }

//    public MFilteredGrid(String caption) {
//        super(caption);
//    }

    public MFilteredGrid(String caption, Collection<T> items) {

        super();
        this.setItems(items);
//        pridajContextMenu();
        this.addColumnResizeListener(new ComponentEventListener<ColumnResizeEvent<T>>() {
            @Override
            public void onComponentEvent(ColumnResizeEvent<T> tColumnResizeEvent) {
                System.out.println(tColumnResizeEvent.getResizedColumn().getKey());
            }


        });

    }

    public static Component createFilterHeader(String labelText,
                                               Consumer<String> filterChangeConsumer) {
        Label label = new Label(labelText);
        label.getStyle().set("padding-top", "var(--lumo-space-m)")
                .set("font-size", "var(--lumo-font-size-xs)");
        TextField textField = new TextField();
//        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.setWidthFull();
        textField.getStyle().set("max-width", "100%");
        textField.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        VerticalLayout layout = new VerticalLayout(label, textField);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");

        return layout;
    }
    public void pridajContextMenu(){
//        gcm=new GridContextMenu(this);


// Checkable item
////        final MenuBar.MenuItem item0 = gcm.addItem("Menu");
////        final MenuBar.MenuItem item01 = gcm.addItem("");
//        item0.setEnabled(false);
//        item01.setEnabled(false);
//        final MenuBar.MenuItem item = gcm.addItem("Export udajov s tabu??ky do XLS", e -> {
//            ExportDoExcelu eXLS=new ExportDoExcelu();
//            eXLS.exportGrid(this.getColumns(),this.getDataProvider(),this.kluc);
//
//        });
//        item.setIcon(VaadinIcons.FILE_TABLE);



    }



    public void registrujZmenuStlpcov(String kluc) {

        this.kluc=kluc;
        if (kluc==null)
            return;
        this.setColumnReorderingAllowed(true);
        this.addColumnReorderListener(new ComponentEventListener<ColumnReorderEvent<T>>() {
            @Override
            public void onComponentEvent(ColumnReorderEvent<T> tColumnReorderEvent) {

//                ProstredieUti.setPoradieStlpcovGridu((Grid) this, kluc);
            }
        } );



        this.addColumnResizeListener(




//                ProstredieUti.setSirkyStlpcovGridu(kluc,event.getColumn().getId(),event.getColumn().getWidth());
//                System.out.print(event.getColumn().getId()+event.getColumn().getWidth());


        //vratenie siriek slpcov
        ulozenyGrid= ProstredieUti.getUlozeneSirkyStlpcovGridu(kluc);
        for (UlozenyGrid g : ulozenyGrid){
            this.getColumn(g.getNazovStlpca()).setWidth(g.getHodnota());
            System.out.println(this.getColumn(g.getNazovStlpca()).getCaption() +this.getColumn(g.getNazovStlpca()).getWidth());
        }
        //vratenie poradia slpcov
        String[] poradiaStlpcov=ProstredieUti.getUlozenePordiaStlpcovGridu(kluc);
        if (poradiaStlpcov!=null)
            this.setColumnOrder(poradiaStlpcov);



    }
    class ExportDoExcelu {
        HSSFCellStyle cs;
        HSSFFont font;
        String FILE_NAME = "prazdny.xls";
        String FILE_NAME_SABLONY = SystemoveParametre.getResourcesAdresar() + FILE_NAME;

        public <T> void exportGrid(List<Grid.Column<T, ?>> columns, DataProvider<T, ?> data,String nadpis) {


            HSSFWorkbook workbook;

            workbook = null;
            FileInputStream excelFile = null;
            try {
                excelFile = new FileInputStream(new File(this.FILE_NAME_SABLONY));
                workbook = new HSSFWorkbook(excelFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy ' ' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            HSSFSheet sheet = workbook.getSheet("zoznam");
            cs = workbook.createCellStyle();
            font = workbook.createFont();
            HSSFRow row;
            HSSFCell cel;
            int aktualnyRiadok=0;
            row = sheet.createRow(aktualnyRiadok);
            cel = row.createCell(0);
            cel.setCellValue("Vytvoren??:" + formatter.format(date));


            cel = row.createCell(1);
            cel.setCellStyle(nadpisovyformat());
            cel.setCellValue(nadpis);

            row = sheet.createRow(aktualnyRiadok++);


            row = sheet.createRow(aktualnyRiadok++);
            int i=0;

            for (Grid.Column<T, ?> stlpec : columns){
                cel = row.createCell(0);
                cel = row.createCell(i++);
                cel.setCellStyle(nadpisovyformat());
                cel.setCellValue(stlpec.getCaption());
            }
            row = sheet.createRow(aktualnyRiadok++);

//            Collection<T> items;
//            if (  ((DataProviderWrapper) data) instanceof DataProviderWrapper) {
//                DataProviderWrapper wrapper = (DataProviderWrapper) data;
//                if (  wrapper.dataProvider instanceof ListDataProvider)
//                    items = ((ListDataProvider<T>) data).getItems();
//            }

//            for ( DataProvider<T, ?> riadok : dataProvider) {
//            }


            try {

                FileOutputStream outputStream = new FileOutputStream(SystemoveParametre.getTmpAdresar() + FILE_NAME);
                workbook.write(outputStream);
                workbook.close();
                excelFile.close();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

            String filePath = SystemoveParametre.getTmpAdresar() + FILE_NAME;

            Window subWindow = new Window("");
            subWindow.setWidth(500, Sizeable.Unit.PIXELS);
            subWindow.setHeight(400, Sizeable.Unit.PIXELS);

            SaveToExcelLink s = new SaveToExcelLink(filePath);
            VerticalLayout vl = new VerticalLayout();
            vl.addComponentsAndExpand(s);
            subWindow.setContent(vl);
            subWindow.setModal(true);
            UI.getCurrent().addWindow(subWindow);

            return;

        }
        private HSSFCellStyle nadpisovyformat(){

            font.setBold(true);
            cs.setBorderRight(BorderStyle.MEDIUM);
            cs.setFont(font);
            return cs;
        }


    }
}
