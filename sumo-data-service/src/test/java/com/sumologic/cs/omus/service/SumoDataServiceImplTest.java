package com.sumologic.cs.omus.service;

import com.sumologic.client.SumoLogicClient;
import com.sumologic.client.searchjob.model.GetRecordsForSearchJobResponse;
import com.sumologic.client.searchjob.model.GetSearchJobStatusResponse;
import com.sumologic.cs.omus.report.generator.api.PropertyReplacementConfig;
import com.sumologic.cs.omus.report.generator.api.SearchJob;
import com.sumologic.cs.omus.test.BaseOMUSTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SumoDataServiceImplTest extends BaseOMUSTest {

    private SumoDataService sumoDataService;

    @Mock
    private SearchJob searchJob;

    @Mock
    private SumoLogicClient client;

    @Mock
    private PropertyReplacementConfig propertyReplacementConfig;

    @Before
    public void setUp() {
        super.setUp();
        sumoDataService = new SumoDataServiceImpl(client, propertyReplacementConfig);
    }

    @After
    public void tearDown() {
        Mockito.reset(client, propertyReplacementConfig);
    }

    @Test
    public void testExecuteSearchJobWithReplacements() {
        Properties properties = new Properties();
        properties.put("foo", "bar");
        when(propertyReplacementConfig.toProperties()).thenReturn(properties);
        when(client.createSearchJob(anyString(), anyString(), anyString(), anyString())).thenReturn("1234");
        when(searchJob.getQuery()).thenReturn("${foo}");
        when(searchJob.getFrom()).thenReturn("");
        when(searchJob.getTo()).thenReturn("");
        when(searchJob.getTimezone()).thenReturn("");
        sumoDataService.executeSearchJob(searchJob);
        verify(searchJob, times(2)).getQuery();
    }

    @Test
    public void testPollSearchJobUntilComplete() {
        GetSearchJobStatusResponse firstResponse = new GetSearchJobStatusResponse();
        firstResponse.setState("GATHERING RESULTS");
        GetSearchJobStatusResponse secondResponse = new GetSearchJobStatusResponse();
        secondResponse.setState("DONE GATHERING RESULTS");
        when(client.getSearchJobStatus(anyString())).thenReturn(firstResponse).thenReturn(secondResponse);
        GetSearchJobStatusResponse response = sumoDataService.pollSearchJobUntilComplete("1234");
        verify(client, times(2)).getSearchJobStatus(anyString());
        assertEquals("DONE GATHERING RESULTS", response.getState());
    }

    @Test
    public void testGetRecordsResponse() {
        when(client.getRecordsForSearchJob(anyString(), anyInt(), anyInt())).thenReturn(new GetRecordsForSearchJobResponse());
        sumoDataService.getRecordsResponse("1234", 1, 10000);
    }

    @Test
    public void testThreadInterrupt() {
        ReflectionTestUtils.setField(sumoDataService, "POLL_INTERVAL", 500);
        GetSearchJobStatusResponse firstResponse = new GetSearchJobStatusResponse();
        firstResponse.setState("GATHERING RESULTS");
        GetSearchJobStatusResponse secondResponse = new GetSearchJobStatusResponse();
        secondResponse.setState("DONE GATHERING RESULTS");
        when(client.getSearchJobStatus(anyString())).thenReturn(firstResponse).thenReturn(secondResponse);
        Thread.currentThread().interrupt();
        GetSearchJobStatusResponse response = sumoDataService.pollSearchJobUntilComplete("1234");
        verify(client, times(2)).getSearchJobStatus(anyString());
        assertEquals("DONE GATHERING RESULTS", response.getState());
    }

    @Test
    public void testNoReplacementConfig() {
        ReflectionTestUtils.setField(sumoDataService, "propertyReplacementConfig", null);
        when(client.createSearchJob(anyString(), anyString(), anyString(), anyString())).thenReturn("1234");
        SearchJob searchJob = new SearchJob();
        searchJob.setQuery("");
        searchJob.setFrom("");
        searchJob.setTo("");
        searchJob.setTimezone("");
        assertEquals("1234", sumoDataService.executeSearchJob(searchJob));
    }

}