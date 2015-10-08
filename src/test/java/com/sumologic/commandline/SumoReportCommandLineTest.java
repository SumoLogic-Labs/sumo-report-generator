package com.sumologic.commandline;

import com.sumologic.BaseSumoReportGeneratorTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SumoReportCommandLineTest extends BaseSumoReportGeneratorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testNoErrorOccursOnHelp() {
        SumoReportCommandLine.main(new String[]{"h"});
    }

}