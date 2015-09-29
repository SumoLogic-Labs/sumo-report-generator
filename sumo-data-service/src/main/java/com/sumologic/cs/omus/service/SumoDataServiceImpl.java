package com.sumologic.cs.omus.service;

import com.sumologic.client.SumoLogicClient;
import com.sumologic.client.searchjob.model.GetRecordsForSearchJobResponse;
import com.sumologic.client.searchjob.model.GetSearchJobStatusResponse;
import com.sumologic.cs.omus.report.generator.api.PropertyReplacementConfig;
import com.sumologic.cs.omus.report.generator.api.SearchJob;
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
        LOGGER.info("executing search job: " + searchJob.toString());
        SearchJob processedSearchJob = processReplacementProperties(searchJob, propertyReplacementConfig);
        return client.createSearchJob(
                processedSearchJob.getQuery(), processedSearchJob.getFrom(), processedSearchJob.getTo(), processedSearchJob.getTimezone());
    }

    @Override
    public GetSearchJobStatusResponse pollSearchJobUntilComplete(String searchJobId) {
        GetSearchJobStatusResponse response = client.getSearchJobStatus(searchJobId);
        while ("GATHERING RESULTS".equals(response.getState())) {
            LOGGER.info("checking search job status");
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