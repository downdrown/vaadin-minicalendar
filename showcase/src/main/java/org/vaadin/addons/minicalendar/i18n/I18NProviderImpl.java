package org.vaadin.addons.minicalendar.i18n;

import com.vaadin.flow.i18n.I18NProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class I18NProviderImpl implements I18NProvider {

    private static final Logger log = LoggerFactory.getLogger(I18NProviderImpl.class);
    private static final String BUNDLE_PREFIX = "translations";

    @Override
    public List<Locale> getProvidedLocales() {
        return List.of(
            Locale.ENGLISH,
            Locale.GERMAN
        );
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key != null && locale != null) {
            var bundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale);
            try {
                var resolvedTranslation = bundle.getString(key);
                if (params.length == 0) {
                    return resolvedTranslation;
                } else {
                    return MessageFormat.format(resolvedTranslation, params);
                }
            } catch (MissingResourceException e) {
                log.warn("Missing resource '{}' in translations for {}", key, locale);
                return "? $key ?";
            }
        }

        throw new IllegalArgumentException("Required arguments missing to resolve translation");
    }
}
