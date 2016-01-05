package com.sumologic.report.generator.excel;

import com.sumologic.report.config.ReportConfig;
import com.sumologic.report.generator.ReportGenerationException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class ExcelWorkbookPopulatorTest extends BaseExcelTest {

    @Autowired
    private WorkbookGenerator workbookGenerator;

    @Autowired
    private WorkbookPopulator workbookPopulator;

    @Mock
    @Autowired
    private WorksheetPopulator worksheetPopulator;

    @Test (expected = ReportGenerationException.class)
    public void testIOException() throws Exception {
        ReportConfig reportConfig = mock(ReportConfig.class);
        Workbook workbook = new XSSFWorkbook();
        doThrow(IOException.class).when(reportConfig).getDestinationFile();
        workbookPopulator.populateWorkbookWithData(reportConfig, workbook);
    }

    @Test
    public void testWorkbookPopulation() throws Exception {
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/testWorkbookPopulation.json");
        ReflectionTestUtils.setField(workbookPopulator, "worksheetPopulator", worksheetPopulator);
        doNothing().when(worksheetPopulator).populateSheetWithData(any());
        Workbook workbook = workbookGenerator.generateWorkbook(reportConfig);
        workbookPopulator.populateWorkbookWithData(reportConfig, workbook);
    }

}