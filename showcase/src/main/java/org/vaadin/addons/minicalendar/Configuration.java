package org.vaadin.addons.minicalendar;

import java.io.IOException;
import java.util.Properties;

public class Configuration {

    public static Configuration INSTANCE = new Configuration();

    private static final String PROP_FILE = "config.properties";

    private final Properties properties;

    private Configuration() {
        try {
            properties = new Properties();
            properties.load(Configuration.class.getClassLoader().getResourceAsStream(PROP_FILE));
        } catch (IOException e) {
            throw new IllegalStateException("Could load configuration properties", e);
        }
    }

    public String getBuyMeACoffeeImageUrl() {
        return properties.getProperty("buymeacoffee.image.url");
    }
    public String getBuyMeACoffeeImageAltText() {
        return properties.getProperty("buymeacoffee.image.alt-text");
    }
    public String getBuyMeACoffeeCoffeeUrl() {
        return properties.getProperty("buymeacoffee.coffeeurl");
    }
}
