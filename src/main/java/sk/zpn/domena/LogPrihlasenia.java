package sk.zpn.domena;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(name = "log_prihlasenia")
@NamedQueries(value = {
        @NamedQuery(name = "LogPrihlasenia.getAll", query = "SELECT l FROM log_prihlasenia l")})

public class LogPrihlasenia extends Vseobecne {

    public LogPrihlasenia() {
    }

}





