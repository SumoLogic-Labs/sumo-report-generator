package com.sumologic.cs.omus.report.generator.impl;

import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.service.SumoDataServiceFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.MalformedURLException;

public class SumoDataServiceFactoryTest extends BaseExcelTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private SumoDataServiceFactory factory = new SumoDataServiceFactory();

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


}