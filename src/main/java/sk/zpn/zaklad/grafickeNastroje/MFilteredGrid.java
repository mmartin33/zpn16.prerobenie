package sk.zpn.zaklad.grafickeNastroje;


import com.vaadin.data.provider.DataProvider;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.ColumnReorderListener;
import com.vaadin.ui.components.grid.ColumnResizeListener;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.vaadin.addons.filteringgrid.FilterGrid;
import sk.zpn.SystemoveParametre;
import sk.zpn.domena.prostredie.UlozenyGrid;

import sk.zpn.nastroje.SaveToExcelLink;
import sk.zpn.zaklad.model.ProstredieUti;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class MFilteredGrid<T> extends FilterGrid<T> {
    //GridContextMenu gcm;
    private List<UlozenyGrid> ulozenyGrid;
    private String kluc;

    public MFilteredGrid() {
        pridajContextMenu();
    }

    public MFilteredGrid(Class<T> beanType) {
        super(beanType);
    }

    public MFilteredGrid(String caption) {
        super(caption);
    }

    public MFilteredGrid(String caption, Collection<T> items) {
        super(caption);
        this.setItems(items);
//        pridajContextMenu();
        this.addColumnResizeListener(new ColumnResizeListener() {
            @Override
            public void columnResize(ColumnResizeEvent event) {
                System.out.println(event.getColumn().getCaption());
            }
        });

    }


    public void pridajContextMenu(){
//        gcm=new GridContextMenu(this);


// Checkable item
////        final MenuBar.MenuItem item0 = gcm.addItem("Menu");
////        final MenuBar.MenuItem item01 = gcm.addItem("");
//        item0.setEnabled(false);
//        item01.setEnabled(false);
//        final MenuBar.MenuItem item = gcm.addItem("Export udajov s tabuľky do XLS", e -> {
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
        this.addColumnReorderListener(new ColumnReorderListener() {
            @Override
            public void columnReorder(ColumnReorderEvent event) {
                if (event.isUserOriginated())
                    ProstredieUti.setPoradieStlpcovGridu((Grid) event.getSource(),kluc);
            }
        });



        this.addColumnResizeListener(new ColumnResizeListener() {
            @Override
            public void columnResize(ColumnResizeEvent event) {
                ProstredieUti.setSirkyStlpcovGridu(kluc,event.getColumn().getId(),event.getColumn().getWidth());
                System.out.print(event.getColumn().getId()+event.getColumn().getWidth());

            }
        });

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
            cel.setCellValue("Vytvorené:" + formatter.format(date));


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
