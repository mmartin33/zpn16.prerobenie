package sk.zpn.zaklad.view.nacitanieCSV;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


import com.google.common.collect.Maps;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import org.apache.commons.lang.StringUtils;
import sk.zpn.SystemoveParametre;
import sk.zpn.domena.importy.Davka;
import sk.zpn.domena.importy.ParametreImportu;
import sk.zpn.domena.importy.VysledokImportu;
import sk.zpn.zaklad.grafickeNastroje.ProgressBarZPN;
import sk.zpn.zaklad.model.FirmaProduktNastroje;
import sk.zpn.zaklad.model.ParametreNastroje;
import sk.zpn.zaklad.model.util.importeryDavky.DavkaCsvImporter;
import sk.zpn.zaklad.model.util.importeryDavky.DavkaDbfImporter;
import sk.zpn.zaklad.model.DokladyNastroje;
import sk.zpn.zaklad.model.util.importeryDavky.DavkaTxtImporter;


public class UploadCSV extends CustomComponent  {
    private static final long serialVersionUID = -4292553844521293140L;
    public ParametreImportu parametreImportu=null;
    Upload upload;
    Label label;
    Button btnSpat;
    Panel panel;
    ProgressBarZPN progressBarZPN;


    VerticalLayout layout;
    String adresar= SystemoveParametre.getUplodAdresar()  ;
    VysledokImportu vysledokSpracovania;

    private NacitanieCSVView nacitanieView;


    public UploadCSV(NacitanieCSVView nacitanieCSVView) {
        this.nacitanieView=nacitanieCSVView;
    }

    public void init() {
        class FileReceiver implements Receiver, SucceededListener {
            private static final long serialVersionUID = -1276759102490466761L;

            public File file;


            public OutputStream receiveUpload(String filename,
                                              String mimeType) {
                // Create upload stream
                FileOutputStream fos = null; // Stream to write to
                try {
                    // Open the file for writing.
                    //file = new File("//d:/tmp/uploads/" + filename);
                    file = new File(adresar + filename);
                    fos = new FileOutputStream(file);
                } catch (final java.io.FileNotFoundException e) {
                    new Notification("S??bor sa ned?? otvori?? ",
                            e.getMessage(),
                            Notification.Type.ERROR_MESSAGE)
                            .show(Page.getCurrent());
                    return null;
                }
                return fos; // Return the output stream to write to
            }
            public void uploadSucceeded(SucceededEvent event) {
                upload.setVisible(false);
                zhrajDavku(file.toString());
                zobrazDavku();

//                image.setVisible(true);
//                image.setSource(new FileResource(file));
            }
        };
        layout = new VerticalLayout();
        FileReceiver receiver = new FileReceiver();
        progressBarZPN= new ProgressBarZPN("");


//        String ico= UzivatelNastroje.getIcoVlastnejFirmyPrihlasenehoUzivala();




        // Create the upload with a caption and set receiver later
        upload = new Upload("V??berte s??bor na odoslanie, Po v??bere sa automaticky spust?? zhranie", receiver);
        upload.setButtonCaption("V??ber s??boru");
        upload.addSucceededListener(receiver);

        // Prevent too big downloads
        final long UPLOAD_LIMIT = 10000000000l;
        upload.addStartedListener(new StartedListener() {
            private static final long serialVersionUID = 4728847902678459488L;

            @Override
            public void uploadStarted(StartedEvent event) {
                if (event.getContentLength() > UPLOAD_LIMIT) {
                    Notification.show("S??bor je pr??li?? ve??k??",
                            Notification.Type.ERROR_MESSAGE);
                    upload.interruptUpload();
                }
            }
        });

        // Check the size also during progress
        upload.addProgressListener(new ProgressListener() {
            private static final long serialVersionUID = 8587352676703174995L;

            @Override
            public void updateProgress(long readBytes, long contentLength) {
                if (readBytes > UPLOAD_LIMIT) {
                    Notification.show("S??bor je pr??li?? ve??k??",
                            Notification.Type.ERROR_MESSAGE);
                    upload.interruptUpload();
                }
            }
        });

        // Put the components in a panel
        panel = new Panel("Odoslanie d??vky");
        VerticalLayout panelContent = new VerticalLayout();
        panelContent.setMargin(true);
        panelContent.addComponents(upload);
        panel.setContent(panelContent);
        // END-EXAMPLE: component.upload.basic

        // Create uploads directory
//        File uploads = new File("/tmp/uploads");
        File uploads = new File(adresar);
        if (!uploads.exists() && !uploads.mkdir())
            layout.addComponent(new Label("ERROR: Could not create upload dir"));

        ((VerticalLayout) panel.getContent()).setSpacing(true);
        panel.setWidth("-1");
        layout.addComponent(panel);
        layout.addComponents(progressBarZPN);



        setCompositionRoot(layout);
    }

    void zobrazDavku() {
        panel.setVisible(false);
        label= new Label("Subor ??spe??ne odoslan??");
        layout.addComponentsAndExpand(label);
    }

    void zhrajDavku(String file){
        Davka davka = null;

        try {
            if (StringUtils.upperCase(StringUtils.right(file,3)).equals("DBF"))
                davka= DavkaDbfImporter.nacitajDbfDavku(file,parametreImportu,progressBarZPN);
            else if (StringUtils.upperCase(StringUtils.right(file,3)).equals("TXT"))
                if (parametreImportu.getFirma().getIco().equals("30997666")) //spodos format
                    davka= DavkaCsvImporter.nacitajCsvSpodosDavku(file,parametreImportu,progressBarZPN);
                else
                    davka= DavkaTxtImporter.nacitajTxtDavku(file,parametreImportu,progressBarZPN);

            else
                if (parametreImportu.getFirma().getIco().equals("51945436")) //becica format
                    davka= DavkaCsvImporter.nacitajCsvDavkuBecica(file,parametreImportu,progressBarZPN);
                else if (parametreImportu.getFirma().getIco().equals("10952799")) //simo format
                {

                    DavkaCsvImporter dCsv=new DavkaCsvImporter();
                    //threed//

                    //dCsv.nastavParametre(DavkaCsvImporter.SPUSTIT_DAVKU_SIMO,file,parametreImportu,progressBarZPN,davka);
                    //dCsv.start();
                    //povodne//
                    davka= dCsv.nacitajCsvDavkuSimo(file,parametreImportu,progressBarZPN);
                }

                else
                    davka= DavkaCsvImporter.nacitajCsvDavku(file,parametreImportu,progressBarZPN);

            //this.setVysledokSpracovania(DokladyNastroje.zalozDokladovuDavku(zaznam));
            System.out.println("Start zalozenia dokladovej davky");
            VysledokImportu vi=DokladyNastroje.zalozDokladovuDavku(davka,file,parametreImportu,progressBarZPN);
            System.out.println("Koniec zalozenia dokladovej davky");
            if (vi!=null) {
                nacitanieView.setVysledokImportu(vi);
                System.out.println(davka.getPolozky().size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void nastavAdresar(String ico){
        this.adresar=this.adresar+ico+"/";
    }
    public void setVysledokSpracovania(VysledokImportu vysledokSpracovania) {
        this.vysledokSpracovania = vysledokSpracovania;
    }
}
