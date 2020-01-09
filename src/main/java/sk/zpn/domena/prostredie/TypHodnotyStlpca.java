package sk.zpn.domena.prostredie;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TypHodnotyStlpca {
    SIRKA("sirka"),
    PORADIE("poradie");


    private String displayValue;

    TypHodnotyStlpca(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public static TypHodnotyStlpca fromDisplayName(String displayName) {
        switch (displayName) {
            case "sirka": return SIRKA;
            case "poradie": return PORADIE;
            default: throw new IllegalArgumentException(
                    String.format("Typ hodnoty stlpca: %s", displayName));
        }
    }

    public static List<String> getListOfDisplayValues() {
        return Arrays.stream(TypHodnotyStlpca.values())
                .map(TypHodnotyStlpca::getDisplayValue)
                .collect(Collectors.toList());
    }

}
