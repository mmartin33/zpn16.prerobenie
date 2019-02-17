package sk.zpn.domena;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TypUzivatela {
    ADMIN("Administrátor"),
    SPRAVCA_ZPN("Správca ZPN"),
    PREDAJCA("Predajca");

    private String displayValue;

    TypUzivatela(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public static TypUzivatela fromDisplayName(String displayName) {
        switch (displayName) {
            case "Administrátor": return ADMIN;
            case "Správca ZPN": return SPRAVCA_ZPN;
            case "Predajca": return PREDAJCA;
            default: throw new IllegalArgumentException(
                    String.format("Typ uzivatela je nepodporovany: %s", displayName));
        }
    }

    public static List<String> getListOfDisplayValues() {
        return Arrays.stream(TypUzivatela.values())
                .map(TypUzivatela::getDisplayValue)
                .collect(Collectors.toList());
    }

}
