package sk.zpn.zaklad.grafickeNastroje;

import java.math.BigDecimal;

public interface IProgresBarZPN {
    public void nadstavNadpis(String nadpis);
    public void koniec();
    public void zobraz();
    public void posun(BigDecimal max, BigDecimal increment);
}
