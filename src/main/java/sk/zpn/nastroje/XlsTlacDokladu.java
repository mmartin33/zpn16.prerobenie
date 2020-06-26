package sk.zpn.nastroje;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.apache.poi.hssf.usermodel.*;
import sk.zpn.SystemoveParametre;
import sk.zpn.domena.Doklad;
import sk.zpn.domena.PolozkaDokladu;
import sk.zpn.domena.TypDokladu;
import sk.zpn.zaklad.model.PolozkaDokladuNastroje;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class XlsTlacDokladu {
    private static final String FILE_NAME_SABLONY =  SystemoveParametre.getResourcesAdresar()+"doklad.xls";
    private static String FILE_NAME = "doklad.xls";
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
        HSSFSheet sheet = workbook.getSheet("protokol");
        cs = workbook.createCellStyle();
        font = workbook.createFont();
        HSSFRow row;
        HSSFCell cel;

        //sem plnit

        HSSFCell resultCell;
        resultCell = (HSSFCell) sheet.getRow(11).getCell(3);

        resultCell = (HSSFCell) sheet.getRow(12).getCell(3);

        resultCell = (HSSFCell) sheet.getRow(13).getCell(3);
        resultCell.setCellValue(doklad.getFirma().getNazov());
        resultCell = (HSSFCell) sheet.getRow(14).getCell(3);
        resultCell.setCellValue(doklad.getCisloDokladu());


        resultCell = (HSSFCell) sheet.getRow(22).getCell(3);
        resultCell.setCellValue("Poberateľ");

        resultCell = (HSSFCell) sheet.getRow(22).getCell(6);
        resultCell.setCellValue("Body");

        List<PolozkaDokladu> zoznamPoloziek=PolozkaDokladuNastroje.zoznamPoloziekDokladov(doklad);




        int riadok=23;
        BigDecimal bodyCelkom=new BigDecimal(BigInteger.ZERO);
        for (PolozkaDokladu polozka : zoznamPoloziek){

            resultCell = (HSSFCell) sheet.createRow(riadok).createCell(2);
            //resultCell = (HSSFCell) sheet.getRow(riadok).getCell(2);
            resultCell.setCellValue(polozka.getPoberatel().getKod());
            resultCell = (HSSFCell) sheet.getRow(riadok).createCell(3);
            resultCell.setCellValue(polozka.getPoberatel().getMeno());
            resultCell = (HSSFCell) sheet.getRow(riadok).createCell(6);
            resultCell.setCellValue(polozka.getBody().intValue());
            bodyCelkom.add(polozka.getBody());
            riadok++;
        }
        resultCell = (HSSFCell) sheet.createRow(riadok).createCell(2);
        resultCell = (HSSFCell) sheet.getRow(riadok).createCell(6);
        resultCell.setCellValue(bodyCelkom.intValue());
        resultCell = (HSSFCell) sheet.getRow(riadok).createCell(3);
        resultCell.setCellValue("Súčet");


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

