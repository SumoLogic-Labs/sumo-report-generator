package com.sumologic.cs.omus.report.generator.excel;

import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;

public interface WorkbookGenerator {

    Workbook generateWorkbook(ReportConfig reportConfig) throws IOException;

}