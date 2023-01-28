package org.vaadin.addons.minicalendar.util;

import com.vaadin.flow.component.html.Label;

import java.util.Map;

public final class ComponentUtils {

    private ComponentUtils() {
        // no-op
    }

    public static Label labelWithStyles(String text, Map<String, String> styles) {
        var label = new Label(text);
        styles.forEach(label.getStyle()::set);
        return label;
    }
}
