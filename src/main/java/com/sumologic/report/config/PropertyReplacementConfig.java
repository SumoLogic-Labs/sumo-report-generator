package com.sumologic.report.config;

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

    /**
     * TODO: currently we are pulling just time frame properties. Probably need to refactor this so that
     * we have specific types of property pulls. Looking for a polymorphic effect.
     */
    public Properties toProperties() {
        Properties properties = new Properties();
        for (PlaceholderProperty placeholderProperty : propertyReplacements) {
            if("from_time".equalsIgnoreCase(placeholderProperty.getReplacementKey())) {
                String fromTime = ConvertToRealTimeUtil.timeAgo(placeholderProperty.getReplacementValue());
                String toTime = ConvertToRealTimeUtil.currentTime();
                properties.put("from_time", fromTime);
                properties.put("to_time", toTime);
            }
            else {
                properties.put(placeholderProperty.getReplacementKey(), placeholderProperty.getReplacementValue());
            }
        }
        return properties;
    }

}
