package sk.zpn.nastroje;


import com.vaadin.server.Sizeable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sk.zpn.SystemoveParametre;
import sk.zpn.domena.Poberatel;
import sk.zpn.domena.StatistikaBodov;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.zaklad.model.UzivatelNastroje;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class XlsStatistikaBodov {


    private static final String FILE_NAME_SABLONY =  SystemoveParametre.getResourcesAdresar()+"statistikapoberatelov.xls";
    private static String FILE_NAME = "statistikapoberatelovVysledok.xls";
    private static HSSFWorkbook workbook;
    private static HSSFCellStyle cs;
    private static HSSFFont font ;


    public static void vytvorXLS2(List<Poberatel> poberatelia,
                                  Map<String, Double> pociatocnyStav,
                                  Map<String, Double> bodyZaPredaj,
                                  Map<String, Double> bodyIne,
                                  Map<String, Double> konecnyStav,
                                  String nadpis,
                                  Map<String, Double> poberateliaVelkoskladu,
                                  Map<String, Double> bodyRegistracia,
                                  Map<String, Double> bodyOdmeny,
                                  Map<String, Double> bodyPrevod,
                                  Map<String, String> icaFiriem) {


        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy ' ' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());



        if ((poberatelia == null) || (bodyZaPredaj== null) || (pociatocnyStav== null) || (bodyIne == null) || (konecnyStav == null))
            return ;
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

        int rowNum = 2;
        HSSFCell  bunka;

        int colNum = 1;
        row = sheet.createRow(rowNum);
        row.setHeight((short)-1);
        row.setHeight((short)700);

        colNum=1;
        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("Poberateľ"));


        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("IČO+názov"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("prihlasovací \r\n kód"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("prihlasovací \r\n email"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("Heslo"));


        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("počiatočný \r\n stav"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("body za \r\n predaj"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("iné"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("odpočty \n bodov za \n odmeny"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("prevody \n bodov"));


        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("body za \n registraciu"));


        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("Konečný \r\n stav"));


        Poberatel poberatel=null;
        BigDecimal riadkovaHodnota=BigDecimal.ONE;
        for (Poberatel p : poberatelia) {
            colNum = 1;
            String kluc=p.getId().toString();
            if (UzivatelNastroje.getPrihlasenehoUzivatela().getTypUzivatela() == TypUzivatela.PREDAJCA)
                if (poberateliaVelkoskladu.get(kluc)==null)
                    continue;
            //cel.setCellStyle(oramovanieBoldZalomenie());


            if (!riadkovaHodnota.equals(BigDecimal.ZERO))
                rowNum++;

            row = sheet.createRow(rowNum);


            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue((String) p.getMeno());


            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());

            bunka.setCellValue((((icaFiriem.get(kluc) == null) ? "": icaFiriem.get(kluc))).toString());




            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue((String) p.getKod());

            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue((String) p.getEmail());

            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue((String) p.getHeslo());

            riadkovaHodnota= BigDecimal.ZERO;

            Double hodnotaD= Double.valueOf(0);


            //PS
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovane());
            hodnotaD=(((pociatocnyStav.get(kluc) == null) ? Double.valueOf(0): pociatocnyStav.get(kluc)));
            bunka.setCellValue(hodnotaD);
            riadkovaHodnota=riadkovaHodnota.add(BigDecimal.valueOf(hodnotaD));

            //predaj
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovane());
            hodnotaD=(((bodyZaPredaj.get(kluc) == null) ? Double.valueOf(0) : bodyZaPredaj.get(kluc)));
            bunka.setCellValue(hodnotaD);
            riadkovaHodnota=riadkovaHodnota.add(BigDecimal.valueOf(hodnotaD));

            //ine
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovane());
            hodnotaD=(((bodyIne.get(kluc) == null) ? Double.valueOf(0) : bodyIne.get(kluc)));
            bunka.setCellValue(hodnotaD);
            riadkovaHodnota=riadkovaHodnota.add(BigDecimal.valueOf(hodnotaD));

            //odmeny
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovane());
            hodnotaD=(((bodyOdmeny.get(kluc) == null) ? Double.valueOf(0) : bodyOdmeny.get(kluc)));
            bunka.setCellValue(hodnotaD);
            riadkovaHodnota=riadkovaHodnota.add(BigDecimal.valueOf(hodnotaD));

            //prevod
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovane());
            hodnotaD=(((bodyPrevod.get(kluc) == null) ? Double.valueOf(0) : bodyPrevod.get(kluc)));
            bunka.setCellValue(hodnotaD);
            riadkovaHodnota=riadkovaHodnota.add(BigDecimal.valueOf(hodnotaD));

            //registracia


            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovane());
            hodnotaD=(((bodyRegistracia.get(kluc) == null) ? Double.valueOf(0) : bodyRegistracia.get(kluc)));
            bunka.setCellValue(hodnotaD);
            riadkovaHodnota=riadkovaHodnota.add(BigDecimal.valueOf(hodnotaD));



            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovane());
            hodnotaD=(((konecnyStav.get(kluc) == null) ? Double.valueOf(0) : konecnyStav.get(kluc)));
            bunka.setCellValue(hodnotaD);
            riadkovaHodnota=riadkovaHodnota.add(BigDecimal.valueOf(hodnotaD));


        }



        rowNum++;



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

        return ;


    }



    public static String vytvorXLS(List<StatistikaBodov> zoznam, String nadpis) {
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

        for (StatistikaBodov z : zoznam) {
            row = sheet.createRow(rowNum++);
            int colNum = 1;
            cel1 = row.createCell(colNum++);
            cel1.setCellValue((String) z.getNazov());
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

    private static HSSFCellStyle nadpisovyformat(){

        font.setBold(true);
        cs.setBorderRight(BorderStyle.MEDIUM);
        cs.setFont(font);
        return cs;
    }
    private static HSSFCellStyle oramovanieBold(){

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


        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        return cs;
    }


}

