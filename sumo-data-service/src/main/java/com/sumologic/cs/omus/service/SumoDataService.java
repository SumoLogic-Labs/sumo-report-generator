package com.sumologic.cs.omus.service;

import com.sumologic.client.searchjob.model.GetRecordsForSearchJobResponse;
import com.sumologic.client.searchjob.model.GetSearchJobStatusResponse;
import com.sumologic.cs.omus.report.generator.api.SearchJob;

public interface SumoDataService {

    String executeSearchJob(SearchJob searchJob);

    GetSearchJobStatusResponse pollSearchJobUntilComplete(String searchJobId);

    GetRecordsForSearchJobResponse getRecordsResponse(String searchJobId, int start, int offset);

}
