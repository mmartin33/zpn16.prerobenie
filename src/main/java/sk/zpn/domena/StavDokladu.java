package sk.zpn.domena;

import com.vaadin.icons.VaadinIcons;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum StavDokladu {
    POTVRDENY("potvrdený"),
    NEPOTVRDENY("nepotvrdený");

    private String displayValue;
    private String iconColor;

    StavDokladu(String displayValue) {
        this.displayValue = displayValue;
        this.iconColor = displayValue.equals("potvrdený") ? "#156ab3" : "#9fa2a5";
    }

    public String getIconValue() {
        return String.format("<font color=\"%s\">%s</font>" , this.iconColor, VaadinIcons.CIRCLE.getHtml());
    }

    public static StavDokladu fromDisplayName(String displayName) {
        switch (displayName) {
            case "potvrdený": return POTVRDENY;
            case "nepotvrdený": return NEPOTVRDENY;
            default: throw new IllegalArgumentException(
                    String.format("Stav dokladu je nepodporovany: %s", displayName));
        }
    }

    public static List<String> getListOfDisplayValues() {
        return Arrays.stream(StavDokladu.values())
                .map(StavDokladu::getDisplayValue)
                .collect(Collectors.toList());
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public String getIconColor() {
        return iconColor;
    }
}
