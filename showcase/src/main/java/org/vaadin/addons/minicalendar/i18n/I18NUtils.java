package org.vaadin.addons.minicalendar.i18n;

import com.vaadin.flow.component.UI;

public final class I18NUtils {

    private I18NUtils() {
        // no-op
    }

    public static String i18n(String key, Object... params) {
        return UI.getCurrent().getTranslation(key, params);
    }
}
