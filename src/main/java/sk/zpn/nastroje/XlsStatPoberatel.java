package sk.zpn.nastroje;

import com.vaadin.server.ClassResource;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sk.zpn.SystemoveParametre;
import sk.zpn.domena.StatPoberatel;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class XlsStatPoberatel {


    private static String FILE_NAME = SystemoveParametre.getResourcesAdresar()+"statistika-poberatelov.xlsx";



    public static String vytvorXLS(List<StatPoberatel> zoznam,String nadpis) {
        SimpleDateFormat formatter= new SimpleDateFormat("dd.MM.yyyy ' ' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());

        if (zoznam == null)
            return null;
        XSSFWorkbook workbook = null;
        FileInputStream excelFile = null;
        try {
            excelFile = new FileInputStream(new File(FILE_NAME));
            //File outFile = new File(new URL("file:"+FILE_NAME).getFile());
            workbook = new XSSFWorkbook(excelFile);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = workbook.getSheet("zoznam");
        Row row;
        Cell cel;
        row = sheet.createRow(0);
        cel=row.createCell(0);
        cel.setCellValue("Vytvorene:"+ formatter.format(date));
        row = sheet.createRow(1);
        cel=row.createCell(2);
        cel.setCellValue(nadpis);
        int rowNum = 2;
        Cell cel1;
        Cell cel2;
        Cell cel3;
        Cell cel4;

        for (StatPoberatel z : zoznam) {
            row = sheet.createRow(rowNum++);
            int colNum = 1;
            cel1 = row.createCell(colNum++);
            cel1.setCellValue((String) z.getPoberatelNazov());
            cel2 = row.createCell(colNum++);
            cel2.setCellValue((z.getPociatocnyStav()==null)?0:z.getPociatocnyStav().doubleValue());
            cel3 = row.createCell(colNum++);
            cel3.setCellValue((z.getBodyZaPredaj()==null)?0:z.getBodyZaPredaj().doubleValue());
            cel4 = row.createCell(colNum++);
            cel4.setCellValue((z.getBodyIne()==null)?0:z.getBodyIne().doubleValue());
            cel4 = row.createCell(colNum++);
            cel4.setCellValue((z.getKonecnyStav()==null)?0:z.getKonecnyStav().doubleValue());
        }
        try {

            FileOutputStream outputStream = new FileOutputStream(SystemoveParametre.getTmpAdresar()+"StatistikaBodovPoberatela.xlsx");
            workbook.write(outputStream);
            workbook.close();
            excelFile.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        String filePath = SystemoveParametre.getTmpAdresar()+"StatistikaBodovPoberatela.xlsx";

        Window subWindow = new Window("");
        subWindow.setWidth(500, Sizeable.Unit.PIXELS);
        subWindow.setHeight(400, Sizeable.Unit.PIXELS);

        SaveToExcelLink s=new SaveToExcelLink(filePath);
        VerticalLayout vl = new VerticalLayout();
        vl.addComponentsAndExpand(s);
        subWindow.setContent(vl);
        subWindow.setModal(true);
        UI.getCurrent().addWindow(subWindow);

        return null;
    }

}

