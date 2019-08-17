package sk.zpn.nastroje;


import com.vaadin.server.Sizeable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import sk.zpn.SystemoveParametre;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Produkt;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class XlsStatistikaBodovDodavatelProdukt {


    private static String FILE_NAME_SABLONY = SystemoveParametre.getResourcesAdresar() + "statistikaDAP.xls";
    private static String FILE_NAME = "StatistikaBodovVelkoskladAProdukty.xls";
    private static CellStyle stylBunky;
    private static HSSFWorkbook workbook;


    CellType stylNadpis;

    public static String vytvorXLS(List<Firma> velkosklady,
                                   List<Produkt> produkty,
                                   Map<String, BigDecimal> predaje,
                                   String nadpis,
                                   Firma dodavatel) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy ' ' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());



        if ((velkosklady == null) || (produkty == null) || (predaje == null))
            return null;
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
        HSSFRow row;
        HSSFCell cel;
        row = sheet.createRow(0);
        cel = row.createCell(0);
        cel.setCellValue("Vytvorené:" + formatter.format(date));

        row = sheet.createRow(1);
        cel = row.createCell(2);
        cel.setCellStyle(nadpisovyformat());
        cel.setCellValue(nadpis);

        row = sheet.createRow(2);

        cel = row.createCell(2);
        cel.setCellStyle(nadpisovyformat());
        cel.setCellValue(dodavatel.getIco()+" "+ dodavatel.getNazov());






        int rowNum = 4;
        HSSFCell  bunka;


        int colNum = 1;
        row = sheet.createRow(rowNum);
        row.setHeight((short)-1);
        row.setHeight((short)700);
        cel = row.createCell(1);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("Názov produktu"));
        cel = row.createCell(2);
        cel.setCellStyle(stylBunky);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("Body/za MN"));

        for (Firma f : velkosklady) {
            cel = row.createCell(2+colNum++);

            cel.setCellStyle(oramovanieBoldZalomenie());
            cel.setCellValue((String) f.getNazov());

            //cel.setCellValue((String) f.getNazov());


        }
        cel = row.createCell(2+colNum);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue("Spolu MN");

        cel = row.createCell(3+colNum);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue("Spolu body");

        cel = row.createCell(4+colNum);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue("Spolu Eur");

        int sumaRiadkuMN=1;
        int sumaRiadkuBody=0;
        BigDecimal sumaRiadkuEur;
        int sumaCelkomMn=0;
        int sumaCelkomBody=0;
        int mnozstvo=0;
        BigDecimal sumaCelkomEur=BigDecimal.ZERO;
        for (Produkt p : produkty) {

            if (sumaRiadkuMN!=0) {
                rowNum++;
            }
            sumaRiadkuMN=0;
            sumaRiadkuBody=0;

            sumaRiadkuEur=BigDecimal.ZERO;


            row = sheet.createRow(rowNum);
            colNum = 1;
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue((String) p.getNazov());
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue(p.getBody().toString()+"/"+p.getKusy().toString());
            for (Firma f : velkosklady) {
                String kluc=f.getIco()+"*"+p.getKat();
                bunka = row.createCell(colNum++);
                bunka.setCellStyle(oramovane());
//                bunka=nastavFormatBunky(bunka,true,false);
                bunka.setCellValue((predaje.get(kluc) == null) ? 0 : predaje.get(kluc).doubleValue());
                mnozstvo=((predaje.get(kluc) == null) ? 0 : predaje.get(kluc).intValue());
                sumaRiadkuMN = sumaRiadkuMN+mnozstvo;
                sumaRiadkuBody = sumaRiadkuBody+(mnozstvo*p.getBody().intValue());
                sumaRiadkuEur = sumaRiadkuEur.add((predaje.get(kluc) == null) ?
                        (new BigDecimal(0)) :
                        (new BigDecimal(mnozstvo*p.getBody().intValue()))
                                .multiply(new BigDecimal(0.01))
                                .setScale(2, RoundingMode.HALF_UP));


            }
            sumaCelkomMn = sumaCelkomMn+sumaRiadkuMN;
            sumaCelkomBody = sumaCelkomBody+sumaRiadkuBody;
            sumaCelkomEur = sumaCelkomEur.add(sumaRiadkuEur);
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue(sumaRiadkuMN);

            bunka = row.createCell(colNum);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue(sumaRiadkuBody);

            bunka = row.createCell(colNum+1);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue(sumaRiadkuEur.toString());

        }
        rowNum++;

        //inicializacia zaverecneho riadku
        cel = row.createCell(rowNum);
        cel = row.createCell(0);
        cel.setCellValue("");
        cel = row.createCell(1);
        cel.setCellValue("");
        cel = row.createCell(2);
        cel.setCellValue("");

        colNum=1;
        for (Firma f : velkosklady) {
            cel = row.createCell(2 + colNum++);
            cel.setCellValue("");
        }
        bunka = row.createCell(1);
        bunka.setCellStyle(oramovanieBold());
        bunka.setCellValue("Spolu");

        bunka = row.createCell(colNum+2);
        bunka.setCellStyle(oramovanieBold());
        bunka.setCellValue(sumaCelkomMn);


        bunka = row.createCell(colNum+3);
        bunka.setCellStyle(oramovanieBold());
        bunka.setCellValue(sumaCelkomBody);

        bunka = row.createCell(colNum+4);
        bunka.setCellStyle(oramovanieBold());
        bunka.setCellValue(sumaCelkomEur.toString());



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

        return null;
    }

    private static Cell nastavFormatBunky(Cell bunka, boolean oramovat,boolean hrubym) {
        CellStyle mojformat=null;

        if (oramovat){
            mojformat.setBorderBottom(BorderStyle.THIN);
            mojformat.setBorderLeft(BorderStyle.THIN);
            mojformat.setBorderRight(BorderStyle.THIN);
            mojformat.setBorderTop(BorderStyle.THIN);
        }
        if (hrubym){
            Font font = null;
            font.setBold(true);
            mojformat.setFont(font);
        }

        bunka.setCellStyle(mojformat);
        return bunka;
    }

    private static HSSFCellStyle nadpisovyformat(){
        HSSFCellStyle cs;
        HSSFFont font = workbook.createFont();
        cs = workbook.createCellStyle();
        font.setBold(true);
        cs.setBorderRight(BorderStyle.MEDIUM);
        cs.setFont(font);
        return cs;
    }
    private static HSSFCellStyle oramovanieBold(){
        HSSFCellStyle cs;
        HSSFFont font = workbook.createFont();
        cs = workbook.createCellStyle();
        font.setBold(true);
        cs.setBorderRight(BorderStyle.MEDIUM);
        cs.setFont(font);
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);

        return cs;
    }

    private static HSSFCellStyle oramovanieBoldZalomenie(){
        HSSFCellStyle cs;
        HSSFFont font = workbook.createFont();
        cs = workbook.createCellStyle();
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

    private static HSSFCellStyle oramovane(){
        HSSFCellStyle cs;
        HSSFFont font = workbook.createFont();
        cs = workbook.createCellStyle();
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        return cs;
    }
}

