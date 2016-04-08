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
import com.sumologic.report.config.ReportSheet;
import com.sumologic.report.config.WorksheetConfig;
import com.sumologic.report.generator.ReportGenerationException;
import com.sumologic.service.SumoDataService;
import com.sumologic.service.SumoDataServiceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

@Service
public class ExcelWorkbookPopulator implements WorkbookPopulator {

    private static final Log LOGGER = LogFactory.getLog(ExcelWorkbookPopulator.class);

    @Autowired
    private SumoDataServiceFactory sumoDataServiceFactory;

    @Autowired
    private WorksheetPopulator worksheetPopulator;

    @Override
    public void populateWorkbookWithData(ReportConfig reportConfig, Workbook workbook) throws ReportGenerationException {
        try {
            openWorkbookAndProcessSheets(reportConfig, workbook);
        } catch (IOException e) {
            LOGGER.error("unable to generate workbook!");
            throw  new ReportGenerationException(e);
        }
    }

    private void openWorkbookAndProcessSheets(ReportConfig reportConfig, Workbook workbook) throws IOException {
        LOGGER.debug("populating workbook");
        processSheets(reportConfig, workbook);
        LOGGER.debug("workbook populated");
    }

    private void processSheets(ReportConfig reportConfig, Workbook workbook) throws IOException {
        SumoDataService sumoDataService = sumoDataServiceFactory.getSumoDataService(reportConfig);
        for (ReportSheet reportSheet : reportConfig.getReportSheets()) {
            WorksheetConfig config = new WorksheetConfig();
            config.setReportConfig(reportConfig);
            config.setReportSheet(reportSheet);
            config.setSumoDataService(sumoDataService);
            String worksheetName = WorkbookUtil.createSafeSheetName(reportSheet.getSheetName());
            Sheet workbookSheet = workbook.getSheet(worksheetName);
            config.setWorkbookSheet(workbookSheet);
            LOGGER.info("populating sheet " + reportSheet.getSheetName());
            worksheetPopulator.populateSheetWithData(config);
            FileOutputStream fileOut = new FileOutputStream(reportConfig.getDestinationFile());
            workbook.write(fileOut);
            fileOut.close();
        }
    }

}