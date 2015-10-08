package com.sumologic.report.config;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PropertyReplacementConfigTest {

    @Test
    public void testToProperties() throws Exception {
        List<PlaceholderProperty> placeholderPropertyList = new ArrayList<>();
        PlaceholderProperty property1 = new PlaceholderProperty();
        property1.setReplacementKey("foo");
        property1.setReplacementValue("bar");
        placeholderPropertyList.add(property1);
        PropertyReplacementConfig propertyReplacementConfig = new PropertyReplacementConfig();
        propertyReplacementConfig.setPropertyReplacements(placeholderPropertyList);
        assertTrue(propertyReplacementConfig.toProperties().containsKey("foo"));
    }

}