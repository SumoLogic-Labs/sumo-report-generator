package com.sumologic.cs.omus.report.generator.impl;

import com.sumologic.cs.omus.report.generator.api.OmusReportGenerationException;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.report.generator.api.ReportSheet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ExcelWorkbookGenerator implements WorkbookGenerator {

    private static final Log LOGGER = LogFactory.getLog(ExcelWorkbookGenerator.class);

    @Override
    public void generateWorkbookWithSheets(ReportConfig reportConfig) throws OmusReportGenerationException {
        try {
            openAndGenerateWorkbook(reportConfig);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new OmusReportGenerationException("unable to generate workbook!");
        }
    }

    private void openAndGenerateWorkbook(ReportConfig reportConfig) throws IOException {
        LOGGER.debug("creating empty workbook with sheets");
        Workbook workbook = new XSSFWorkbook();
        FileOutputStream fileOut = new FileOutputStream(reportConfig.getDestinationFile());
        for (ReportSheet sheet : reportConfig.getReportSheets()) {
            String safeName = getSafeSheetName(sheet.getSheetName());
            LOGGER.debug("creating sheet " + safeName);
            workbook.createSheet(safeName);
        }
        workbook.write(fileOut);
        fileOut.close();
        LOGGER.debug("workbook created");
    }

    private String getSafeSheetName(String sheetName) {
        String safeSheetName = WorkbookUtil.createSafeSheetName(sheetName);
        if (!safeSheetName.equals(sheetName)) {
            LOGGER.warn("Sheet name " + sheetName + " contains invalid characters, renaming to " + safeSheetName);
        }
        return safeSheetName;
    }

}
