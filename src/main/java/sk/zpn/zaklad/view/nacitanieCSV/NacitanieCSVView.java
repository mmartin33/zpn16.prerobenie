package sk.zpn.zaklad.view.nacitanieCSV;

import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;
import sk.zpn.domena.TypUzivatela;
import sk.zpn.domena.Uzivatel;
import sk.zpn.domena.VysledokImportu;
import sk.zpn.zaklad.model.UzivatelNastroje;
import sk.zpn.zaklad.view.prevadzky.EditacnyForm;

import java.time.LocalDate;


public class NacitanieCSVView extends VerticalLayout implements View {

    public static final String NAME = "nacitanieDbfView";
    private final UploadCSV ue;
    private VysledokImportu vysledokImportu;
    private BrowsPanelVysledkov browsPanelVysledkov;
    private FormLayout frmVstupneUdaje;
    private TextField tfFirma;
    private TextField tfobdobie;

    public NacitanieCSVView(){

        ue=new UploadCSV(this);
        ue.init();
        frmVstupneUdaje=new FormLayout();
        tfFirma=new TextField("Firma");
        tfFirma.setWidth(150, Sizeable.Unit.PIXELS);
        tfobdobie=new TextField("Do obdobia");
        this.addComponent(ue);
        browsPanelVysledkov=new BrowsPanelVysledkov();
        this.addComponent(browsPanelVysledkov);
        browsPanelVysledkov.setVisible(false);



    }

    public VysledokImportu getVysledokImportu() {
        return vysledokImportu;
    }

    public void init(){
        if (UzivatelNastroje.getPrihlasenehoUzivatela().getTypUzivatela()!=TypUzivatela.SPRAVCA_ZPN)
            tfFirma.setValue(UzivatelNastroje.getPrihlasenehoUzivatela().getFirma().getNazov());
        Integer rok =(Integer)LocalDate.now().getYear();
        Integer mesiac =(Integer)LocalDate.now().getMonthValue();
        tfobdobie.setValue(rok.toString()+StringUtils.leftPad(mesiac.toString(),2,'0'));


    }
    public void setVysledokImportu(VysledokImportu vysledokImportu) {
        this.vysledokImportu = vysledokImportu;
        this.browsPanelVysledkov.setVysledokImportu(vysledokImportu);


    }
}

