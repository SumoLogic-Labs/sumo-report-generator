package com.sumologic.cs.omus.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.service.SumoDataServiceFactory;
import com.sumologic.cs.omus.test.BaseOMUSTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/spring-test-context.xml")
public class SumoDataServiceFactoryTest extends BaseOMUSTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private SumoDataServiceFactory factory;

    @Test(expected = MalformedURLException.class)
    public void testMalformedURL() throws Exception {
        ReportConfig config = getReportConfigFromResource("/testReportConfig/TestJSON_testSumoDataServiceFactory.json");
        factory.getSumoDataService(config);
    }

    @Test
    public void testValidURL() throws Exception {
        ReportConfig config = getReportConfigFromResource("/testReportConfig/TestJSON_testSumoDataServiceFactory.json");
        config.setUrl("http://www.google.com");
        factory.getSumoDataService(config);
    }

    protected ReportConfig getReportConfigFromResource(String resourcePath) throws IOException {
        copyResourceToFolder(resourcePath, tempFolderPath + "/config.json");
        ReportConfig reportConfig = mapper.readValue(new File(tempFolderPath + "/config.json"), ReportConfig.class);
        reportConfig.setDestinationFile(tempFolderPath + "/workbook.xlsx");
        return reportConfig;
    }



}