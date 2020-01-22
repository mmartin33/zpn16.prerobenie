package sk.zpn.domena.log;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TypLogovanejHodnoty {
    UZIVATEL("uzivatel"),
    POLOZKA_DOKLADU("polozkaDokladu"),
    DOKLAD("doklad");


    private String displayValue;

    TypLogovanejHodnoty(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public static TypLogovanejHodnoty fromDisplayName(String displayName) {
        switch (displayName) {
            case "uzivatel": return UZIVATEL;
            case "polozkaDokladu": return POLOZKA_DOKLADU;
            case "doklad": return DOKLAD;
            default: throw new IllegalArgumentException(
                    String.format("Typ hodnoty stlpca: %s", displayName));
        }
    }

    public static List<String> getListOfDisplayValues() {
        return Arrays.stream(TypLogovanejHodnoty.values())
                .map(TypLogovanejHodnoty::getDisplayValue)
                .collect(Collectors.toList());
    }

}
