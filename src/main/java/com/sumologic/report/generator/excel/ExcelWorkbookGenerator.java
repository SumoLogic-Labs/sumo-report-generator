package com.sumologic.report.generator.excel;

import com.sumologic.report.config.ReportConfig;
import com.sumologic.report.config.ReportSheet;
import com.sumologic.report.generator.ReportGenerationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ExcelWorkbookGenerator implements WorkbookGenerator {

    private static final Log LOGGER = LogFactory.getLog(ExcelWorkbookGenerator.class);

    @Override
    public Workbook generateWorkbook(ReportConfig reportConfig) throws ReportGenerationException {
        try {
            return generateWorkbookWithSheets(reportConfig);
        } catch (IOException | InvalidFormatException e) {
            LOGGER.error("unable to generate workbook!");
            throw  new ReportGenerationException(e);
        }
    }

    private Workbook generateWorkbookWithSheets(ReportConfig reportConfig) throws IOException, InvalidFormatException {
        if (!reportConfig.isAppendToDestination()) {
            LOGGER.debug("creating empty workbook with sheets");
            Workbook workbook = new XSSFWorkbook();
            FileOutputStream fileOut = new FileOutputStream(reportConfig.getDestinationFile());
            generateSheets(reportConfig, workbook);
            workbook.write(fileOut);
            fileOut.close();
            LOGGER.debug("workbook created");
            return workbook;
        } else {
            LOGGER.debug("will append to destination file");
            File destFile = new File(reportConfig.getDestinationFile());
            FileInputStream fileInputStream = new FileInputStream(destFile);
            OPCPackage opc = OPCPackage.open(fileInputStream);
            return WorkbookFactory.create(opc);
        }
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