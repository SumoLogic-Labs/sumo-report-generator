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
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class ExcelWorkbookPopulatorTest extends BaseExcelTest {

    @Autowired
    private WorkbookGenerator workbookGenerator;

    @Autowired
    private WorkbookPopulator workbookPopulator;

    @Mock
    @Autowired
    private WorksheetPopulator worksheetPopulator;

    @Test (expected = ReportGenerationException.class)
    public void testIOException() throws Exception {
        ReportConfig reportConfig = mock(ReportConfig.class);
        Workbook workbook = new XSSFWorkbook();
        doThrow(IOException.class).when(reportConfig).getDestinationFile();
        workbookPopulator.populateWorkbookWithData(reportConfig, workbook);
    }

    @Test
    public void testWorkbookPopulation() throws Exception {
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/testWorkbookPopulation.json");
        ReflectionTestUtils.setField(workbookPopulator, "worksheetPopulator", worksheetPopulator);
        doNothing().when(worksheetPopulator).populateSheetWithData(any());
        Workbook workbook = workbookGenerator.generateWorkbook(reportConfig);
        workbookPopulator.populateWorkbookWithData(reportConfig, workbook);
    }

}