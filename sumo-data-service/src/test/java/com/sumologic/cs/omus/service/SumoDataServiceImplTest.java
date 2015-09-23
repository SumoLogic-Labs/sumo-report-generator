package com.sumologic.cs.omus.service;

import com.sumologic.client.SumoLogicClient;
import com.sumologic.client.searchjob.model.GetRecordsForSearchJobResponse;
import com.sumologic.client.searchjob.model.GetSearchJobStatusResponse;
import com.sumologic.cs.omus.report.generator.api.PropertyReplacementConfig;
import com.sumologic.cs.omus.report.generator.api.SearchJob;
import com.sumologic.cs.omus.test.BaseOMUSTest;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SumoDataServiceImplTest extends BaseOMUSTest {

    private SumoDataService sumoDataService;

    @Mock
    private SumoLogicClient client;

    @Mock
    private PropertyReplacementConfig propertyReplacementConfig;

    @Mock
    private Log logger;

    @Before
    public void setUp() {
        super.setUp();
        sumoDataService = new SumoDataServiceImpl(client, propertyReplacementConfig);
    }

    @After
    public void tearDown() {
        Mockito.reset(client, propertyReplacementConfig, logger);
    }

    @Test
    public void testExecuteSearchJob() {
        when(client.createSearchJob(anyString(), anyString(), anyString(), anyString())).thenReturn("1234");
        SearchJob searchJob = new SearchJob();
        searchJob.setQuery("");
        searchJob.setFrom("");
        searchJob.setTo("");
        searchJob.setTimezone("");
        assertEquals("1234", sumoDataService.executeSearchJob(searchJob));
    }

    @Test
    public void testPollSearchJobUntilComplete() {
        ReflectionTestUtils.setField(sumoDataService, "logger", logger);
        GetSearchJobStatusResponse firstResponse = new GetSearchJobStatusResponse();
        firstResponse.setState("GATHERING RESULTS");
        GetSearchJobStatusResponse secondResponse = new GetSearchJobStatusResponse();
        secondResponse.setState("DONE GATHERING RESULTS");
        when(client.getSearchJobStatus(anyString())).thenReturn(firstResponse).thenReturn(secondResponse);
        GetSearchJobStatusResponse response = sumoDataService.pollSearchJobUntilComplete("1234");
        verify(logger).info("checking search job status");
        verify(logger).info("search job state is GATHERING RESULTS, will recheck in 5 seconds");
        assertEquals("DONE GATHERING RESULTS", response.getState());
    }

    @Test
    public void testGetRecordsResponse() {
        when(client.getRecordsForSearchJob(anyString(), anyInt(), anyInt())).thenReturn(new GetRecordsForSearchJobResponse());
        sumoDataService.getRecordsResponse("1234", 1, 10000);
    }

    @Test
    public void testThreadInterrupt() {
        ReflectionTestUtils.setField(sumoDataService, "logger", logger);
        GetSearchJobStatusResponse firstResponse = new GetSearchJobStatusResponse();
        firstResponse.setState("GATHERING RESULTS");
        GetSearchJobStatusResponse secondResponse = new GetSearchJobStatusResponse();
        secondResponse.setState("DONE GATHERING RESULTS");
        when(client.getSearchJobStatus(anyString())).thenReturn(firstResponse).thenReturn(secondResponse);
        Thread.currentThread().interrupt();
        GetSearchJobStatusResponse response = sumoDataService.pollSearchJobUntilComplete("1234");
        verify(logger).error(anyString());
        assertEquals("DONE GATHERING RESULTS", response.getState());
    }

}