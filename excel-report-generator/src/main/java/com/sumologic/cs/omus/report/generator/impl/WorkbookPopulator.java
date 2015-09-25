package com.sumologic.cs.omus.report.generator.impl;

import com.sumologic.cs.omus.report.generator.api.OmusReportGenerationException;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;

public interface WorkbookPopulator {

    void populateWorkbookWithData(ReportConfig reportConfig) throws OmusReportGenerationException;

}