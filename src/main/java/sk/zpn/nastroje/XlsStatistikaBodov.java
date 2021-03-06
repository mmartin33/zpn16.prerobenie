
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


    private static final String FILE_NAME_SABLONY = SystemoveParametre.getResourcesAdresar() + "statistikapoberatelov.xls";
    private static String FILE_NAME = "statistikapoberatelovVysledok.xls";
    private static final String FILE_NAME_SABLONY_BILANCIE = SystemoveParametre.getResourcesAdresar() + "bilancia.xls";
    private static String FILE_NAME_BILANCIE = "bilancia.xls";
    private static HSSFWorkbook workbook;
    private static HSSFCellStyle cs;
    private static HSSFFont font;


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
                                  Map<String, String> icaFiriem,
                                  Map<String, String> prevPopis,
                                  Map<String, String> poberatelPopis,
                                  Map<String, String> icaFiriemZPob,
                                  Map<String, String> prevPopisZPob,
                                  Map<String, String> poberatelPopisZPob,
                                  Map<String, String> poberateliaAVelkosklady,
                                  Map<String, Double> poberateliaPodlaPohybov,
                                  String typPoberatela) {


        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy ' ' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());


        if ((poberatelia == null) || (bodyZaPredaj == null) || (pociatocnyStav == null) || (bodyIne == null) || (konecnyStav == null))
            return;
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
        cel.setCellValue("Vytvoren??:" + formatter.format(date));

        row = sheet.createRow(1);
        cel = row.createCell(1);
        cel.setCellStyle(nadpisovyformat());
        cel.setCellValue(nadpis);


        cel = row.createCell(3);
        cel.setCellStyle(nadpisovyformat());
        cel.setCellValue(typPoberatela);


        int rowNum = 2;
        HSSFCell bunka;

        int colNum = 1;
        row = sheet.createRow(rowNum);
        row.setHeight((short) -1);
        row.setHeight((short) 700);

        colNum = 1;

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("Ve??kosklad"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("Poberate??"));


        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("I??O+firma"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("prev??dzka+obec"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("poberate?? - obec"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("prihlasovac?? \r\n k??d"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("prihlasovac?? \r\n email"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("Heslo"));


        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("po??iato??n?? \r\n stav"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("body za \r\n predaj"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("in??"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("odpo??ty \n bodov za \n odmeny"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("prevody \n bodov"));


        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("body za \n registraciu"));


        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("Kone??n?? \r\n stav"));


        Poberatel poberatel = null;
        BigDecimal riadkovaHodnota = BigDecimal.ONE;
        for (Poberatel p : poberatelia) {
            colNum = 1;
            String kluc = p.getId().toString();
            if (UzivatelNastroje.getPrihlasenehoUzivatela().getTypUzivatela() == TypUzivatela.PREDAJCA)
                if (poberateliaVelkoskladu.get(kluc) == null)
                    continue;
            //cel.setCellStyle(oramovanieBoldZalomenie());

            //overenie ktorych poslat "V??etk??ch", "Mali pohyb v novom syst??me", "Nemali pohyb v novom syst??me");
            if (typPoberatela.equals("Mali pohyb v novom syst??me")){
                if (poberateliaPodlaPohybov.get(kluc) == null)
                    continue;
            }
                else if (typPoberatela.equals("Nemali pohyb v novom syst??me")){
                if (poberateliaPodlaPohybov.get(kluc) != null)
                    continue;

            }
            if (!riadkovaHodnota.equals(BigDecimal.ZERO))
                rowNum++;

            row = sheet.createRow(rowNum);


            String textBunky = "";

            textBunky = (((poberateliaAVelkosklady.get(kluc) == null) ? "" : poberateliaAVelkosklady.get(kluc))).toString();
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue(textBunky);


            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue((String) p.getMeno());


            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            textBunky = (((icaFiriem.get(kluc) == null) ? "" : icaFiriem.get(kluc))).toString();
            textBunky = ((textBunky.length() > 0) ?
                    textBunky :
                    (icaFiriemZPob.get(kluc) == null) ? "" : icaFiriemZPob.get(kluc).toString());
            bunka.setCellValue(textBunky);

            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());

            textBunky = "";
            textBunky = (((prevPopis.get(kluc) == null) ? "" : prevPopis.get(kluc))).toString();
            textBunky = ((textBunky.length() > 0) ?
                    textBunky :
                    (((prevPopisZPob.get(kluc) == null) ? "" : prevPopisZPob.get(kluc))).toString());
            bunka.setCellValue(textBunky);

            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            textBunky = "";
            textBunky = (((poberatelPopis.get(kluc) == null) ? "" : poberatelPopis.get(kluc))).toString();
            textBunky = ((textBunky.length() > 0) ?
                    textBunky :
                    (((poberatelPopisZPob.get(kluc) == null) ? "" : poberatelPopisZPob.get(kluc))).toString());

            bunka.setCellValue(textBunky);


            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue((String) p.getKod());

            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue((String) p.getEmail());

            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovanieBold());
            bunka.setCellValue((String) p.getHeslo());

            riadkovaHodnota = BigDecimal.ZERO;

            Double hodnotaD = Double.valueOf(0);


            //PS
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovane());
            hodnotaD = (((pociatocnyStav.get(kluc) == null) ? Double.valueOf(0) : pociatocnyStav.get(kluc)));
            bunka.setCellValue(hodnotaD);
            riadkovaHodnota = riadkovaHodnota.add(BigDecimal.valueOf(hodnotaD));

            //predaj
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovane());
            hodnotaD = (((bodyZaPredaj.get(kluc) == null) ? Double.valueOf(0) : bodyZaPredaj.get(kluc)));
            bunka.setCellValue(hodnotaD);
            riadkovaHodnota = riadkovaHodnota.add(BigDecimal.valueOf(hodnotaD));

            //ine
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovane());
            hodnotaD = (((bodyIne.get(kluc) == null) ? Double.valueOf(0) : bodyIne.get(kluc)));
            bunka.setCellValue(hodnotaD);
            riadkovaHodnota = riadkovaHodnota.add(BigDecimal.valueOf(hodnotaD));

            //odmeny
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovane());
            hodnotaD = (((bodyOdmeny.get(kluc) == null) ? Double.valueOf(0) : bodyOdmeny.get(kluc)));
            bunka.setCellValue(hodnotaD);
            riadkovaHodnota = riadkovaHodnota.add(BigDecimal.valueOf(hodnotaD));

            //prevod
            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovane());
            hodnotaD = (((bodyPrevod.get(kluc) == null) ? Double.valueOf(0) : bodyPrevod.get(kluc)));
            bunka.setCellValue(hodnotaD);
            riadkovaHodnota = riadkovaHodnota.add(BigDecimal.valueOf(hodnotaD));

            //registracia


            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovane());
            hodnotaD = (((bodyRegistracia.get(kluc) == null) ? Double.valueOf(0) : bodyRegistracia.get(kluc)));
            bunka.setCellValue(hodnotaD);
            riadkovaHodnota = riadkovaHodnota.add(BigDecimal.valueOf(hodnotaD));


            bunka = row.createCell(colNum++);
            bunka.setCellStyle(oramovane());
            hodnotaD = (((konecnyStav.get(kluc) == null) ? Double.valueOf(0) : konecnyStav.get(kluc)));
            bunka.setCellValue(hodnotaD);
            riadkovaHodnota = riadkovaHodnota.add(BigDecimal.valueOf(hodnotaD));


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


    public static void vytvorXLSBilancie(List<Poberatel> poberatelia,
                                         Map<String, Double> pociatocnyStav,
                                         Map<String, Double> bodyZaPredaj,
                                         Map<String, Double> bodyIne,
                                         Map<String, Double> konecnyStav,
                                         String nadpis,
                                         Map<String, Double> poberateliaVelkoskladu,
                                         Map<String, Double> bodyRegistracia,
                                         Map<String, Double> bodyOdmeny,
                                         Map<String, Double> bodyPrevod) {


        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy ' ' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());


        if ((poberatelia == null) || (bodyZaPredaj == null) || (pociatocnyStav == null) || (bodyIne == null) || (konecnyStav == null))
            return;
        workbook = null;
        FileInputStream excelFile = null;
        try {
            excelFile = new FileInputStream(new File(FILE_NAME_SABLONY_BILANCIE));

            workbook = new HSSFWorkbook(excelFile);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = workbook.getSheet("bilancia");
        cs = workbook.createCellStyle();
        font = workbook.createFont();
        HSSFRow row;
        HSSFCell cel;
        row = sheet.createRow(0);
        cel = row.createCell(0);
        cel.setCellValue("Vytvoren??:" + formatter.format(date));

        row = sheet.createRow(1);
        cel = row.createCell(1);
        cel.setCellStyle(nadpisovyformat());
        cel.setCellValue(nadpis);

        int rowNum = 2;
        HSSFCell bunka;

        int colNum = 1;
        row = sheet.createRow(rowNum);
        row.setHeight((short) -1);
        row.setHeight((short) 700);

        colNum = 1;

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("po??iato??n?? \r\n stav"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("body za \r\n predaj"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("in??"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("odpo??ty \n bodov za \n odmeny"));

        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("prevody \n bodov"));


        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("body za \n registraciu"));


        cel = row.createCell(colNum++);
        cel.setCellStyle(oramovanieBold());
        cel.setCellValue(("Kone??n?? \r\n stav"));


        Poberatel poberatel = null;
        BigDecimal sumaPociatocnyStav = BigDecimal.ZERO;
        BigDecimal sumaKonecnyStav = BigDecimal.ZERO;
        BigDecimal sumaPrevodu = BigDecimal.ZERO;
        BigDecimal sumaZaPredaj = BigDecimal.ZERO;
        BigDecimal sumaZaOdmeny = BigDecimal.ZERO;
        BigDecimal sumaIna = BigDecimal.ZERO;
        BigDecimal sumaZaRegistraciu = BigDecimal.ZERO;
        Double hodnotaD = Double.valueOf(0);

        for (Poberatel p : poberatelia) {

            String kluc = p.getId().toString();
            if (UzivatelNastroje.getPrihlasenehoUzivatela().getTypUzivatela() == TypUzivatela.PREDAJCA)
                if (poberateliaVelkoskladu.get(kluc) == null)
                    continue;


            hodnotaD = (((pociatocnyStav.get(kluc) == null) ? Double.valueOf(0) : pociatocnyStav.get(kluc)));
            sumaPociatocnyStav=sumaPociatocnyStav.add(BigDecimal.valueOf(hodnotaD));


            hodnotaD = (((bodyZaPredaj.get(kluc) == null) ? Double.valueOf(0) : bodyZaPredaj.get(kluc)));
            sumaZaPredaj=sumaZaPredaj.add(BigDecimal.valueOf(hodnotaD));


            hodnotaD = (((bodyIne.get(kluc) == null) ? Double.valueOf(0) : bodyIne.get(kluc)));
            sumaIna=sumaIna.add(BigDecimal.valueOf(hodnotaD));


            hodnotaD = (((bodyOdmeny.get(kluc) == null) ? Double.valueOf(0) : bodyOdmeny.get(kluc)));
            sumaZaOdmeny=sumaZaOdmeny.add(BigDecimal.valueOf(hodnotaD));

            hodnotaD = (((bodyPrevod.get(kluc) == null) ? Double.valueOf(0) : bodyPrevod.get(kluc)));
            sumaPrevodu=sumaPrevodu.add(BigDecimal.valueOf(hodnotaD));

            hodnotaD = (((bodyRegistracia.get(kluc) == null) ? Double.valueOf(0) : bodyRegistracia.get(kluc)));
            sumaZaRegistraciu=sumaZaRegistraciu.add(BigDecimal.valueOf(hodnotaD));

            hodnotaD = (((konecnyStav.get(kluc) == null) ? Double.valueOf(0) : konecnyStav.get(kluc)));
            sumaKonecnyStav=sumaKonecnyStav.add(BigDecimal.valueOf(hodnotaD));


        }
        rowNum = 3;
        row = sheet.createRow(rowNum);
        colNum = 1;
        //PS
        bunka = row.createCell(colNum++);
        bunka.setCellStyle(oramovane());
        bunka.setCellValue(sumaPociatocnyStav.intValue());

        //predaj
        bunka = row.createCell(colNum++);
        bunka.setCellStyle(oramovane());
        bunka.setCellValue(sumaZaPredaj.intValue());


        //ine
        bunka = row.createCell(colNum++);
        bunka.setCellStyle(oramovane());
        bunka.setCellValue(sumaIna.intValue());


        //odmeny
        bunka = row.createCell(colNum++);
        bunka.setCellStyle(oramovane());
        bunka.setCellValue(sumaZaOdmeny.intValue());


        //prevod
        bunka = row.createCell(colNum++);
        bunka.setCellStyle(oramovane());
        bunka.setCellValue(sumaPrevodu.intValue());


        //registracia
        bunka = row.createCell(colNum++);
        bunka.setCellStyle(oramovane());
        bunka.setCellValue(sumaZaRegistraciu.intValue());


        //konecny stav
        bunka = row.createCell(colNum++);
        bunka.setCellStyle(oramovane());
        bunka.setCellValue(sumaKonecnyStav.intValue());


        try {

            FileOutputStream outputStream = new FileOutputStream(SystemoveParametre.getTmpAdresar() + FILE_NAME_BILANCIE);
            workbook.write(outputStream);
            workbook.close();
            excelFile.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        String filePath = SystemoveParametre.getTmpAdresar() + FILE_NAME_BILANCIE;

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

