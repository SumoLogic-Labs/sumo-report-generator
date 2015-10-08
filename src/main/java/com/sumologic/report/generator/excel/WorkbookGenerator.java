package com.sumologic.report.generator.excel;

import com.sumologic.report.config.ReportConfig;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;

public interface WorkbookGenerator {

    Workbook generateWorkbook(ReportConfig reportConfig) throws IOException;

}