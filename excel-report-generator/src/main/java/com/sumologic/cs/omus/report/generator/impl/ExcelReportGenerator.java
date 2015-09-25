package com.sumologic.cs.omus.report.generator.impl;

import com.sumologic.cs.omus.report.generator.api.OmusReportGenerationException;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.report.generator.api.ReportGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcelReportGenerator implements ReportGenerator {

    private static final Log LOGGER = LogFactory.getLog(ExcelReportGenerator.class);

    @Autowired
    private WorkbookGenerator workbookGenerator;

    @Autowired
    private WorkbookPopulator workbookPopulator;

    @Override
    public void generateReport(ReportConfig reportConfig) throws OmusReportGenerationException {
            LOGGER.info("starting report generation");
            LOGGER.debug("using config: " + reportConfig);
            workbookGenerator.generateWorkbookWithSheets(reportConfig);
            workbookPopulator.populateWorkbookWithData(reportConfig);
            LOGGER.info("report successfully generated");
    }

}