package com.sumologic.report.generator.excel;

import com.sumologic.report.config.ReportSheet;
import com.sumologic.service.SumoDataService;
import org.apache.poi.ss.usermodel.Sheet;

public interface WorksheetPopulator {

    void populateSheetWithData(Sheet workbookSheet, ReportSheet reportSheet, SumoDataService sumoDataService);

}