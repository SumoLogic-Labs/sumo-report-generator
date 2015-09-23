package com.sumologic.cs.omus.report.generator.impl;

import com.sumologic.client.SumoLogicClient;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.service.SumoDataService;
import com.sumologic.cs.omus.service.SumoDataServiceImpl;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

@Service
public class SumoDataServiceFactory {

    public SumoDataService getSumoDataService(ReportConfig reportConfig) throws MalformedURLException {
        SumoLogicClient sumoLogicClient = new SumoLogicClient(reportConfig.getUsername(), reportConfig.getPassword());
        sumoLogicClient.setURL(reportConfig.getUrl());
        return new SumoDataServiceImpl(sumoLogicClient, reportConfig.getPropertyReplacementConfig());
    }

}