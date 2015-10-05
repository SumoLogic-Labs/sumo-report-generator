package com.sumologic.cs.omus.report.generator.excel;

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
    public Workbook generateWorkbook(ReportConfig reportConfig) throws IOException {
        return generateWorkbookWithSheets(reportConfig);
    }

    private Workbook generateWorkbookWithSheets(ReportConfig reportConfig) throws IOException {
        LOGGER.debug("creating empty workbook with sheets");
        Workbook workbook = new XSSFWorkbook();
        FileOutputStream fileOut = new FileOutputStream(reportConfig.getDestinationFile());
        generateSheets(reportConfig, workbook);
        workbook.write(fileOut);
        fileOut.close();
        LOGGER.debug("workbook created");
        return workbook;
    }

    private void generateSheets(ReportConfig reportConfig, Workbook workbook) {
        for (ReportSheet sheet : reportConfig.getReportSheets()) {
            generateSheet(workbook, sheet);
        }
    }

    private void generateSheet(Workbook workbook, ReportSheet sheet) {
        String safeName = getSafeSheetName(sheet.getSheetName());
        LOGGER.debug("creating sheet " + safeName);
        workbook.createSheet(safeName);
    }

    private String getSafeSheetName(String sheetName) {
        String safeSheetName = WorkbookUtil.createSafeSheetName(sheetName);
        if (!safeSheetName.equals(sheetName)) {
            LOGGER.warn("Sheet name " + sheetName + " contains invalid characters, renaming to " + safeSheetName);
        }
        return safeSheetName;
    }

}