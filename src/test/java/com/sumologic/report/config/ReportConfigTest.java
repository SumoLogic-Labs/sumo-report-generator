package com.sumologic.report.config;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ReportConfigTest {

    @Test
    public void testToStringDoesNotContainPasswordField() throws Exception {
        ReportConfig reportConfig = new ReportConfig();
        assertTrue(!reportConfig.toString().contains("password"));
    }

}