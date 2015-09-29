package com.sumologic.cs.omus.report.generator.excel;

import com.sumologic.cs.omus.report.generator.api.OmusReportGenerationException;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.report.generator.api.ReportSheet;
import com.sumologic.cs.omus.service.SumoDataService;
import com.sumologic.cs.omus.service.SumoDataServiceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.WorkbookUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ExcelWorkbookPopulator implements WorkbookPopulator {

    private static final Log LOGGER = LogFactory.getLog(ExcelWorkbookPopulator.class);

    @Autowired
    private SumoDataServiceFactory sumoDataServiceFactory;

    @Autowired
    private WorksheetPopulator worksheetPopulator;

    @Override
    public void populateWorkbookWithData(ReportConfig reportConfig) throws OmusReportGenerationException {
        try {
            openWorkbookAndProcessSheets(reportConfig);
        } catch (IOException | InvalidFormatException e) {
            LOGGER.error(e);
            throw new OmusReportGenerationException("unable to populate workbook!");
        }
    }

    private void openWorkbookAndProcessSheets(ReportConfig reportConfig) throws IOException, InvalidFormatException {
        LOGGER.debug("populating workbook");
        File file = new File(reportConfig.getDestinationFile());
        FileInputStream fileInputStream = new FileInputStream(file);
        OPCPackage opcPackage = OPCPackage.open(fileInputStream);
        Workbook workbook = WorkbookFactory.create(opcPackage);
        SumoDataService sumoDataService = sumoDataServiceFactory.getSumoDataService(reportConfig);
        for (ReportSheet reportSheet : reportConfig.getReportSheets()) {
            LOGGER.info("populating sheet " + reportSheet.getSheetName());
            Sheet workbookSheet = workbook.getSheet(WorkbookUtil.createSafeSheetName(reportSheet.getSheetName()));
            worksheetPopulator.populateSheetWithData(workbookSheet, reportSheet, sumoDataService);
        }
        FileOutputStream fileOut = new FileOutputStream(file);
        workbook.write(fileOut);
        fileInputStream.close();
        opcPackage.close();
        fileOut.close();
        LOGGER.debug("workbook populated");
    }

}