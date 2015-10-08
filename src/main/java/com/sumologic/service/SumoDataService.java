package com.sumologic.service;

import com.sumologic.client.searchjob.model.GetRecordsForSearchJobResponse;
import com.sumologic.client.searchjob.model.GetSearchJobStatusResponse;
import com.sumologic.report.config.SearchJob;

public interface SumoDataService {

    String executeSearchJob(SearchJob searchJob);

    GetSearchJobStatusResponse pollSearchJobUntilComplete(String searchJobId);

    GetRecordsForSearchJobResponse getRecordsResponse(String searchJobId, int start, int offset);

}
