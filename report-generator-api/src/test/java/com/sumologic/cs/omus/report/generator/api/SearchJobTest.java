package com.sumologic.cs.omus.report.generator.api;

import org.junit.Test;

import static org.junit.Assert.*;

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