package com.sumologic.cs.omus.commandline;

import com.sumologic.cs.omus.report.generator.api.ReportGenerator;
import com.sumologic.cs.omus.test.BaseOMUSTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/spring-test-context.xml")
public class OMUSCommandParserTest extends BaseOMUSTest {

    @Mock
    private ReportGenerator reportGenerator;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private OMUSCommandParser parser;

    @Test
    public void testLoggingOptions() {
        parser.parse(new String[] {"-d"});
        parser.parse(new String[] {"-t"});
    }

    @Test
    public void testInvalidOptions() {
        parser.parse(new String[] {"-foo"});
    }

    @Test
    public void testPrintHelp() {
        parser.parse(new String[] {"-h"});
    }

    @Test
    public void testValidConfig() throws Exception {
        ReflectionTestUtils.setField(parser, "reportGenerator", reportGenerator);
        doNothing().when(reportGenerator).generateReport(any());
        copyResourceToFolder("/testReportConfig/TestJSON_testParserSampleConfig.json", tempFolderPath + "/config.json");
        parser.parse(new String[]{"-c", tempFolderPath + "/config.json"});
    }

    @Test
    public void testInvalidConfig() throws Exception {
        ReflectionTestUtils.setField(parser, "reportGenerator", reportGenerator);
        doNothing().when(reportGenerator).generateReport(any());
        parser.parse(new String[]{"-c", tempFolderPath + "/config.json"});
    }

}