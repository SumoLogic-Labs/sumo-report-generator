package com.sumologic.report.generator.excel;

import com.sumologic.report.config.WorksheetConfig;

public interface WorksheetPopulator {

    void populateSheetWithData(WorksheetConfig worksheetConfig);

}