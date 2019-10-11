package sk.zpn.domena;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TypProduktov {
    BODOVACI("Bodovaci"),
    ODMENA("Odmena");


    private String displayValue;

    TypProduktov(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public static TypProduktov fromDisplayName(String displayName) {
        switch (displayName) {
            case "Bodovaci": return BODOVACI;
            case "Odmena": return ODMENA;
            default: throw new IllegalArgumentException(
                    String.format("Typ produktu: %s", displayName));
        }
    }

    public static List<String> getListOfDisplayValues() {
        return Arrays.stream(TypProduktov.values())
                .map(TypProduktov::getDisplayValue)
                .collect(Collectors.toList());
    }

}
