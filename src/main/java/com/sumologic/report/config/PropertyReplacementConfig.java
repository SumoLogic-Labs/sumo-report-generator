/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
