package com.sumologic.cs.omus.report.generator.api;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReportConfigTest {

    @Test
    public void testToStringDoesNotContainPasswordField() throws Exception {
        ReportConfig reportConfig = new ReportConfig();
        assertTrue(!reportConfig.toString().contains("password"));
    }

}