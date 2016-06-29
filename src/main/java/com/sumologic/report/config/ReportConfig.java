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

public class ReportConfig {

    private String accessId;
    private String accessKey;
    private String url;
    private String destinationFile;
    private String templateFile;
    private boolean appendToDestination;
    private PropertyReplacementConfig propertyReplacementConfig;
    private List<ReportSheet> reportSheets;

    public boolean isAppendToDestination() {
        return appendToDestination;
    }

    public void setAppendToDestination(boolean appendToDestination) {
        this.appendToDestination = appendToDestination;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDestinationFile() {
        return destinationFile;
    }

    public void setDestinationFile(String destinationFile) {
        this.destinationFile = destinationFile;
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }

    public List<ReportSheet> getReportSheets() {
        return reportSheets;
    }

    public void setReportSheets(List<ReportSheet> reportSheets) {
        this.reportSheets = reportSheets;
    }

    public PropertyReplacementConfig getPropertyReplacementConfig() {
        return propertyReplacementConfig;
    }

    public void setPropertyReplacementConfig(PropertyReplacementConfig propertyReplacementConfig) {
        this.propertyReplacementConfig = propertyReplacementConfig;
    }

    @Override
    public String toString() {
        return "ReportConfig{" +
                "accessId='" + accessId + '\'' +
                ", url='" + url + '\'' +
                ", destinationFile='" + destinationFile + '\'' +
                ", templateFile='" + templateFile + '\'' +
                ", appendToDestination=" + appendToDestination +
                ", propertyReplacementConfig=" + propertyReplacementConfig +
                ", reportSheets=" + reportSheets +
                '}';
    }

}