package com.sumologic.cs.omus.report.generator.excel;

import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.report.generator.api.ReportGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

public class ReportGenerationIT extends BaseExcelTest {

    @Autowired
    private ReportGenerator reportGenerator;

    @Autowired
    private WorksheetPopulator worksheetPopulator;

    @Autowired
    private WorkbookPopulator workbookPopulator;

    @Test
    public void testWithoutIteration() throws Exception {
        ReflectionTestUtils.setField(worksheetPopulator, "MAX_OFFSET", 10000);
        ReflectionTestUtils.setField(workbookPopulator, "worksheetPopulator", worksheetPopulator);
        ReflectionTestUtils.setField(reportGenerator, "workbookPopulator", workbookPopulator);
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/IT.json");
        reportGenerator.generateReport(reportConfig);
    }

    @Test
    public void testWithIteration() throws Exception {
        ReflectionTestUtils.setField(worksheetPopulator, "MAX_OFFSET", 100);
        ReflectionTestUtils.setField(workbookPopulator, "worksheetPopulator", worksheetPopulator);
        ReflectionTestUtils.setField(reportGenerator, "workbookPopulator", workbookPopulator);
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/IT.json");
        reportGenerator.generateReport(reportConfig);
    }

}