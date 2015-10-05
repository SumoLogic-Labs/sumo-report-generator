package com.sumologic.cs.omus.report.generator.excel;

import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.report.generator.api.ReportGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ReportGenerationIT extends BaseExcelTest {

    @Autowired
    private ReportGenerator reportGenerator;

    @Test
    public void test() throws Exception {
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/TestJSON_IT.json");
        reportGenerator.generateReport(reportConfig);
    }

    @Test
    public void testWithReplacements() throws Exception {
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/TestJSON_IT.json");
        reportGenerator.generateReport(reportConfig);
    }

}