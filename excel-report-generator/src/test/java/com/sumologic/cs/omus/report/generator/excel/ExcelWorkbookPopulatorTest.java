package com.sumologic.cs.omus.report.generator.excel;

import com.sumologic.cs.omus.report.generator.api.OmusReportGenerationException;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import junit.framework.Assert;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class ExcelWorkbookPopulatorTest extends BaseExcelTest {

    @Autowired
    private WorkbookGenerator workbookGenerator;

    @Autowired
    private WorkbookPopulator workbookPopulator;

    @Mock
    @Autowired
    private WorksheetPopulator worksheetPopulator;

    @Test (expected = OmusReportGenerationException.class)
    public void testIOException() throws Exception {
        ReportConfig reportConfig = mock(ReportConfig.class);
        doThrow(IOException.class).when(reportConfig).getDestinationFile();
        workbookPopulator.populateWorkbookWithData(reportConfig);
    }

    @Test (expected = OmusReportGenerationException.class)
    public void testInvalidFormatException() throws Exception {
        ReportConfig reportConfig = mock(ReportConfig.class);
        doThrow(InvalidFormatException.class).when(reportConfig).getDestinationFile();
        workbookPopulator.populateWorkbookWithData(reportConfig);
    }

    @Test
    public void testWorkbookPopulation() throws Exception {
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/TestJSON_testWorkbookPopulation.json");
        ReflectionTestUtils.setField(workbookPopulator, "worksheetPopulator", worksheetPopulator);
        doNothing().when(worksheetPopulator).populateSheetWithData(any(), any(), any());
        workbookGenerator.generateWorkbook(reportConfig);
        workbookPopulator.populateWorkbookWithData(reportConfig);
    }

}