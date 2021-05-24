package sk.zpn.nastroje;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import sk.zpn.SystemoveParametre;
import sk.zpn.domena.FirmaProdukt;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XlsExportMostika {
    private static HSSFWorkbook workbook;
    private static HSSFCellStyle cs;
    private static HSSFFont font;


    public static void tlac(List<FirmaProdukt>  zoznam) {
        String FILE_NAME_SABLONY = SystemoveParametre.getResourcesAdresar() + "mostik.xls";
        String FILE_NAME = "mostik.xls";

        if (zoznam == null)
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
        HSSFSheet sheet = workbook.getSheet("zoznam");
        cs = workbook.createCellStyle();
        font = workbook.createFont();
        HSSFRow row;
        HSSFCell cel;

        //sem plnit

        HSSFCell resultCell;
        resultCell = (HSSFCell) sheet.getRow(0).createCell(1);
        resultCell.setCellStyle(nadpisovyformat());
//        resultCell.setCellValue(firma);
        resultCell = (HSSFCell) sheet.getRow(1).createCell(1);
        resultCell.setCellStyle(nadpisovyformat());
        resultCell.setCellValue("Mostik");

        resultCell = (HSSFCell) sheet.createRow(2).createCell(1);
        resultCell.setCellValue("Dátum vytvorenia:" + formatter.format(date));
        int riadok = 5;

        for (FirmaProdukt polozka : zoznam) {


            resultCell = (HSSFCell) sheet.createRow(riadok).createCell(1);
            resultCell.setCellStyle(oramovanie());
            resultCell.setCellValue(polozka.getKit());
            resultCell = (HSSFCell) sheet.getRow(riadok).createCell(2);
            resultCell.setCellValue(polozka.getProdukt().getKat());

            resultCell.setCellStyle(oramovanieZalomenie());
            resultCell = (HSSFCell) sheet.getRow(riadok).createCell(3);
            resultCell.setCellValue(polozka.getProdukt().getNazov());
            resultCell.setCellStyle(oramovanie());
            resultCell = (HSSFCell) sheet.getRow(riadok).createCell(4);
            resultCell.setCellValue(polozka.getKoeficient().doubleValue());
            resultCell.setCellStyle(oramovanie());
            riadok++;
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



    private static HSSFCellStyle nadpisovyformat() {

        font.setBold(true);
        //cs.setBorderRight(BorderStyle.MEDIUM);
        cs.setFont(font);
        return cs;
    }

    private static HSSFCellStyle oramovanieBold() {

        font.setBold(true);
        cs.setBorderRight(BorderStyle.MEDIUM);
        cs.setFont(font);
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);

        return cs;
    }

    private static HSSFCellStyle oramovanie() {

        font.setBold(false);
        cs.setBorderRight(BorderStyle.MEDIUM);
        //cs.setFont(font);
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);

        return cs;
    }

    private static HSSFCellStyle oramovanieZalomenie() {


        cs.setWrapText(true);

        cs.setBorderRight(BorderStyle.MEDIUM);

        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);

        return cs;
    }


}
