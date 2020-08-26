
package sk.zpn.nastroje;


import com.vaadin.server.Sizeable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import sk.zpn.SystemoveParametre;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.StatistikaBodovPoRokochAVelkoskladoch;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.zaklad.model.UzivatelNastroje;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class XlsStatPoRokochAVelkoskladoch {


    private static final String FILE_NAME_SABLONY = SystemoveParametre.getResourcesAdresar() + "statistika-bododov-po-rokokoch.xls";
    private static String FILE_NAME = "statistika-bododov-po-rokokoch.xls";
    private static HSSFWorkbook workbook;
    private static HSSFCellStyle cs;
    private static HSSFFont font;


    public static void vytvorXLSBilancie(Map<String, BigDecimal> statistika,
                                         List<Firma> velkosklady,
                                         String nadpis,
                                         String rokOd,
                                         String rokDo) {


        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy ' ' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());


        if (velkosklady == null || rokOd == null || rokDo == null)
            return;
        Integer iRokOd = new Integer(rokOd);
        Integer iRokDo = new Integer(rokDo);
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
        row = sheet.createRow(0);
        cel = row.createCell(0);
        cel.setCellValue("Vytvorené:" + formatter.format(date));

        row = sheet.createRow(1);
        cel = row.createCell(1);
        cel.setCellStyle(nadpisovyformat());
        cel.setCellValue(nadpis);

        int rowNum = 3;
        HSSFCell bunka;
        int colNum = 0;
        BigDecimal hodnota = BigDecimal.ONE;
        //nadpis//

        colNum = 2;
        rowNum = 2;
        row = sheet.createRow(rowNum);
        for (int rok = iRokOd.intValue(); rok <= iRokDo.intValue(); rok++) {
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue(rok);

        }
        String kluc;
        rowNum = 3;
        for (Firma f : velkosklady) {
            colNum = 1;
            row = sheet.createRow(rowNum++);
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue(f.getNazov());
            for (int rok = iRokOd.intValue(); rok <= iRokDo.intValue(); rok++) {


                hodnota = BigDecimal.ONE;
                kluc = f.getIco() + '*' + new Integer(rok).toString();
                hodnota = statistika.get(kluc);
                bunka = row.createCell(colNum++);
                bunka.setCellStyle(oramovanieBold());
                if (hodnota != null) {
                    bunka.setCellValue(hodnota.intValue());

                }
            }

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
        cs.setBorderRight(BorderStyle.MEDIUM);
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

    private static HSSFCellStyle oramovanieBoldZalomenie() {

        font.setBold(true);
        cs.setWrapText(true);

        cs.setBorderRight(BorderStyle.MEDIUM);
        cs.setFont(font);
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);

        return cs;
    }

    private static HSSFCellStyle oramovane() {


        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        return cs;
    }


}

