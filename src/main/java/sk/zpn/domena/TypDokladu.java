package sk.zpn.domena;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TypDokladu {
    DAVKA("Dávka"),
    VYDAJ_BONUSU("Výdaj bonusu"),
    INTERNY_DOKLAD("Interný doklad");

    private String displayValue;

    TypDokladu(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public static TypDokladu fromDisplayName(String displayName) {
        switch (displayName) {
            case "Dávka": return DAVKA;
            case "Výdaj bonusu": return VYDAJ_BONUSU;
            case "Interný doklad": return INTERNY_DOKLAD;
            default: throw new IllegalArgumentException(
                    String.format("Typ dokladu je nepodporovany: %s", displayName));
        }
    }

    public static List<String> getListOfDisplayValues() {
        return Arrays.stream(TypDokladu.values())
                .map(TypDokladu::getDisplayValue)
                .collect(Collectors.toList());
    }

}
