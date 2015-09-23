package com.sumologic.cs.omus.report.generator.impl;

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

    private final Log logger = LogFactory.getLog(ExcelWorkbookGenerator.class);

    @Override
    public void generateWorkbookWithSheets(ReportConfig reportConfig) throws IOException {
        logger.debug("creating empty workbook with sheets");
        Workbook workbook = new XSSFWorkbook();
        FileOutputStream fileOut = new FileOutputStream(reportConfig.getDestinationFile());
        for (ReportSheet sheet : reportConfig.getReportSheets()) {
            String safeName = getSafeSheetName(sheet.getSheetName());
            logger.debug("creating sheet " + safeName);
            workbook.createSheet(safeName);
        }
        workbook.write(fileOut);
        fileOut.close();
        logger.debug("workbook created");
    }

    private String getSafeSheetName(String sheetName) {
        String safeSheetName = WorkbookUtil.createSafeSheetName(sheetName);
        if (!safeSheetName.equals(sheetName)) {
            logger.warn("Sheet name " + sheetName + " contains invalid characters, renaming to " + safeSheetName);
        }
        return safeSheetName;
    }

}
