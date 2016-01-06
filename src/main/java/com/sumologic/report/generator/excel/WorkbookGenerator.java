package com.sumologic.report.generator.excel;

import com.sumologic.report.config.ReportConfig;
import com.sumologic.report.generator.ReportGenerationException;
import org.apache.poi.ss.usermodel.Workbook;

public interface WorkbookGenerator {

    Workbook generateWorkbook(ReportConfig reportConfig) throws ReportGenerationException;

}