/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.sumologic.service;

import com.sumologic.BaseSumoReportGeneratorTest;
import com.sumologic.client.SumoLogicClient;
import com.sumologic.client.searchjob.model.GetRecordsForSearchJobResponse;
import com.sumologic.client.searchjob.model.GetSearchJobStatusResponse;
import com.sumologic.report.config.PropertyReplacementConfig;
import com.sumologic.report.config.SearchJob;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SumoDataServiceImplTest extends BaseSumoReportGeneratorTest {

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
        GetSearchJobStatusResponse response = sumoDataService.pollSearchJobUntilComplete("1234", "test");
        verify(client, times(2)).getSearchJobStatus(anyString());
        Assert.assertEquals("DONE GATHERING RESULTS", response.getState());
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
        GetSearchJobStatusResponse response = sumoDataService.pollSearchJobUntilComplete("1234", "test");
        verify(client, times(2)).getSearchJobStatus(anyString());
        Assert.assertEquals("DONE GATHERING RESULTS", response.getState());
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