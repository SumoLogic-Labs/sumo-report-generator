package com.sumologic.report.generator.excel;

import com.sumologic.report.config.ReportConfig;
import com.sumologic.report.generator.ReportGenerationException;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;

public interface WorkbookPopulator {

    void populateWorkbookWithData(ReportConfig reportConfig, Workbook workbook) throws ReportGenerationException;

}