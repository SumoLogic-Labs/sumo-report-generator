package com.sumologic.cs.omus.report.generator.excel;

import com.sumologic.cs.omus.report.generator.api.ReportSheet;
import com.sumologic.cs.omus.service.SumoDataService;
import org.apache.poi.ss.usermodel.Sheet;

public interface WorksheetPopulator {

    void populateSheetWithData(Sheet workbookSheet, ReportSheet reportSheet, SumoDataService sumoDataService);

}