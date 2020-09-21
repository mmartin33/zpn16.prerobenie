package sk.zpn.nastroje;


import com.vaadin.server.Sizeable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.util.ResourceUtils;
import sk.zpn.SystemoveParametre;
import sk.zpn.domena.Doklad;
import sk.zpn.domena.PolozkaDokladu;
import sk.zpn.zaklad.model.PolozkaDokladuNastroje;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class PdfTlacProtokolu {
    // name and destination of output file e.g. "report.pdf"
    private static String destFileName = SystemoveParametre.getTmpAdresar() + "preberaci_protokol.pdf";
    private static String FILE_NAME_REPORTU = SystemoveParametre.getResourcesAdresar() + "preberaci_protokol.jasper";
    private static Doklad doklad;

    public static void tlac(Doklad doklad) {
        PdfTlacProtokolu.doklad = doklad;
        System.out.println("generating jasper report...");

        // 1. compile template ".jrxml" file
        JasperReport jasperReport = null;
        try {
            jasperReport = getJasperReport();

            // 2. parameters "empty"
            Map<String, Object> parameters = getParameters();

            // 3. datasource "java object"
            JRDataSource dataSource = getDataSource();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                    parameters,
                    dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        }

        Window subWindow = new Window("");
        subWindow.setWidth(500, Sizeable.Unit.PIXELS);
        subWindow.setHeight(400, Sizeable.Unit.PIXELS);

        SaveToPdfLink s = new SaveToPdfLink(destFileName);
        VerticalLayout vl = new VerticalLayout();
        vl.addComponentsAndExpand(s);
        subWindow.setContent(vl);
        subWindow.setModal(true);
        UI.getCurrent().addWindow(subWindow);


    }

    private static JasperReport getJasperReport() throws FileNotFoundException, JRException {
        File template = ResourceUtils.getFile(FILE_NAME_REPORTU);
        //JRMxlLoader.load(new FileInputStream(new File(Reprot_Def);
        JasperReport jasReport = (JasperReport) JRLoader.loadObject(new FileInputStream(template));
        return jasReport;
    }

    private static Map<String, Object> getParameters() throws FileNotFoundException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("menoAdresa", doklad.getPoberatelMenoAdresa());
        parameters.put("kodPoberatela", doklad.getPoberatel().getKod());
        parameters.put("nazovFirmy", doklad.getFirma().getNazov());
        parameters.put("cisloDokladuOdmeny", doklad.getCisloDokladuOdmeny());
        parameters.put("datum", formatter.format(doklad.getDatum()));
        parameters.put("obrazok", new FileInputStream(SystemoveParametre.getResourcesAdresar()+"bdik.jpg"));
        parameters.put("podpis", new FileInputStream(SystemoveParametre.getResourcesAdresar()+"podpis.jpg"));
        return parameters;
    }

    private static JRDataSource getDataSource() {
        List<PolozkaDokladu> zoznamPoloziek = PolozkaDokladuNastroje.zoznamPoloziekDokladov(doklad);


        return new JRBeanCollectionDataSource(zoznamPoloziek);
    }
}
