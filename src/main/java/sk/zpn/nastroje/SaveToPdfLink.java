package sk.zpn.nastroje;


import com.vaadin.server.StreamResource;
import com.vaadin.ui.Link;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SaveToPdfLink extends Link {
    String namefile;
    public SaveToPdfLink(String namefile) {

        super();
        this.namefile=namefile;
        setCaption("Kliknutim preberete zostavu");
        setDescription("Stiahnute súboru do PC");
        setTargetName("_blank");
    }

    @Override
    public void attach() {
        super.attach(); // Must call.
        StreamResource.StreamSource source = new StreamResource.StreamSource() {
            public InputStream getStream() {
                byte[] b = null;
                File f=new  File(namefile);
                try {
                    b =  FileUtils.readFileToByteArray(f);
                } catch (IOException e) {
                }
                return new ByteArrayInputStream(b);
            }
        };
        StreamResource resource = new StreamResource(source, namefile );

        resource.getStream().setParameter("Content-Disposition", "attachment;filename=\"" + namefile + "\"");
        resource.setMIMEType("application/pdf");
        resource.setCacheTime(0);

        setResource(resource);
    }




}
