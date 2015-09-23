package com.sumologic.cs.omus.report.generator.impl;

import com.sumologic.cs.omus.report.generator.api.ReportConfig;

import java.io.IOException;

public interface WorkbookGenerator {

    void generateWorkbookWithSheets(ReportConfig reportConfig) throws IOException;

}