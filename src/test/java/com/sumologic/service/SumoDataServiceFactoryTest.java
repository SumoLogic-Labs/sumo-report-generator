package com.sumologic.service;

import com.sumologic.BaseSumoReportGeneratorTest;
import com.sumologic.report.config.ReportConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.net.MalformedURLException;

import static org.mockito.Mockito.when;

public class SumoDataServiceFactoryTest extends BaseSumoReportGeneratorTest {

    @Mock
    private ReportConfig reportConfig;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private SumoDataServiceFactory factory = new SumoDataServiceFactory();

    @Test(expected = MalformedURLException.class)
    public void testMalformedURL() throws Exception {
        when(reportConfig.getUrl()).thenReturn("bad_url");
        factory.getSumoDataService(reportConfig);
    }

    @Test
    public void testValidURL() throws Exception {
        when(reportConfig.getUrl()).thenReturn("http://www.google.com");
        factory.getSumoDataService(reportConfig);
    }

}