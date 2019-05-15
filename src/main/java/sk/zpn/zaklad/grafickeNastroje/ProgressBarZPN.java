package sk.zpn.zaklad.grafickeNastroje;


import com.vaadin.ui.*;


import java.math.BigDecimal;

public class ProgressBarZPN extends VerticalLayout implements IProgresBarZPN{
    Label lblNadpis;
    public ProgressBar progresBar;
    Button btnStorno;

    public Float getProgresBarValue() {
        return progresBar.getValue();
    }

    public void setProgresBarValue(Float value) {
        this.progresBar.setValue(value);
    }

    public ProgressBarZPN(String nadpis) {
        lblNadpis=new Label(nadpis);
        this.addComponent(lblNadpis);
        progresBar=new ProgressBar();
        this.addComponentsAndExpand(progresBar);
        btnStorno= new Button("Koniec");
        this.addComponent(btnStorno);
        btnStorno.addClickListener(clickEvent -> {
            koniec();
        });
    }

    @Override
    public void nadstavNadpis(String nadpis) {
        this.lblNadpis.setValue(nadpis);

    }

    @Override
    public void koniec() {
        this.setVisible(false);
    }

    @Override
    public void zobraz() {
        this.setVisible(true);
    }

    @Override
    public void posun(BigDecimal max, BigDecimal increment) {
        this.progresBar.setValue(increment.divide(max,2,BigDecimal.ROUND_HALF_UP).floatValue());
        this.markAsDirtyRecursive();



    }
}
