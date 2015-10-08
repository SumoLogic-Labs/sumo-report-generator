package com.sumologic.report.generator;

import com.sumologic.report.config.ReportConfig;

public interface ReportGenerator {

    void generateReport(ReportConfig reportConfig) throws ReportGenerationException;

}