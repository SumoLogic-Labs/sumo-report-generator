package com.sumologic.cs.omus.report.generator.api;

import java.util.List;
import java.util.Properties;

public class PropertyReplacementConfig {

    private List<PlaceholderProperty> propertyReplacements;

    public List<PlaceholderProperty> getPropertyReplacements() {
        return propertyReplacements;
    }

    public void setPropertyReplacements(List<PlaceholderProperty> propertyReplacements) {
        this.propertyReplacements = propertyReplacements;
    }

    public Properties toProperties() {
        Properties properties = new Properties();
        for (PlaceholderProperty placeholderProperty : propertyReplacements) {
            properties.put(placeholderProperty.getReplacementKey(), placeholderProperty.getReplacementValue());
        }
        return properties;
    }

}
