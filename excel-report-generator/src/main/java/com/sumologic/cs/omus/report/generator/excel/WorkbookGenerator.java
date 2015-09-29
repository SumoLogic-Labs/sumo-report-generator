package com.sumologic.cs.omus.report.generator.excel;

import com.sumologic.cs.omus.report.generator.api.OmusReportGenerationException;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;

public interface WorkbookGenerator {

    void generateWorkbook(ReportConfig reportConfig) throws OmusReportGenerationException;

}