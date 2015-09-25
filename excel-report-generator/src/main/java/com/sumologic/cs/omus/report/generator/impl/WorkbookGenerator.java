package com.sumologic.cs.omus.report.generator.impl;

import com.sumologic.cs.omus.report.generator.api.OmusReportGenerationException;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;

public interface WorkbookGenerator {

    void generateWorkbookWithSheets(ReportConfig reportConfig) throws OmusReportGenerationException;

}