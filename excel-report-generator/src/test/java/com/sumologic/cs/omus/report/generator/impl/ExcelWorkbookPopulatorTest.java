package com.sumologic.cs.omus.report.generator.impl;

import com.sumologic.client.searchjob.model.GetRecordsForSearchJobResponse;
import com.sumologic.client.searchjob.model.GetSearchJobStatusResponse;
import com.sumologic.client.searchjob.model.SearchJobField;
import com.sumologic.client.searchjob.model.SearchJobRecord;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.service.SumoDataService;
import com.sumologic.cs.omus.service.SumoDataServiceFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ExcelWorkbookPopulatorTest extends BaseExcelTest {

    @Autowired
    private WorkbookGenerator workbookGenerator;

    @Autowired
    private WorkbookPopulator workbookPopulator;

    @Mock
    private SumoDataServiceFactory sumoDataServiceFactory;

    @Mock
    private SumoDataService sumoDataService;

    @After
    public void tearDown() {
        Mockito.reset(sumoDataServiceFactory, sumoDataService);
    }

    @Test
    public void testNoRecordsReturned() throws Exception {
        ReflectionTestUtils.setField(workbookPopulator, "MAX_OFFSET", 10000);
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/TestJSON_testWorkbookPopulation.json");
        workbookGenerator.generateWorkbookWithSheets(reportConfig);
        when(sumoDataServiceFactory.getSumoDataService(reportConfig)).thenReturn(sumoDataService);
        when(sumoDataService.executeSearchJob(any())).thenReturn("1234");
        when(sumoDataService.pollSearchJobUntilComplete("1234")).thenReturn(new GetSearchJobStatusResponse());
        GetRecordsForSearchJobResponse recordsResponse = new GetRecordsForSearchJobResponse();
        recordsResponse.setFields(new ArrayList<>());
        recordsResponse.setRecords(new ArrayList<>());
        when(sumoDataService.getRecordsResponse("1234", 0, 10000)).thenReturn(recordsResponse);
        ReflectionTestUtils.setField(workbookPopulator, "sumoDataServiceFactory", sumoDataServiceFactory);
        workbookPopulator.populateWorkbookWithData(reportConfig);
        File file = new File(reportConfig.getDestinationFile());
        Assert.assertTrue(file.exists());
        Workbook createdWorkbook = new XSSFWorkbook(file);
        assertEquals(2, createdWorkbook.getNumberOfSheets());
        assertEquals("ingest", createdWorkbook.getSheetName(0));
        assertEquals("ingest2", createdWorkbook.getSheetName(1));
        assertEquals(1, createdWorkbook.getSheet("ingest").getPhysicalNumberOfRows());
        assertEquals(1, createdWorkbook.getSheet("ingest2").getPhysicalNumberOfRows());
    }

    @Test
         public void testWithData() throws Exception {
        ReflectionTestUtils.setField(workbookPopulator, "MAX_OFFSET", 10000);
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/TestJSON_testWorkbookPopulation.json");
        workbookGenerator.generateWorkbookWithSheets(reportConfig);
        when(sumoDataServiceFactory.getSumoDataService(reportConfig)).thenReturn(sumoDataService);
        when(sumoDataService.executeSearchJob(any())).thenReturn("1234");
        GetSearchJobStatusResponse searchJobStatusResponse = new GetSearchJobStatusResponse();
        searchJobStatusResponse.setRecordCount(2);
        when(sumoDataService.pollSearchJobUntilComplete("1234")).thenReturn(searchJobStatusResponse);
        GetRecordsForSearchJobResponse recordsResponse = getSampleRecordsResponse();
        when(sumoDataService.getRecordsResponse("1234", 0, 10000)).thenReturn(recordsResponse);
        ReflectionTestUtils.setField(workbookPopulator, "sumoDataServiceFactory", sumoDataServiceFactory);
        workbookPopulator.populateWorkbookWithData(reportConfig);
        File file = new File(reportConfig.getDestinationFile());
        Assert.assertTrue(file.exists());
        Workbook createdWorkbook = new XSSFWorkbook(file);
        assertEquals(2, createdWorkbook.getNumberOfSheets());
        assertEquals("ingest", createdWorkbook.getSheetName(0));
        assertEquals("ingest2", createdWorkbook.getSheetName(1));
        assertEquals(3, createdWorkbook.getSheet("ingest").getPhysicalNumberOfRows());
        assertEquals(3, createdWorkbook.getSheet("ingest2").getPhysicalNumberOfRows());
    }

    @Test
    public void testWithMoreThanOffsetData() throws Exception {
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/TestJSON_testWorkbookPopulation.json");
        ReflectionTestUtils.setField(workbookPopulator, "MAX_OFFSET", 1);
        workbookGenerator.generateWorkbookWithSheets(reportConfig);
        when(sumoDataServiceFactory.getSumoDataService(reportConfig)).thenReturn(sumoDataService);
        when(sumoDataService.executeSearchJob(any())).thenReturn("1234");
        GetSearchJobStatusResponse searchJobStatusResponse = new GetSearchJobStatusResponse();
        searchJobStatusResponse.setRecordCount(2);
        when(sumoDataService.pollSearchJobUntilComplete("1234")).thenReturn(searchJobStatusResponse);
        GetRecordsForSearchJobResponse recordsResponse = getSampleRecordsResponse();
        when(sumoDataService.getRecordsResponse("1234", 0, 1)).thenReturn(recordsResponse);
        ReflectionTestUtils.setField(workbookPopulator, "sumoDataServiceFactory", sumoDataServiceFactory);
        workbookPopulator.populateWorkbookWithData(reportConfig);
        File file = new File(reportConfig.getDestinationFile());
        Assert.assertTrue(file.exists());
        Workbook createdWorkbook = new XSSFWorkbook(file);
        assertEquals(2, createdWorkbook.getNumberOfSheets());
        assertEquals("ingest", createdWorkbook.getSheetName(0));
        assertEquals("ingest2", createdWorkbook.getSheetName(1));
        assertEquals(3, createdWorkbook.getSheet("ingest").getPhysicalNumberOfRows());
        assertEquals(3, createdWorkbook.getSheet("ingest2").getPhysicalNumberOfRows());
    }

    private GetRecordsForSearchJobResponse getSampleRecordsResponse() {
        GetRecordsForSearchJobResponse recordsResponse = new GetRecordsForSearchJobResponse();
        List<SearchJobField> fields = new ArrayList<>();
        SearchJobField field1 = new SearchJobField();
        field1.setName("column1");
        SearchJobField field2 = new SearchJobField();
        field2.setName("column2");
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