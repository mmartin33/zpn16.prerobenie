package sk.zpn.domena;

import com.vaadin.icons.VaadinIcons;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum StatusUzivatela {
    ACTIVE("aktívny"),
    INACTIVE("neaktívny");

    private String displayValue;
    private String iconColor;

    StatusUzivatela(String displayValue) {
        this.displayValue = displayValue;
        this.iconColor = displayValue.equals("aktívny") ? "#156ab3" : "#9fa2a5";
    }

    public String getIconValue() {
        return String.format("<font color=\"%s\">%s</font>" , this.iconColor, VaadinIcons.CIRCLE.getHtml());
    }

    public static StatusUzivatela fromDisplayName(String displayName) {
        switch (displayName) {
            case "aktívny": return ACTIVE;
            case "neaktívny": return INACTIVE;
            default: throw new IllegalArgumentException(
                    String.format("Status uzivatela je nepodporovany: %s", displayName));
        }
    }

    public static List<String> getListOfDisplayValues() {
        return Arrays.stream(StatusUzivatela.values())
                .map(StatusUzivatela::getDisplayValue)
                .collect(Collectors.toList());
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public String getIconColor() {
        return iconColor;
    }
}
