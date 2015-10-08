package com.sumologic.report.generator.excel;

import com.sumologic.client.searchjob.model.GetRecordsForSearchJobResponse;
import com.sumologic.client.searchjob.model.GetSearchJobStatusResponse;
import com.sumologic.client.searchjob.model.SearchJobField;
import com.sumologic.client.searchjob.model.SearchJobRecord;
import com.sumologic.report.config.ReportSheet;
import com.sumologic.service.SumoDataService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

@Service
public class ExcelWorksheetPopulator implements WorksheetPopulator {

    private static final Log LOGGER = LogFactory.getLog(ExcelWorksheetPopulator.class);

    private static int DEFAULT_START = 0;
    private static int MAX_OFFSET = 10000;

    @Override
    public void populateSheetWithData(Sheet workbookSheet, ReportSheet reportSheet, SumoDataService sumoDataService) {
        String jobId = sumoDataService.executeSearchJob(reportSheet.getSearchJob());
        GetSearchJobStatusResponse statusResponse = sumoDataService.pollSearchJobUntilComplete(jobId);
        LOGGER.info("found a total of " + statusResponse.getRecordCount() + " records");
        iterateAndPopulate(sumoDataService, jobId, statusResponse, workbookSheet);
    }


    private void iterateAndPopulate(SumoDataService sumoDataService, String jobId, GetSearchJobStatusResponse statusResponse, Sheet workbookSheet) {
        GetRecordsForSearchJobResponse recordsResponse = sumoDataService.getRecordsResponse(jobId, DEFAULT_START, MAX_OFFSET);
        populateColumnHeaders(workbookSheet, recordsResponse);
        int startRecordIndex = 0;
        while (startRecordIndex < statusResponse.getRecordCount()) {
            populateRecords(startRecordIndex, workbookSheet, recordsResponse);
            startRecordIndex += recordsResponse.getRecords().size();
            recordsResponse = sumoDataService.getRecordsResponse(jobId, startRecordIndex, MAX_OFFSET);
        }
    }

    private void populateColumnHeaders(Sheet workbookSheet, GetRecordsForSearchJobResponse recordsResponse) {
        if (workbookSheet.getPhysicalNumberOfRows() == 0) {
            int rowIndex = 0;
            int colIndex = 0;
            Row row = workbookSheet.createRow(rowIndex);
            for (SearchJobField field : recordsResponse.getFields()) {
                LOGGER.trace("adding column header " + field.getName());
                row.createCell(colIndex).setCellValue(field.getName());
                colIndex++;
            }
        }
    }

    private void populateRecords(int startRowIndex, Sheet workbookSheet, GetRecordsForSearchJobResponse recordsResponse) {
        int rowIndex = startRowIndex+1;
        for (SearchJobRecord record : recordsResponse.getRecords()) {
            LOGGER.trace("creating row " + rowIndex);
            Row row = workbookSheet.createRow(rowIndex);
            for (SearchJobField field : recordsResponse.getFields()) {
                int colIndex = ExcelUtils.lookupCellIndexByColumnName(field.getName(), workbookSheet);
                LOGGER.trace("creating cell " + colIndex + " with value " + record.getMap().get(field.getName()));
                row.createCell(colIndex).setCellValue(record.getMap().get(field.getName()));
            }
            rowIndex++;
        }
    }



}