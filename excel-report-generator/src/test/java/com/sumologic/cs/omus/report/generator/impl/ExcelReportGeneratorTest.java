package com.sumologic.cs.omus.report.generator.impl;

import com.sumologic.cs.omus.report.generator.api.OmusReportGenerationException;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.report.generator.api.ReportGenerator;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

public class ExcelReportGeneratorTest extends BaseExcelTest {

    @Autowired
    private ReportGenerator reportGenerator;

    @Mock
    private WorkbookGenerator workbookGenerator;

    @Mock
    private WorkbookPopulator workbookPopulator;

    @After
    public void tearDown() {
        Mockito.reset(workbookGenerator, workbookPopulator);
    }

    @Test(expected = OmusReportGenerationException.class)
    public void testException() throws Exception {
        ReportConfig reportConfig =
                getReportConfigFromResource("/testReportConfig/TestJSON_testWorkbookWithSheetsCreation.json");
        doThrow(new IOException()).when(workbookGenerator).generateWorkbookWithSheets(reportConfig);
        ReflectionTestUtils.setField(reportGenerator, "workbookGenerator", workbookGenerator);
        reportGenerator.generateReport(reportConfig);
    }

    @Test
    public void testSuccess() throws Exception {
        ReportConfig reportConfig =
                getReportConfigFromResource("/testReportConfig/TestJSON_testWorkbookWithSheetsCreation.json");
        doNothing().when(workbookGenerator).generateWorkbookWithSheets(reportConfig);
        doNothing().when(workbookPopulator).populateWorkbookWithData(reportConfig);
        ReflectionTestUtils.setField(reportGenerator, "workbookPopulator", workbookPopulator);
        reportGenerator.generateReport(reportConfig);
    }

}