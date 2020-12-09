package sk.zpn.nastroje;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.apache.poi.hssf.usermodel.*;
import sk.zpn.SystemoveParametre;
import sk.zpn.domena.Doklad;
import sk.zpn.domena.PolozkaDokladu;
import sk.zpn.zaklad.model.PolozkaDokladuNastroje;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class XlsExportDavky {
    private static final String FILE_NAME_SABLONY =  SystemoveParametre.getResourcesAdresar()+"export.xls";
    private static String FILE_NAME = "export.xls";
    private static HSSFWorkbook workbook;
    private static HSSFCellStyle cs;
    private static HSSFFont font ;


    public static void tlac(Doklad doklad) {
        if (doklad == null)
            return;

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date(System.currentTimeMillis());






        workbook = null;
        FileInputStream excelFile = null;
        try {
            excelFile = new FileInputStream(new File(FILE_NAME_SABLONY));

            workbook = new HSSFWorkbook(excelFile);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = workbook.getSheet("export");
        cs = workbook.createCellStyle();
        font = workbook.createFont();
        HSSFRow row = null;
        HSSFCell cel;

        //sem plnit
        String cisloDokladu=doklad.getCisloDokladu();
        String velkosklad=doklad.getFirmaNazov();
        Integer rowNum=1;
        Integer colNum=1;

        List<PolozkaDokladu> zoznamPoloziek=PolozkaDokladuNastroje.zoznamPoloziekDokladov(doklad);
        int i=1;
        for (PolozkaDokladu polozka : zoznamPoloziek){
            colNum=0;
            row = sheet.createRow(rowNum++);

            cel = row.createCell(colNum++);
            cel.setCellValue((String) cisloDokladu);

            cel = row.createCell(colNum++);
            cel.setCellValue((String) velkosklad);

            cel = row.createCell(colNum++);
            cel.setCellValue(polozka.getProduktKod());


            cel = row.createCell(colNum++);
            cel.setCellValue(polozka.getProduktNazov());

            cel = row.createCell(colNum++);
            cel.setCellValue(polozka.getPrevadzkaNazov());

            cel = row.createCell(colNum++);
            cel.setCellValue(polozka.getPoberatelMeno());

            cel = row.createCell(colNum++);
            cel.setCellValue(polozka.getBodyUpraveneBigInteger().toString());

            cel = row.createCell(colNum++);
            cel.setCellValue(polozka.getBodyZaProdukt(polozka).toString());

            cel = row.createCell(colNum++);
            cel.setCellValue(polozka.getMnozstvoBigInteger().toString());


            cel = row.createCell(colNum++);
            cel.setCellValue(polozka.getPoznamka().toString());





            //cel.setCellValue((String) f.getNazov());


        }

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



}

