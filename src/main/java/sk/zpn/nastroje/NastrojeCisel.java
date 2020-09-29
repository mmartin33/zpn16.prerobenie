package sk.zpn.nastroje;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

public class NastrojeCisel {

    public static BigDecimal parse(String value) throws ParseException {
        return parse(value, null);
    }

    public static BigDecimal parse(String value, Locale locale) throws ParseException {
        NumberFormat format = (locale == null)
                ? NumberFormat.getNumberInstance()
                : NumberFormat.getNumberInstance(locale);

        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }

        String trim = value.trim();

        ParsePosition parsePosition = new ParsePosition(0);
        Number result = format.parse(trim, parsePosition);

        if (parsePosition.getIndex() != trim.length()) {
            throw new ParseException(trim, parsePosition.getIndex());
        }

        return (BigDecimal) result;
    }


}
