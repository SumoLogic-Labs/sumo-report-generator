package com.sumologic.service;

import com.sumologic.client.SumoLogicClient;
import com.sumologic.report.config.ReportConfig;
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