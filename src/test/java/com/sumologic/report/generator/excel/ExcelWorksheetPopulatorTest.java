package com.sumologic.report.generator.excel;

import com.sumologic.client.searchjob.model.GetRecordsForSearchJobResponse;
import com.sumologic.client.searchjob.model.GetSearchJobStatusResponse;
import com.sumologic.client.searchjob.model.SearchJobField;
import com.sumologic.client.searchjob.model.SearchJobRecord;
import com.sumologic.report.config.ReportConfig;
import com.sumologic.report.config.ReportSheet;
import com.sumologic.report.config.WorksheetConfig;
import com.sumologic.service.SumoDataService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ExcelWorksheetPopulatorTest extends BaseExcelTest {

    @Mock
    private SumoDataService sumoDataService;

    @Autowired
    private WorksheetPopulator worksheetPopulator;

    @Test
    public void testNoRecordsReturned() throws Exception {
        ReflectionTestUtils.setField(worksheetPopulator, "MAX_OFFSET", 10000);
        ReportSheet reportSheet = new ReportSheet();
        reportSheet.setSheetName("ingest");
        when(sumoDataService.executeSearchJob(any())).thenReturn("1234");
        when(sumoDataService.pollSearchJobUntilComplete("1234", "ingest")).thenReturn(new GetSearchJobStatusResponse());
        GetRecordsForSearchJobResponse recordsResponse = new GetRecordsForSearchJobResponse();
        recordsResponse.setFields(new ArrayList<>());
        recordsResponse.setRecords(new ArrayList<>());
        when(sumoDataService.getRecordsResponse("1234", 0, 10000)).thenReturn(recordsResponse);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ingest");
        WorksheetConfig config = new WorksheetConfig();
        config.setSumoDataService(sumoDataService);
        config.setReportSheet(reportSheet);
        config.setWorkbookSheet(sheet);
        ReportConfig reportConfig = Mockito.mock(ReportConfig.class);
        when(reportConfig.isAppendToDestination()).thenReturn(false);
        config.setReportConfig(reportConfig);
        worksheetPopulator.populateSheetWithData(config);
        assertEquals("ingest", sheet.getSheetName());
        assertEquals(1, sheet.getPhysicalNumberOfRows());
    }

    @Test
    public void testWithData() throws Exception {
        ReflectionTestUtils.setField(worksheetPopulator, "MAX_OFFSET", 10000);
        ReportSheet reportSheet = new ReportSheet();
        reportSheet.setSheetName("ingest");
        when(sumoDataService.executeSearchJob(any())).thenReturn("1234");
        GetSearchJobStatusResponse searchJobStatusResponse = new GetSearchJobStatusResponse();
        searchJobStatusResponse.setRecordCount(2);
        when(sumoDataService.pollSearchJobUntilComplete("1234", "ingest")).thenReturn(searchJobStatusResponse);
        GetRecordsForSearchJobResponse recordsResponse = getSampleRecordsResponse();
        when(sumoDataService.getRecordsResponse("1234", 0, 10000)).thenReturn(recordsResponse);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ingest");
        Row colRow = sheet.createRow(0);
        colRow.createCell(0).setCellValue("column1");
        colRow.createCell(1).setCellValue("column2");
        WorksheetConfig config = new WorksheetConfig();
        config.setSumoDataService(sumoDataService);
        config.setReportSheet(reportSheet);
        config.setWorkbookSheet(sheet);
        ReportConfig reportConfig = Mockito.mock(ReportConfig.class);
        when(reportConfig.isAppendToDestination()).thenReturn(false);
        config.setReportConfig(reportConfig);
        worksheetPopulator.populateSheetWithData(config);
        assertEquals(1, workbook.getNumberOfSheets());
        assertEquals("ingest", sheet.getSheetName());
        assertEquals(3, sheet.getPhysicalNumberOfRows());
    }

    @Test
    public void testColumnsAlreadyExistWithData() throws Exception {
        ReflectionTestUtils.setField(worksheetPopulator, "MAX_OFFSET", 10000);
        ReportSheet reportSheet = new ReportSheet();
        reportSheet.setSheetName("ingest");
        when(sumoDataService.executeSearchJob(any())).thenReturn("1234");
        GetSearchJobStatusResponse searchJobStatusResponse = new GetSearchJobStatusResponse();
        searchJobStatusResponse.setRecordCount(2);
        when(sumoDataService.pollSearchJobUntilComplete("1234", "ingest")).thenReturn(searchJobStatusResponse);
        GetRecordsForSearchJobResponse recordsResponse = getSampleRecordsResponse();
        when(sumoDataService.getRecordsResponse("1234", 0, 10000)).thenReturn(recordsResponse);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ingest");
        WorksheetConfig config = new WorksheetConfig();
        config.setSumoDataService(sumoDataService);
        config.setReportSheet(reportSheet);
        config.setWorkbookSheet(sheet);
        ReportConfig reportConfig = Mockito.mock(ReportConfig.class);
        when(reportConfig.isAppendToDestination()).thenReturn(false);
        config.setReportConfig(reportConfig);
        worksheetPopulator.populateSheetWithData(config);
        assertEquals(1, workbook.getNumberOfSheets());
        assertEquals("ingest", sheet.getSheetName());
        assertEquals(3, sheet.getPhysicalNumberOfRows());
    }

    @Test
    public void testWithAppend() throws Exception {
        ReflectionTestUtils.setField(worksheetPopulator, "MAX_OFFSET", 10000);
        ReportSheet reportSheet = new ReportSheet();
        reportSheet.setSheetName("ingest");
        when(sumoDataService.executeSearchJob(any())).thenReturn("1234");
        GetSearchJobStatusResponse searchJobStatusResponse = new GetSearchJobStatusResponse();
        searchJobStatusResponse.setRecordCount(2);
        when(sumoDataService.pollSearchJobUntilComplete("1234", "ingest")).thenReturn(searchJobStatusResponse);
        GetRecordsForSearchJobResponse recordsResponse = getSampleRecordsResponse();
        when(sumoDataService.getRecordsResponse("1234", 0, 10000)).thenReturn(recordsResponse);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ingest");
        Row colRow = sheet.createRow(0);
        colRow.createCell(0).setCellValue("column1");
        colRow.createCell(1).setCellValue("column2");
        WorksheetConfig config = new WorksheetConfig();
        config.setSumoDataService(sumoDataService);
        config.setReportSheet(reportSheet);
        config.setWorkbookSheet(sheet);
        ReportConfig reportConfig = Mockito.mock(ReportConfig.class);
        when(reportConfig.isAppendToDestination()).thenReturn(false);
        config.setReportConfig(reportConfig);
        worksheetPopulator.populateSheetWithData(config);
        when(reportConfig.isAppendToDestination()).thenReturn(true);
        worksheetPopulator.populateSheetWithData(config);
        assertEquals(1, workbook.getNumberOfSheets());
        assertEquals("ingest", sheet.getSheetName());
        assertEquals(5, sheet.getPhysicalNumberOfRows());
    }

    @Test
    public void testWithMoreThanOffsetData() throws Exception {
        ReflectionTestUtils.setField(worksheetPopulator, "MAX_OFFSET", 1);
        ReportSheet reportSheet = new ReportSheet();
        reportSheet.setSheetName("ingest");
        when(sumoDataService.executeSearchJob(any())).thenReturn("1234");
        GetSearchJobStatusResponse searchJobStatusResponse = new GetSearchJobStatusResponse();
        searchJobStatusResponse.setRecordCount(2);
        when(sumoDataService.pollSearchJobUntilComplete("1234", "ingest")).thenReturn(searchJobStatusResponse);
        GetRecordsForSearchJobResponse recordsResponse = getSampleRecordsResponse();
        when(sumoDataService.getRecordsResponse("1234", 0, 1)).thenReturn(recordsResponse);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ingest");
        WorksheetConfig config = new WorksheetConfig();
        config.setSumoDataService(sumoDataService);
        config.setReportSheet(reportSheet);
        config.setWorkbookSheet(sheet);
        ReportConfig reportConfig = Mockito.mock(ReportConfig.class);
        when(reportConfig.isAppendToDestination()).thenReturn(false);
        config.setReportConfig(reportConfig);
        worksheetPopulator.populateSheetWithData(config);
        assertEquals(1, workbook.getNumberOfSheets());
        assertEquals("ingest", sheet.getSheetName());
        assertEquals(3, sheet.getPhysicalNumberOfRows());
    }

    private GetRecordsForSearchJobResponse getSampleRecordsResponse() {
        GetRecordsForSearchJobResponse recordsResponse = new GetRecordsForSearchJobResponse();
        List<SearchJobField> fields = new ArrayList<>();
        SearchJobField field1 = new SearchJobField();
        field1.setName("column1");
        field1.setFieldType("string");
        SearchJobField field2 = new SearchJobField();
        field2.setName("column2");
        field2.setFieldType("string");
        fields.add(field1);
        fields.add(field2);
        recordsResponse.setFields(fields);
        List<SearchJobRecord> records = new ArrayList<>();
        SearchJobRecord record1 = new SearchJobRecord();
        Map<String, String> record1Map = new HashMap<>();
        record1Map.put("column1", "foo");
        record1Map.put("column2", "bar");
        record1.setMap(record1Map);
        SearchJobRecord record2 = new SearchJobRecord();
        Map<String, String> record2Map = new HashMap<>();
        record2Map.put("column1", "foo1");
        record2Map.put("column2", "bar1");
        record2.setMap(record2Map);
        records.add(record1);
        records.add(record2);
        recordsResponse.setRecords(records);
        return recordsResponse;
    }

}