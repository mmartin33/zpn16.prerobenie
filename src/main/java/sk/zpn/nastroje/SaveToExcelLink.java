package sk.zpn.nastroje;


import com.vaadin.server.StreamResource;
import com.vaadin.ui.Link;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class SaveToExcelLink extends Link {
    String namefile = "picture.xls";
    public SaveToExcelLink(String namefile) {

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
        //StreamResource resource = new StreamResource(source, namefile );
        StreamResource resource = new StreamResource(source, FilenameUtils.getBaseName(namefile)
                + "." + FilenameUtils.getExtension(namefile) );


        resource.getStream().setParameter("Content-Disposition", "attachment;filename=\"" + namefile + "\"");
        resource.setMIMEType("application/xlsx");
        resource.setCacheTime(0);

        setResource(resource);
    }




}
