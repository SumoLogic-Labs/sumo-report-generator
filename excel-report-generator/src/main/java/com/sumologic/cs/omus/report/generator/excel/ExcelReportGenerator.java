package com.sumologic.cs.omus.report.generator.excel;

import com.sumologic.cs.omus.report.generator.api.OmusReportGenerationException;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.report.generator.api.ReportGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ExcelReportGenerator implements ReportGenerator {

    private static final Log LOGGER = LogFactory.getLog(ExcelReportGenerator.class);

    @Autowired
    private WorkbookGenerator workbookGenerator;

    @Autowired
    private WorkbookPopulator workbookPopulator;

    @Override
    public void generateReport(ReportConfig reportConfig) throws OmusReportGenerationException {
        try {
            LOGGER.info("starting report generation");
            LOGGER.debug("using config: " + reportConfig);
            Workbook workbook = workbookGenerator.generateWorkbook(reportConfig);
            workbookPopulator.populateWorkbookWithData(reportConfig, workbook);
            LOGGER.info("report successfully generated");
        } catch (IOException e) {
            throw new OmusReportGenerationException(e);
        }
    }

}