package com.sumologic.cs.omus.report.generator.excel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.test.BaseOMUSTest;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/spring-test-context.xml")
public abstract class BaseExcelTest extends BaseOMUSTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    protected ReportConfig getReportConfigFromResource(String resourcePath) throws IOException {
        copyResourceToFolder(resourcePath, tempFolderPath + "/config.json");
        ReportConfig reportConfig = mapper.readValue(new File(tempFolderPath + "/config.json"), ReportConfig.class);
        reportConfig.setDestinationFile(tempFolderPath + "/workbook.xlsx");
        return reportConfig;
    }



}
