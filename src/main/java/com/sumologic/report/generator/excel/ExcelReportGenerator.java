/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.sumologic.report.generator.excel;

import com.sumologic.report.config.ReportConfig;
import com.sumologic.report.generator.ReportGenerationException;
import com.sumologic.report.generator.ReportGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ExcelReportGenerator implements ReportGenerator {

    private static final Log LOGGER = LogFactory.getLog(ExcelReportGenerator.class);

    @Autowired
    private WorkbookGenerator workbookGenerator;

    @Autowired
    private WorkbookPopulator workbookPopulator;

    @Override
    public void generateReport(ReportConfig reportConfig) throws ReportGenerationException {
        try {
            LOGGER.info("starting report generation");
            long start = System.currentTimeMillis();
            LOGGER.debug("using config: " + reportConfig);
            Workbook workbook = getWorkbook(reportConfig);
            workbookPopulator.populateWorkbookWithData(reportConfig, workbook);
            LOGGER.info("updating formulas");
            XSSFFormulaEvaluator.evaluateAllFormulaCells((XSSFWorkbook) workbook);
            FileOutputStream fileOut = new FileOutputStream(reportConfig.getDestinationFile());
            workbook.write(fileOut);
            fileOut.close();
            String timeTaken = new SimpleDateFormat("mm:ss").format(new Date(System.currentTimeMillis() - start));
            LOGGER.info("report successfully generated in " + timeTaken);
        } catch (IOException | InvalidFormatException e ) {
            LOGGER.error("unabe to generate report!");
            throw new ReportGenerationException(e);
        }
    }

    private Workbook getWorkbook(ReportConfig reportConfig) throws IOException, ReportGenerationException, InvalidFormatException {
        Workbook workbook;
        if (reportConfig.getTemplateFile() == null) {
            workbook = workbookGenerator.generateWorkbook(reportConfig);
        } else if (reportConfig.isAppendToDestination()) {
            workbook = getExistingWorkbook(reportConfig);
        } else {
            workbook = copyTemplate(reportConfig);
        }
        return workbook;
    }

    private Workbook copyTemplate(ReportConfig reportConfig) throws ReportGenerationException {
        try {
            File srcFile = new File(reportConfig.getTemplateFile());
            File destFile = new File(reportConfig.getDestinationFile());
            FileUtils.copyFile(srcFile, destFile);
            return getExistingWorkbook(reportConfig);
        } catch (IOException e) {
            throw new ReportGenerationException(e);
        }
    }

    private Workbook getExistingWorkbook(ReportConfig reportConfig) throws ReportGenerationException {
        try {
            File destFile = new File(reportConfig.getDestinationFile());
            FileInputStream fileInputStream = new FileInputStream(destFile);
            OPCPackage opc = OPCPackage.open(fileInputStream);
            return WorkbookFactory.create(opc);
        } catch (IOException | InvalidFormatException e) {
            throw new ReportGenerationException(e);
        }
    }

}