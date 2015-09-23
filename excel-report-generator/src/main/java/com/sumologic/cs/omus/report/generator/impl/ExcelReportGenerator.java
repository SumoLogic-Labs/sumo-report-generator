package com.sumologic.cs.omus.report.generator.impl;

import com.sumologic.cs.omus.report.generator.api.OmusReportGenerationException;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.report.generator.api.ReportGenerator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ExcelReportGenerator implements ReportGenerator {

    private final Log logger = LogFactory.getLog(ExcelReportGenerator.class);

    @Autowired
    private WorkbookGenerator workbookGenerator;

    @Autowired
    private WorkbookPopulator workbookPopulator;

    @Override
    public void generateReport(ReportConfig reportConfig) throws OmusReportGenerationException {
        try {
            logger.info("starting report generation");
            logger.debug("using config: " + reportConfig);
            workbookGenerator.generateWorkbookWithSheets(reportConfig);
            workbookPopulator.populateWorkbookWithData(reportConfig);
            logger.info("report successfully generated");
        } catch (IOException | InvalidFormatException e) {
            throw new OmusReportGenerationException(e.getMessage());
        }
    }

}