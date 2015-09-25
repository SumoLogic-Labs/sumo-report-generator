package com.sumologic.cs.omus.report.generator.impl;

import com.sumologic.cs.omus.report.generator.api.OmusReportGenerationException;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class ExcelWorkbookGeneratorTest extends BaseExcelTest {

    @Autowired
    private WorkbookGenerator workbookGenerator;

    @Test
    public void testWorkbookWithSheetsCreation() throws Exception {
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/TestJSON_testWorkbookWithSheetsCreation.json");
        workbookGenerator.generateWorkbookWithSheets(reportConfig);
        File file = new File(reportConfig.getDestinationFile());
        Assert.assertTrue(file.exists());
        Workbook createdWorkbook = new XSSFWorkbook(file);
        assertEquals(2, createdWorkbook.getNumberOfSheets());
        assertEquals("sheet1", createdWorkbook.getSheetName(0));
        assertEquals("sheet2", createdWorkbook.getSheetName(1));
    }

    @Test(expected = OmusReportGenerationException.class)
    public void testWorkbookWithSheetsCreationException() throws Exception {
        ReportConfig reportConfig = mock(ReportConfig.class);
        doThrow(IOException.class).when(reportConfig).getDestinationFile();
        workbookGenerator.generateWorkbookWithSheets(reportConfig);
    }

    @Test
    public void testWorkbookCreationWithInvalidSheetName() throws Exception {
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/TestJSON_testWorkbookCreationWithInvalidSheetName.json");
        workbookGenerator.generateWorkbookWithSheets(reportConfig);
        File file = new File(reportConfig.getDestinationFile());
        Assert.assertTrue(file.exists());
        Workbook createdWorkbook = new XSSFWorkbook(file);
        assertEquals(1, createdWorkbook.getNumberOfSheets());
        assertEquals("sheet       ", createdWorkbook.getSheetName(0));
    }

}