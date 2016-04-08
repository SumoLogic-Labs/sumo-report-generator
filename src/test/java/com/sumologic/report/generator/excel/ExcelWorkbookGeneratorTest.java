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
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class ExcelWorkbookGeneratorTest extends BaseExcelTest {

    @Autowired
    private WorkbookGenerator workbookGenerator;

    @Test
    public void testWorkbookWithSheetsCreation() throws Exception {
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/testWorkbookWithSheetsCreation.json");
        workbookGenerator.generateWorkbook(reportConfig);
        File file = new File(reportConfig.getDestinationFile());
        Assert.assertTrue(file.exists());
        Workbook createdWorkbook = new XSSFWorkbook(file);
        assertEquals(2, createdWorkbook.getNumberOfSheets());
        assertEquals("sheet1", createdWorkbook.getSheetName(0));
        assertEquals("sheet2", createdWorkbook.getSheetName(1));
    }

    @Test(expected = ReportGenerationException.class)
    public void testWorkbookWithSheetsCreationException() throws Exception {
        ReportConfig reportConfig = mock(ReportConfig.class);
        doThrow(IOException.class).when(reportConfig).getDestinationFile();
        workbookGenerator.generateWorkbook(reportConfig);
    }

    @Test
    public void testWorkbookCreationWithInvalidSheetName() throws Exception {
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/testWorkbookCreationWithInvalidSheetName.json");
        workbookGenerator.generateWorkbook(reportConfig);
        File file = new File(reportConfig.getDestinationFile());
        Assert.assertTrue(file.exists());
        Workbook createdWorkbook = new XSSFWorkbook(file);
        assertEquals(1, createdWorkbook.getNumberOfSheets());
        assertEquals("sheet       ", createdWorkbook.getSheetName(0));
    }

}