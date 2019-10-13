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
import sk.zpn.zaklad.model.DokladyNastroje;
import sk.zpn.zaklad.model.PolozkaDokladuNastroje;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class XlsTlacProtokolu {
    private static final String FILE_NAME_SABLONY =  SystemoveParametre.getResourcesAdresar()+"preberaci-protokol.xls";
    private static String FILE_NAME = "preberaci-protokol.xls";
    private static HSSFWorkbook workbook;
    private static HSSFCellStyle cs;
    private static HSSFFont font ;


    public static void tlac(Doklad doklad) {
        if (doklad == null)
            return;
        if (doklad.getTypDokladu()!= TypDokladu.ODMENY) {
            return;
        }
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
        resultCell.setCellValue(doklad.getPoberatelMenoAdresa());
        resultCell = (HSSFCell) sheet.getRow(12).getCell(3);
        resultCell.setCellValue(doklad.getFirma().getIco());
        resultCell = (HSSFCell) sheet.getRow(13).getCell(3);
        resultCell.setCellValue(doklad.getFirma().getNazov());
        resultCell = (HSSFCell) sheet.getRow(14).getCell(3);
        resultCell.setCellValue(doklad.getCisloDokladuOdmeny());

        resultCell = (HSSFCell) sheet.getRow(32).getCell(6);
        resultCell.setCellValue(formatter.format(doklad.getDatum()));


        List<PolozkaDokladu> zoznamPoloziek=PolozkaDokladuNastroje.zoznamPoloziekDokladov(doklad);
        int riadok=23;
        for (PolozkaDokladu polozka : zoznamPoloziek){

            resultCell = (HSSFCell) sheet.getRow(riadok).getCell(2);
            resultCell.setCellValue(polozka.getProdukt().getKat().toString());
            resultCell = (HSSFCell) sheet.getRow(riadok).getCell(3);
            resultCell.setCellValue(polozka.getProdukt().getNazov().toString());
            resultCell = (HSSFCell) sheet.getRow(riadok).getCell(4);
            resultCell.setCellValue(polozka.getProdukt().getBody().intValue());
            resultCell = (HSSFCell) sheet.getRow(riadok).getCell(5);
            resultCell.setCellValue(polozka.getMnozstvo().multiply(new BigDecimal(-1)).intValue());
            resultCell = (HSSFCell) sheet.getRow(riadok).getCell(6);
            resultCell.setCellValue(polozka.getBody().multiply(new BigDecimal(-1)).intValue());
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



}

