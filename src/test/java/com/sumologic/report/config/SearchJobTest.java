package com.sumologic.report.config;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SearchJobTest {

    @Test
    public void testToString() throws Exception {
        SearchJob searchJob = new SearchJob();
        searchJob.setTimezone("PST");
        searchJob.setTo("9/2/2015");
        searchJob.setFrom("9/1/2015");
        searchJob.setQuery("error");
        assertNotNull(searchJob.toString());
    }

}