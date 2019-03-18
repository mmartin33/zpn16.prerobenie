package sk.zpn.zaklad.view.nacitanieCSV;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import sk.zpn.domena.Doklad;
import sk.zpn.domena.ZaznamCsv;
import sk.zpn.zaklad.model.DavkaCsvImporter;
import sk.zpn.zaklad.model.DokladyNastroje;
import sk.zpn.zaklad.model.UzivatelNastroje;
import sk.zpn.zaklad.view.VitajteView;

public class UploadCSV extends CustomComponent  {
    private static final long serialVersionUID = -4292553844521293140L;
    Upload upload;
    Label label;
    Button btnSpat;
    Panel panel;
    VerticalLayout layout;
    String adresar="/c:/zpn/upload/";


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
                    new Notification("Súbor sa nedá otvoriť <br/>",
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

        String ico= UzivatelNastroje.getIcoVlastnejFirmyPrihlasenehoUzivala();
        this.adresar=this.adresar+ico+"/";


        // Create the upload with a caption and set receiver later
        upload = new Upload("Výberte dávku na odoslanie", receiver);
        upload.setButtonCaption("Štart odosielania");
        upload.addSucceededListener(receiver);

        // Prevent too big downloads
        final long UPLOAD_LIMIT = 10000000l;
        upload.addStartedListener(new StartedListener() {
            private static final long serialVersionUID = 4728847902678459488L;

            @Override
            public void uploadStarted(StartedEvent event) {
                if (event.getContentLength() > UPLOAD_LIMIT) {
                    Notification.show("Súbor je príliš veľký",
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
                    Notification.show("Súbor je príliš veľký",
                            Notification.Type.ERROR_MESSAGE);
                    upload.interruptUpload();
                }
            }
        });

        // Put the components in a panel
        panel = new Panel("Odoslanie dávky");
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
        setCompositionRoot(layout);
    }

    void zobrazDavku() {
        panel.setVisible(false);
        label= new Label("Subor úspešne odoslaný");
        //zhrajDavku();
        btnSpat= new Button("Späť",VaadinIcons.ARROW_BACKWARD);

        btnSpat.addClickListener(clickEvent ->
                {
                    UI.getCurrent().getNavigator().navigateTo(VitajteView.NAME);}
        );


        layout.addComponentsAndExpand(label);
        layout.addComponentsAndExpand(btnSpat);
    }
    void zhrajDavku(String file){
        List <ZaznamCsv> zaznam;
        try {
            zaznam= DavkaCsvImporter.nacitajCsvDavku(file);
            DokladyNastroje.zalozkDokladovuDavku(zaznam);
            
            System.out.println(zaznam.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
//    void advanced(VerticalLayout layout) {
//        // BEGIN-EXAMPLE: component.upload.advanced
//        class UploadBox extends CustomComponent
//                implements Receiver, ProgressListener,
//                FailedListener, SucceededListener {
//            private static final long serialVersionUID = -46336015006190050L;
//
//            // Put upload in this memory buffer that grows automatically
//            ByteArrayOutputStream os =
//                    new ByteArrayOutputStream(10240);
//
//            // Name of the uploaded file
//            String filename;
//
//            ProgressBar progress = new ProgressBar(0.0f);
//
//            // Show uploaded file in this placeholder
//            Image image = new Image("Uploaded Image");
//
//            public UploadBox() {
//                // Create the upload component and handle all its events
//                Upload upload = new Upload("Upload the image here", null);
//                upload.setReceiver(this);
//                upload.addProgressListener(this);
//                upload.addFailedListener(this);
//                upload.addSucceededListener(this);
//
//                // Put the upload and image display in a panel
//                Panel panel = new Panel("Cool Image Storage");
//                panel.setWidth("400px");
//                VerticalLayout panelContent = new VerticalLayout();
//                panelContent.setSpacing(true);
//                panel.setContent(panelContent);
//                panelContent.addComponent(upload);
//                panelContent.addComponent(progress);
//                panelContent.addComponent(image);
//
//                progress.setVisible(false);
//                image.setVisible(false);
//
//                setCompositionRoot(panel);
//            }
//
//            public OutputStream receiveUpload(String filename, String mimeType) {
//                this.filename = filename;
//                os.reset(); // Needed to allow re-uploading
//                return os;
//            }
//
//            @Override
//            public void updateProgress(long readBytes, long contentLength) {
//                progress.setVisible(true);
//                if (contentLength == -1)
//                    progress.setIndeterminate(true);
//                else {
//                    progress.setIndeterminate(false);
//                    progress.setValue(((float)readBytes) /
//                            ((float)contentLength));
//                }
//            }
//
//            public void uploadSucceeded(SucceededEvent event) {
//                image.setVisible(true);
//                image.setCaption("Uploaded Image " + filename +
//                        " has length " + os.toByteArray().length);
//
//                // Display the image as a stream resource from
//                // the memory buffer
//                StreamSource source = new StreamSource() {
//                    private static final long serialVersionUID = -4905654404647215809L;
//
//                    public InputStream getStream() {
//                        return new ByteArrayInputStream(os.toByteArray());
//                    }
//                };
//
//                if (image.getSource() == null)
//                    // Create a new stream resource
//                    image.setSource(new StreamResource(source, filename));
//                else { // Reuse the old resource
//                    StreamResource resource =
//                            (StreamResource) image.getSource();
//                    resource.setStreamSource(source);
//                    resource.setFilename(filename);
//                }
//
//                image.markAsDirty();
//            }
//
//            @Override
//            public void uploadFailed(FailedEvent event) {
//                Notification.show("Upload failed",
//                        Notification.Type.ERROR_MESSAGE);
//            }
//        }
//
//        UploadBox uploadbox = new UploadBox();
//        layout.addComponent(uploadbox);
//        // END-EXAMPLE: component.upload.advanced
//    }
}
