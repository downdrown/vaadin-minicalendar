package org.vaadin.addons.minicalendar.util;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.function.SerializableSupplier;

import java.util.Map;

public final class ComponentUtils {

    private ComponentUtils() {
        // no-op
    }

    public static Div labelWithStyles(String text, Map<String, String> styles) {
        var label = new Div(text);
        styles.forEach(label.getStyle()::set);
        return label;
    }

    public static Component header(String header) {
        return labelWithStyles(
            header,
            Map.of(
                "font-size", "var(--lumo-font-size-l)",
                "color", "var(--lumo-primary-text-color)"
            )
        );
    }

    public static Component fromDefinition(SerializableSupplier<Component> definition) {
        return definition.get();
    }
}
