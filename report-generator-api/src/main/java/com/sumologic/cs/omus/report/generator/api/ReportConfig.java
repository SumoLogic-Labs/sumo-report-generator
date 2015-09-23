package com.sumologic.cs.omus.report.generator.api;

import java.util.List;

public class ReportConfig {

    private String username;
    private String password;
    private String url;
    private String destinationFile;
    private PropertyReplacementConfig propertyReplacementConfig;
    private List<ReportSheet> reportSheets;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                ", destinationFile='" + destinationFile + '\'' +
                ", propertyReplacementConfig=" + propertyReplacementConfig +
                ", reportSheets=" + reportSheets +
                '}';
    }

}