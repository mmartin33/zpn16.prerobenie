package sk.zpn.nastroje;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.apache.poi.hssf.usermodel.*;
import sk.zpn.SystemoveParametre;
import sk.zpn.domena.Doklad;
import sk.zpn.domena.TypDokladu;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XlsTlacProtokolu {
    private static final String FILE_NAME_SABLONY =  SystemoveParametre.getResourcesAdresar()+"preberaci-protokol.xls";
    private static String FILE_NAME = "preberaci-protokol.xls";
    private static HSSFWorkbook workbook;
    private static HSSFCellStyle cs;
    private static HSSFFont font ;


    public static void tlac(Doklad doklad) {
        if (doklad == null)
            return;
        if (doklad.getTypDokladu()!= TypDokladu.INTERNY_DOKLAD) {
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy ' ' HH:mm:ss z");
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
        HSSFSheet sheet = workbook.getSheet("zoznam");
        cs = workbook.createCellStyle();
        font = workbook.createFont();
        HSSFRow row;
        HSSFCell cel;

        //sem plnit

        row=sheet.getRow(12);
        cel=row.getCell(4);
        //cel.setCellValue(doklad);


        ///////////



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

