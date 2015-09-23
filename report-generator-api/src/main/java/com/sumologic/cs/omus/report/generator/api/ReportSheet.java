package com.sumologic.cs.omus.report.generator.api;

public class ReportSheet {

    private String sheetName;
    private SearchJob searchJob;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public SearchJob getSearchJob() {
        return searchJob;
    }

    public void setSearchJob(SearchJob searchJob) {
        this.searchJob = searchJob;
    }

    @Override
    public String toString() {
        return "ReportSheet{" +
                "sheetName='" + sheetName + '\'' +
                ", searchJob=" + searchJob +
                '}';
    }

}