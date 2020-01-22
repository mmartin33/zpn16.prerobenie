package sk.zpn.domena.log;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TypUkonu {
    PRIDANIE("pridanie"),
    VYMAZ("vymaz"),
    OPRAVA("oprava"),
    PRIHLASENIE("prihlasenie");


    private String displayValue;

    TypUkonu(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public static TypUkonu fromDisplayName(String displayName) {
        switch (displayName) {
            case "pridanie": return PRIDANIE;
            case "vymaz": return VYMAZ;
            case "prihasenie": return PRIHLASENIE;
            case "oprava": return OPRAVA;
            default: throw new IllegalArgumentException(
                    String.format("Typ hodnoty stlpca: %s", displayName));
        }
    }

    public static List<String> getListOfDisplayValues() {
        return Arrays.stream(TypUkonu.values())
                .map(TypUkonu::getDisplayValue)
                .collect(Collectors.toList());
    }

}
