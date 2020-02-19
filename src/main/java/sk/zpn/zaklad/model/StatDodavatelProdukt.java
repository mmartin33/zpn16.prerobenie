package sk.zpn.zaklad.model;

import com.google.common.collect.Maps;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import sk.zpn.SystemoveParametre;
import sk.zpn.domena.Firma;
import sk.zpn.domena.Produkt;
import sk.zpn.domena.statistiky.Zaznam;
import sk.zpn.nastroje.NastrojePoli;
import sk.zpn.nastroje.SaveToExcelLink;
import sk.zpn.nastroje.XlsStatistikaBodovDodavatelProdukt;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class StatDodavatelProdukt {


    public static void load(boolean bodovyRezim, LocalDate dod, LocalDate ddo, int rok, Firma dodavatel) {
        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);


        Map<String, BigDecimal> predaje = Maps.newHashMap();
        if (dodavatel!=null) {
            if (bodovyRezim)
                predaje = vratPredajeBodov(dod, ddo, rok, dodavatel);
            else
                predaje = vratPredaje(dod, ddo, rok, dodavatel);
            List<Firma> velkosklady = FirmaNastroje.zoznamFiriemIbaVelkosklady();
            List<Produkt> produkty = ProduktyNastroje.zoznamProduktovZaRokZaDodavatela(new Integer(rok).toString(), dodavatel);
            String nadpis = "Vyhodnotenie dodavateľa  od: " + simpleDateFormat.format(Date.valueOf(dod)) + " dp: " + simpleDateFormat.format(Date.valueOf(ddo));
            if (bodovyRezim)
                XlsStatistikaBodovDodavatelProdukt.vytvorXLSbodovy(velkosklady, produkty, predaje, nadpis, dodavatel, false);
            else
                XlsStatistikaBodovDodavatelProdukt.vytvorXLS(velkosklady, produkty, predaje, nadpis, dodavatel);
            return;
        }else{
            String zipFile = SystemoveParametre.getTmpAdresar()+"StatistikaBodovVelkoskladAProdukty.zip";
            ZipOutputStream zos=null;
            List <Firma> dodavatelia;
            dodavatelia=FirmaNastroje.zoznamFiriemIbaDodavatelia();
            String xlsFile;
            FileOutputStream zipOupuStream = null;

            try {
                zipOupuStream = new FileOutputStream(zipFile);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            zos = new ZipOutputStream(zipOupuStream);


            for (Firma f : dodavatelia){

                predaje = vratPredajeBodov(dod, ddo, rok, f);
                List<Firma> velkosklady = FirmaNastroje.zoznamFiriemIbaVelkosklady();
                List<Produkt> produkty = ProduktyNastroje.zoznamProduktovZaRokZaDodavatela(new Integer(rok).toString(), f);
                String nadpis = "Vyhodnotenie dodavateľa  od: " + simpleDateFormat.format(Date.valueOf(dod)) + " dp: " + simpleDateFormat.format(Date.valueOf(ddo));
                xlsFile = XlsStatistikaBodovDodavatelProdukt.vytvorXLSbodovy(velkosklady, produkty, predaje, nadpis, f, true);




                try {

                    // create byte buffer
                    byte[] buffer = new byte[1024];
                    FileInputStream fis = new FileInputStream(xlsFile);
                    zos.putNextEntry(new ZipEntry(xlsFile.substring(xlsFile.lastIndexOf("/")+1)));
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }
                    zos.closeEntry();
                        // close the InputStream
                    fis.close();

                    // close the ZipOutputStream


                }
                catch (IOException ioe) {
                    System.out.println("Error creating zip file: " + ioe);
                }

            }
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stiahniZipFile(zipFile);
        }
    }

    private static void stiahniZipFile(String filePath) {




        Window subWindow = new Window("");
        subWindow.setWidth(500, Sizeable.Unit.PIXELS);
        subWindow.setHeight(400, Sizeable.Unit.PIXELS);

        SaveToExcelLink s = new SaveToExcelLink(filePath);
        VerticalLayout vl = new VerticalLayout();
        vl.addComponentsAndExpand(s);
        subWindow.setContent(vl);
        subWindow.setModal(true);
        UI.getCurrent().addWindow(subWindow);

    }





    private static Map<String, BigDecimal> vratPredaje(LocalDate dod, LocalDate ddo, int rok, Firma dodavatel) {
        Map<String, BigDecimal> predaje =Maps.newHashMap();
        List<Zaznam> result1=null;
        EntityManager em1;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em1 = emf.createEntityManager();
        String sql="select f.ico||'*'||prod.kat as kluc,sum(p.mnozstvo) as hodnota from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id " +
                "    join firmy as f on f.id=d.firma_id " +
                "    join produkty as prod on prod.id=p.produkt_id " +
                "    join firmy as dod on dod.id=prod.firma_id " +
                "    where " +
                "    dod.id="+dodavatel.getId()+
                "    and d.stavdokladu='POTVRDENY' " +
                "    and (d.typdokladu='DAVKA' or d.typdokladu='INTERNY_DOKLAD')" +
                "    and d.datum>=Date('" + dod + "') and d.datum<=Date('" + ddo + "') " +
                "    group by f.ico||'*'||prod.kat";


        result1= em1.createNativeQuery(sql,Zaznam.class).getResultList();

        predaje= NastrojePoli.prerobListNaMapu(result1);
        em1.close();
        emf.close();

        return predaje;
    }
    private static Map<String, BigDecimal> vratPredajeBodov(LocalDate dod, LocalDate ddo, int rok, Firma dodavatel) {
        Map<String, BigDecimal> predaje =Maps.newHashMap();
        List<Zaznam> result1=null;
        EntityManager em1;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zpn");
        em1 = emf.createEntityManager();
        String sql="select f.ico||'*'||prod.kat as kluc,sum(p.body) as hodnota from polozkydokladu as p " +
                "join doklady as d on d.id=p.doklad_id " +
                "    join firmy as f on f.id=d.firma_id " +
                "    join produkty as prod on prod.id=p.produkt_id " +
                "    join firmy as dod on dod.id=prod.firma_id " +
                "    where " +
                "    dod.id="+dodavatel.getId()+
                "    and d.stavdokladu='POTVRDENY' " +
                "    and (d.typdokladu='DAVKA' or d.typdokladu='INTERNY_DOKLAD')" +
                "    and d.datum>=Date('" + dod + "') and d.datum<=Date('" + ddo + "') " +
                "    group by f.ico||'*'||prod.kat";


        result1= em1.createNativeQuery(sql,Zaznam.class).getResultList();

        predaje= NastrojePoli.prerobListNaMapu(result1);
        em1.close();
        emf.close();

        return predaje;
    }

}
