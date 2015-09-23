package com.sumologic.cs.omus.report.generator.api;

public class PlaceholderProperty {

    private String replacementKey;
    private String replacementValue;

    public String getReplacementKey() {
        return replacementKey;
    }

    public void setReplacementKey(String replacementKey) {
        this.replacementKey = replacementKey;
    }

    public String getReplacementValue() {
        return replacementValue;
    }

    public void setReplacementValue(String replacementValue) {
        this.replacementValue = replacementValue;
    }

}
