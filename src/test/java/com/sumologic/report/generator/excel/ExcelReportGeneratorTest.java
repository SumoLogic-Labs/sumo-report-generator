package com.sumologic.report.generator.excel;

import com.sumologic.report.config.ReportConfig;
import com.sumologic.report.generator.ReportGenerator;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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

    @Test
    public void testSuccessWithoutTemplate() throws Exception {
        ReportConfig reportConfig =
                getReportConfigFromResource("/testReportConfig/testWorkbookWithSheetsCreation.json");
        Workbook workbook = new XSSFWorkbook();
        when(workbookGenerator.generateWorkbook(reportConfig)).thenReturn(workbook);
        doNothing().when(workbookPopulator).populateWorkbookWithData(reportConfig, workbook);
        ReflectionTestUtils.setField(reportGenerator, "workbookPopulator", workbookPopulator);
        reportGenerator.generateReport(reportConfig);
    }

    @Test
    public void testSuccessWithTemplate() throws Exception {
        copyResourceToFolder("/template.xlsx", tempFolderPath + "/template.xlsx");
        ReportConfig reportConfig =
                getReportConfigFromResource("/testReportConfig/testWorkbookWithSheetsCreation.json");
        reportConfig.setTemplateFile(tempFolderPath + "/template.xlsx");
        Workbook workbook = new XSSFWorkbook();
        when(workbookGenerator.generateWorkbook(reportConfig)).thenReturn(workbook);
        doNothing().when(workbookPopulator).populateWorkbookWithData(reportConfig, workbook);
        ReflectionTestUtils.setField(reportGenerator, "workbookPopulator", workbookPopulator);
        reportGenerator.generateReport(reportConfig);
    }

    @Test
    public void testSuccessWithAppend() throws Exception {
        Workbook wb = new XSSFWorkbook();
        FileOutputStream fileOut = new FileOutputStream(tempFolderPath + "/workbook.xlsx");
        wb.write(fileOut);
        fileOut.close();
        ReportConfig reportConfig =
                getReportConfigFromResource("/testReportConfig/testWorkbookWithSheetsCreation.json");
        reportConfig.setAppendToDestination(true);
        Workbook workbook = new XSSFWorkbook();
        when(workbookGenerator.generateWorkbook(reportConfig)).thenReturn(workbook);
        doNothing().when(workbookPopulator).populateWorkbookWithData(reportConfig, workbook);
        ReflectionTestUtils.setField(reportGenerator, "workbookPopulator", workbookPopulator);
        reportGenerator.generateReport(reportConfig);
    }

}