package com.sumologic.cs.omus.report.generator.excel;

import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.report.generator.api.ReportGenerator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

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
    public void testSuccess() throws Exception {
        ReportConfig reportConfig =
                getReportConfigFromResource("/testReportConfig/testWorkbookWithSheetsCreation.json");
        Workbook workbook = new XSSFWorkbook();
        when(workbookGenerator.generateWorkbook(reportConfig)).thenReturn(workbook);
        doNothing().when(workbookPopulator).populateWorkbookWithData(reportConfig, workbook);
        ReflectionTestUtils.setField(reportGenerator, "workbookPopulator", workbookPopulator);
        reportGenerator.generateReport(reportConfig);
    }

}