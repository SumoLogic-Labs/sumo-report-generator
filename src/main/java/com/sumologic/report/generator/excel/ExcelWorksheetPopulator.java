package com.sumologic.report.generator.excel;

import com.sumologic.client.searchjob.model.GetRecordsForSearchJobResponse;
import com.sumologic.client.searchjob.model.GetSearchJobStatusResponse;
import com.sumologic.client.searchjob.model.SearchJobField;
import com.sumologic.client.searchjob.model.SearchJobRecord;
import com.sumologic.report.config.ReportSheet;
import com.sumologic.report.config.WorksheetConfig;
import com.sumologic.service.SumoDataService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

@Service
public class ExcelWorksheetPopulator implements WorksheetPopulator {

    private static final Log LOGGER = LogFactory.getLog(ExcelWorksheetPopulator.class);

    private static int DEFAULT_START = 0;
    private static int MAX_OFFSET = 10000;

    @Override
    public void populateSheetWithData(WorksheetConfig worksheetConfig) {
        String jobId = worksheetConfig.getSumoDataService().executeSearchJob(worksheetConfig.getReportSheet().getSearchJob());
        LOGGER.info("got back search job id " + jobId);
        GetSearchJobStatusResponse statusResponse =
                worksheetConfig.getSumoDataService().pollSearchJobUntilComplete(jobId, worksheetConfig.getReportSheet().getSheetName());
        LOGGER.info("found a total of " + statusResponse.getRecordCount() + " records");
        iterateAndPopulate(jobId, statusResponse, worksheetConfig);
    }


    private void iterateAndPopulate(String jobId, GetSearchJobStatusResponse statusResponse, WorksheetConfig worksheetConfig) {
        SumoDataService sumoDataService = worksheetConfig.getSumoDataService();
        Sheet workbookSheet = worksheetConfig.getWorkbookSheet();
        GetRecordsForSearchJobResponse recordsResponse = sumoDataService.getRecordsResponse(jobId, DEFAULT_START, MAX_OFFSET);
        populateColumnHeaders(workbookSheet, recordsResponse);
        int recordIterationIndex = 0;
        int recordPopulationIndex = 0;
        if (worksheetConfig.getReportConfig().isAppendToDestination()) {
            recordPopulationIndex = workbookSheet.getPhysicalNumberOfRows() - 1;
        }
        while (recordIterationIndex < statusResponse.getRecordCount()) {
            populateRecords(recordPopulationIndex, workbookSheet, recordsResponse);
            recordIterationIndex += recordsResponse.getRecords().size();
            recordsResponse = sumoDataService.getRecordsResponse(jobId, recordIterationIndex, MAX_OFFSET);
            recordPopulationIndex = workbookSheet.getPhysicalNumberOfRows() - 1;
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
                createCell(record, row, field, colIndex);
            }
            rowIndex++;
        }
    }

    private void createCell(SearchJobRecord record, Row row, SearchJobField field, int colIndex) {
        Cell cell = row.createCell(colIndex);
        String cellValue = record.getMap().get(field.getName());
        if (isNumericField(field)) {
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            if (isDoubleField(field)) {
                cell.setCellValue(Double.parseDouble(cellValue));
            } else if (isLongField(field)) {
                cell.setCellValue(Long.parseLong(cellValue));
            } else if (isIntField(field)) {
                cell.setCellValue(Integer.parseInt(cellValue));
            } else {
                LOGGER.error("unknown field type " + field.getFieldType());
            }
        } else {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(cellValue);
        }
    }


    private boolean isNumericField(SearchJobField field) {
        return isIntField(field) || isLongField(field) || isDoubleField(field);
    }

    private boolean isDoubleField(SearchJobField field) {
        return field.getFieldType().equals("double");
    }

    private boolean isLongField(SearchJobField field) {
        return field.getFieldType().equals("long");
    }

    private boolean isIntField(SearchJobField field) {
        return field.getFieldType().equals("int");
    }

}