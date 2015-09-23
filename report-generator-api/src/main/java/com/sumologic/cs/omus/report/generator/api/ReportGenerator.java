package com.sumologic.cs.omus.report.generator.api;

public interface ReportGenerator {

    void generateReport(ReportConfig reportConfig) throws OmusReportGenerationException;

}