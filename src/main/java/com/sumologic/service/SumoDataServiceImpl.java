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

import com.sumologic.client.SumoLogicClient;
import com.sumologic.client.searchjob.model.GetRecordsForSearchJobResponse;
import com.sumologic.client.searchjob.model.GetSearchJobStatusResponse;
import com.sumologic.report.config.PropertyReplacementConfig;
import com.sumologic.report.config.SearchJob;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Properties;

public class SumoDataServiceImpl implements SumoDataService {

    private static final Log LOGGER = LogFactory.getLog(SumoDataServiceImpl.class);
    private static int POLL_INTERVAL = 5000;

    private SumoLogicClient client;
    private PropertyReplacementConfig propertyReplacementConfig;

    public SumoDataServiceImpl(SumoLogicClient client, PropertyReplacementConfig propertyReplacementConfig) {
        this.client = client;
        this.propertyReplacementConfig = propertyReplacementConfig;
    }

    @Override
    public String executeSearchJob(SearchJob searchJob) {
        SearchJob processedJob = processReplacementProperties(searchJob, propertyReplacementConfig);
        LOGGER.info("executing search job: " + searchJob.toString());
        return client.createSearchJob(
                processedJob.getQuery(), processedJob.getFrom(), processedJob.getTo(), processedJob.getTimezone());
    }

    @Override
    public GetSearchJobStatusResponse pollSearchJobUntilComplete(String searchJobId, String jobName) {
        GetSearchJobStatusResponse response = client.getSearchJobStatus(searchJobId);
        while ("GATHERING RESULTS".equals(response.getState())) {
            LOGGER.info("checking search job status for sheet " + jobName);
            LOGGER.info("search job state is " + response.getState() + ", will recheck in 5 seconds");
            try {
                Thread.sleep(POLL_INTERVAL);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
            response = client.getSearchJobStatus(searchJobId);
        }
        return response;
    }

    @Override
    public GetRecordsForSearchJobResponse getRecordsResponse(String searchJobId, int start, int offset) {
        return client.getRecordsForSearchJob(searchJobId, start, offset);
    }

    private SearchJob processReplacementProperties(SearchJob searchJob, PropertyReplacementConfig replacementConfig) {
        if (replacementConfig != null) {
            searchJob.setQuery(replacePlaceholders(searchJob.getQuery(), replacementConfig.toProperties()));
            searchJob.setFrom(replacePlaceholders(searchJob.getFrom(), replacementConfig.toProperties()));
            searchJob.setTo(replacePlaceholders(searchJob.getTo(), replacementConfig.toProperties()));
            searchJob.setTimezone(replacePlaceholders(searchJob.getTimezone(), replacementConfig.toProperties()));
            return searchJob;
        } else {
            return searchJob;
        }
    }

    private String replacePlaceholders(String value, Properties properties) {
        if (value.contains("$")) {
            LOGGER.debug("replacing placeholders in " + value);
            PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}");
            String newValue = helper.replacePlaceholders(value, properties);
            LOGGER.debug("after replacement: " + newValue);
            return newValue;
        } else {
            return value;
        }
    }

}