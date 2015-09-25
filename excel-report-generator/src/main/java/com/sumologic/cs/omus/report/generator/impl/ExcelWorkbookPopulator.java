package com.sumologic.cs.omus.report.generator.impl;

import com.sumologic.client.searchjob.model.GetRecordsForSearchJobResponse;
import com.sumologic.client.searchjob.model.GetSearchJobStatusResponse;
import com.sumologic.client.searchjob.model.SearchJobField;
import com.sumologic.client.searchjob.model.SearchJobRecord;
import com.sumologic.cs.omus.report.generator.api.OmusReportGenerationException;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.report.generator.api.ReportSheet;
import com.sumologic.cs.omus.service.SumoDataService;
import com.sumologic.cs.omus.service.SumoDataServiceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.WorkbookUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ExcelWorkbookPopulator implements WorkbookPopulator {

    private static final Log LOGGER = LogFactory.getLog(ExcelWorkbookPopulator.class);

    private static int DEFAULT_START = 0;
    private static int MAX_OFFSET = 10000;

    @Autowired
    private SumoDataServiceFactory sumoDataServiceFactory;

    @Override
    public void populateWorkbookWithData(ReportConfig reportConfig) throws OmusReportGenerationException {
        try {
            openWorkbookAndProcessSheets(reportConfig);
        } catch (IOException | InvalidFormatException e) {
            LOGGER.error(e);
            throw new OmusReportGenerationException("unable to populate workbook!");
        }
    }

    private void openWorkbookAndProcessSheets(ReportConfig reportConfig) throws IOException, InvalidFormatException {
        LOGGER.debug("populating workbook");
        File file = new File(reportConfig.getDestinationFile());
        FileInputStream fileInputStream = new FileInputStream(file);
        OPCPackage opcPackage = OPCPackage.open(fileInputStream);
        Workbook workbook = WorkbookFactory.create(opcPackage);
        SumoDataService sumoDataService = sumoDataServiceFactory.getSumoDataService(reportConfig);
        for (ReportSheet reportSheet : reportConfig.getReportSheets()) {
            LOGGER.info("populating sheet " + reportSheet.getSheetName());
            populateSheetWithData(workbook, reportSheet, sumoDataService);
        }
        FileOutputStream fileOut = new FileOutputStream(file);
        workbook.write(fileOut);
        fileInputStream.close();
        opcPackage.close();
        fileOut.close();
        LOGGER.debug("workbook populated");
    }

    private void populateSheetWithData(Workbook workbook, ReportSheet reportSheet, SumoDataService sumoDataService) {
        String jobId = sumoDataService.executeSearchJob(reportSheet.getSearchJob());
        GetSearchJobStatusResponse statusResponse = sumoDataService.pollSearchJobUntilComplete(jobId);
        LOGGER.info("found a total of " + statusResponse.getRecordCount() + " records");
        Sheet workbookSheet = workbook.getSheet(WorkbookUtil.createSafeSheetName(reportSheet.getSheetName()));
        if (statusResponse.getRecordCount() <= MAX_OFFSET) {
            populateSheetWithData(jobId, workbookSheet, sumoDataService);
        } else {
            iterateAndPopulate(sumoDataService, jobId, statusResponse, workbookSheet);
        }
    }

    private void populateSheetWithData(String jobId, Sheet workbookSheet, SumoDataService sumoDataService) {
        GetRecordsForSearchJobResponse recordsResponse = sumoDataService.getRecordsResponse(jobId, DEFAULT_START, MAX_OFFSET);
        populateColumnHeaders(workbookSheet, recordsResponse);
        populateRecords(workbookSheet, recordsResponse);
    }

    private void iterateAndPopulate(SumoDataService sumoDataService, String jobId, GetSearchJobStatusResponse statusResponse, Sheet workbookSheet) {
        GetRecordsForSearchJobResponse recordsResponse = sumoDataService.getRecordsResponse(jobId, DEFAULT_START, MAX_OFFSET);
        populateColumnHeaders(workbookSheet, recordsResponse);
        int startRecordIndex=1;
        while (startRecordIndex < statusResponse.getRecordCount()) {
            populateRecords(startRecordIndex, workbookSheet, recordsResponse);
            startRecordIndex += recordsResponse.getRecords().size();
            recordsResponse = sumoDataService.getRecordsResponse(jobId, startRecordIndex, MAX_OFFSET);
        }
    }

    private void populateColumnHeaders(Sheet workbookSheet, GetRecordsForSearchJobResponse recordsResponse) {
        int rowIndex = 0;
        int colIndex = 0;
        Row row = workbookSheet.createRow(rowIndex);
        for (SearchJobField field : recordsResponse.getFields()) {
            LOGGER.trace("adding column header " + field.getName());
            row.createCell(colIndex).setCellValue(field.getName());
            colIndex++;
        }
    }

    private void populateRecords(Sheet workbookSheet, GetRecordsForSearchJobResponse recordsResponse) {
        int rowIndex=1;
        for (SearchJobRecord record : recordsResponse.getRecords()) {
            LOGGER.trace("creating row " + rowIndex);
            Row row = workbookSheet.createRow(rowIndex);
            int colIndex = 0;
            for (SearchJobField field : recordsResponse.getFields()) {
                LOGGER.trace("creating cell " + colIndex + " with value " + record.getMap().get(field.getName()));
                row.createCell(colIndex).setCellValue(record.getMap().get(field.getName()));
                colIndex++;
            }
            rowIndex++;
        }
    }

    private void populateRecords(int startRowIndex, Sheet workbookSheet, GetRecordsForSearchJobResponse recordsResponse) {
        int rowIndex = startRowIndex;
        for (SearchJobRecord record : recordsResponse.getRecords()) {
            LOGGER.trace("creating row " + rowIndex);
            Row row = workbookSheet.createRow(rowIndex);
            int colIndex = 0;
            for (SearchJobField field : recordsResponse.getFields()) {
                LOGGER.trace("creating cell " + colIndex + " with value " + record.getMap().get(field.getName()));
                row.createCell(colIndex).setCellValue(record.getMap().get(field.getName()));
                colIndex++;
            }
            rowIndex++;
        }
    }

}