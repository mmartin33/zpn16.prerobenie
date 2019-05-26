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
        progresBar.setIndeterminate(false);

        progresBar.setWidth("400");
        this.addComponentsAndExpand(progresBar);

        btnStorno= new Button("Koniec");
        btnStorno.setVisible(false);
        this.addComponent(btnStorno);
        btnStorno.addClickListener(clickEvent -> {
            koniec();
        });
    }

    @Override
    public void nadstavNadpis(String nadpis) {
        this.lblNadpis.setValue(nadpis);

    }
    public void nadstavspustenie(Boolean spustit) {
        progresBar.setIndeterminate(spustit);

    }


    @Override
    public void koniec() {
        this.setVisible(false);
        progresBar.setIndeterminate(false);
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
