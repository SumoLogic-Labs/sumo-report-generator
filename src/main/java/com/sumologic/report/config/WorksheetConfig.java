package com.sumologic.report.config;

import com.sumologic.service.SumoDataService;
import org.apache.poi.ss.usermodel.Sheet;

public class WorksheetConfig {

    private Sheet workbookSheet;
    private ReportSheet reportSheet;
    private SumoDataService sumoDataService;
    private ReportConfig reportConfig;

    public Sheet getWorkbookSheet() {
        return workbookSheet;
    }

    public void setWorkbookSheet(Sheet workbookSheet) {
        this.workbookSheet = workbookSheet;
    }

    public ReportSheet getReportSheet() {
        return reportSheet;
    }

    public void setReportSheet(ReportSheet reportSheet) {
        this.reportSheet = reportSheet;
    }

    public SumoDataService getSumoDataService() {
        return sumoDataService;
    }

    public void setSumoDataService(SumoDataService sumoDataService) {
        this.sumoDataService = sumoDataService;
    }

    public ReportConfig getReportConfig() {
        return reportConfig;
    }

    public void setReportConfig(ReportConfig reportConfig) {
        this.reportConfig = reportConfig;
    }

}
