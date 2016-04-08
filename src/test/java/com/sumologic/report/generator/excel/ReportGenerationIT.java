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
import com.sumologic.report.generator.ReportGenerator;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;

public class ReportGenerationIT extends BaseExcelTest {

    @Autowired
    private ReportGenerator reportGenerator;

    @Autowired
    private WorksheetPopulator worksheetPopulator;

    @Autowired
    private WorkbookPopulator workbookPopulator;

    @Test
    public void testWithoutIteration() throws Exception {
        ReflectionTestUtils.setField(worksheetPopulator, "MAX_OFFSET", 10000);
        ReflectionTestUtils.setField(workbookPopulator, "worksheetPopulator", worksheetPopulator);
        ReflectionTestUtils.setField(reportGenerator, "workbookPopulator", workbookPopulator);
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/IT.json");
        reportGenerator.generateReport(reportConfig);
        FileInputStream fileInputStream = new FileInputStream(reportConfig.getDestinationFile());
        OPCPackage opc = OPCPackage.open(fileInputStream);
        Workbook workbook = WorkbookFactory.create(opc);
        assertEquals(2, workbook.getNumberOfSheets());
        Sheet sheet1 = workbook.getSheetAt(0);
        Sheet sheet2 = workbook.getSheetAt(1);
        assertEquals("ingest", sheet1.getSheetName());
        assertEquals("ingest ", sheet2.getSheetName());
        assertEquals(806, sheet1.getPhysicalNumberOfRows());
        assertEquals(4, sheet1.getRow(0).getPhysicalNumberOfCells());
        assertEquals(2, sheet2.getPhysicalNumberOfRows());
        assertEquals(4, sheet2.getRow(0).getPhysicalNumberOfCells());
        assertEquals("org_id", sheet1.getRow(0).getCell(0).getStringCellValue());
        assertEquals("accountname", sheet1.getRow(0).getCell(1).getStringCellValue());
        assertEquals("date", sheet1.getRow(0).getCell(2).getStringCellValue());
        assertEquals("size_gb", sheet1.getRow(0).getCell(3).getStringCellValue());
        assertEquals("org_id", sheet2.getRow(0).getCell(0).getStringCellValue());
        assertEquals("accountname", sheet2.getRow(0).getCell(1).getStringCellValue());
        assertEquals("date", sheet2.getRow(0).getCell(2).getStringCellValue());
        assertEquals("size_gb", sheet2.getRow(0).getCell(3).getStringCellValue());
    }

    @Test
    public void testWithAppend() throws Exception {
        ReflectionTestUtils.setField(worksheetPopulator, "MAX_OFFSET", 10000);
        ReflectionTestUtils.setField(workbookPopulator, "worksheetPopulator", worksheetPopulator);
        ReflectionTestUtils.setField(reportGenerator, "workbookPopulator", workbookPopulator);
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/Initial.json");
        reportGenerator.generateReport(reportConfig);
        FileInputStream fileInputStream = new FileInputStream(reportConfig.getDestinationFile());
        OPCPackage opc = OPCPackage.open(fileInputStream);
        Workbook workbook = WorkbookFactory.create(opc);
        assertEquals(2, workbook.getNumberOfSheets());
        Sheet sheet1 = workbook.getSheetAt(0);
        Sheet sheet2 = workbook.getSheetAt(1);
        assertEquals("ingest", sheet1.getSheetName());
        assertEquals("ingest ", sheet2.getSheetName());
        assertEquals(806, sheet1.getPhysicalNumberOfRows());
        assertEquals(4, sheet1.getRow(0).getPhysicalNumberOfCells());
        assertEquals(2, sheet2.getPhysicalNumberOfRows());
        assertEquals(4, sheet2.getRow(0).getPhysicalNumberOfCells());
        assertEquals("org_id", sheet1.getRow(0).getCell(0).getStringCellValue());
        assertEquals("accountname", sheet1.getRow(0).getCell(1).getStringCellValue());
        assertEquals("date", sheet1.getRow(0).getCell(2).getStringCellValue());
        assertEquals("size_gb", sheet1.getRow(0).getCell(3).getStringCellValue());
        assertEquals("org_id", sheet2.getRow(0).getCell(0).getStringCellValue());
        assertEquals("accountname", sheet2.getRow(0).getCell(1).getStringCellValue());
        assertEquals("date", sheet2.getRow(0).getCell(2).getStringCellValue());
        assertEquals("size_gb", sheet2.getRow(0).getCell(3).getStringCellValue());
        reportConfig = getReportConfigFromResource("/testReportConfig/Append.json");
        reportGenerator.generateReport(reportConfig);
        fileInputStream = new FileInputStream(reportConfig.getDestinationFile());
        opc = OPCPackage.open(fileInputStream);
        workbook = WorkbookFactory.create(opc);
        assertEquals(2, workbook.getNumberOfSheets());
        sheet1 = workbook.getSheetAt(0);
        sheet2 = workbook.getSheetAt(1);
        assertEquals("ingest", sheet1.getSheetName());
        assertEquals("ingest ", sheet2.getSheetName());
        assertEquals(1611, sheet1.getPhysicalNumberOfRows());
        assertEquals(4, sheet1.getRow(0).getPhysicalNumberOfCells());
        assertEquals(3, sheet2.getPhysicalNumberOfRows());
        assertEquals(4, sheet2.getRow(0).getPhysicalNumberOfCells());
        assertEquals("org_id", sheet1.getRow(0).getCell(0).getStringCellValue());
        assertEquals("accountname", sheet1.getRow(0).getCell(1).getStringCellValue());
        assertEquals("date", sheet1.getRow(0).getCell(2).getStringCellValue());
        assertEquals("size_gb", sheet1.getRow(0).getCell(3).getStringCellValue());
        assertEquals("org_id", sheet2.getRow(0).getCell(0).getStringCellValue());
        assertEquals("accountname", sheet2.getRow(0).getCell(1).getStringCellValue());
        assertEquals("date", sheet2.getRow(0).getCell(2).getStringCellValue());
        assertEquals("size_gb", sheet2.getRow(0).getCell(3).getStringCellValue());
    }

    @Test
    public void testWithIteration() throws Exception {
        ReflectionTestUtils.setField(worksheetPopulator, "MAX_OFFSET", 100);
        ReflectionTestUtils.setField(workbookPopulator, "worksheetPopulator", worksheetPopulator);
        ReflectionTestUtils.setField(reportGenerator, "workbookPopulator", workbookPopulator);
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/IT.json");
        reportGenerator.generateReport(reportConfig);
        FileInputStream fileInputStream = new FileInputStream(reportConfig.getDestinationFile());
        OPCPackage opc = OPCPackage.open(fileInputStream);
        Workbook workbook = WorkbookFactory.create(opc);
        Sheet sheet1 = workbook.getSheetAt(0);
        Sheet sheet2 = workbook.getSheetAt(1);
        assertEquals("ingest", sheet1.getSheetName());
        assertEquals("ingest ", sheet2.getSheetName());
        assertEquals(806, sheet1.getPhysicalNumberOfRows());
        assertEquals(4, sheet1.getRow(0).getPhysicalNumberOfCells());
        assertEquals(2, sheet2.getPhysicalNumberOfRows());
        assertEquals(4, sheet2.getRow(0).getPhysicalNumberOfCells());
        assertEquals("org_id", sheet1.getRow(0).getCell(0).getStringCellValue());
        assertEquals("accountname", sheet1.getRow(0).getCell(1).getStringCellValue());
        assertEquals("date", sheet1.getRow(0).getCell(2).getStringCellValue());
        assertEquals("size_gb", sheet1.getRow(0).getCell(3).getStringCellValue());
        assertEquals("org_id", sheet2.getRow(0).getCell(0).getStringCellValue());
        assertEquals("accountname", sheet2.getRow(0).getCell(1).getStringCellValue());
        assertEquals("date", sheet2.getRow(0).getCell(2).getStringCellValue());
        assertEquals("size_gb", sheet2.getRow(0).getCell(3).getStringCellValue());
    }

    @Test
    public void testWithTemplate() throws Exception {
        ReflectionTestUtils.setField(worksheetPopulator, "MAX_OFFSET", 10000);
        ReflectionTestUtils.setField(workbookPopulator, "worksheetPopulator", worksheetPopulator);
        ReflectionTestUtils.setField(reportGenerator, "workbookPopulator", workbookPopulator);
        ReportConfig reportConfig = getReportConfigFromResource("/testReportConfig/IT.json");
        reportConfig.setTemplateFile(tempFolderPath + "template.xlsx");
        copyResourceToFolder("/template.xlsx", tempFolderPath + "template.xlsx");
        reportGenerator.generateReport(reportConfig);
        FileInputStream fileInputStream = new FileInputStream(reportConfig.getDestinationFile());
        OPCPackage opc = OPCPackage.open(fileInputStream);
        Workbook workbook = WorkbookFactory.create(opc);
        Sheet sheet1 = workbook.getSheetAt(0);
        Sheet sheet2 = workbook.getSheetAt(1);
        assertEquals("ingest", sheet1.getSheetName());
        assertEquals("ingest ", sheet2.getSheetName());
        assertEquals(806, sheet1.getPhysicalNumberOfRows());
        assertEquals(4, sheet1.getRow(0).getPhysicalNumberOfCells());
        assertEquals(2, sheet2.getPhysicalNumberOfRows());
        assertEquals(8, sheet2.getRow(0).getPhysicalNumberOfCells());
        assertEquals("accountname", sheet1.getRow(0).getCell(0).getStringCellValue());
        assertEquals("org_id", sheet1.getRow(0).getCell(1).getStringCellValue());
        assertEquals("date", sheet1.getRow(0).getCell(2).getStringCellValue());
        assertEquals("size_gb", sheet1.getRow(0).getCell(3).getStringCellValue());
        assertEquals("foo", sheet2.getRow(0).getCell(0).getStringCellValue());
        assertEquals("accountname", sheet2.getRow(0).getCell(1).getStringCellValue());
        assertEquals("foo2", sheet2.getRow(0).getCell(2).getStringCellValue());
        assertEquals("org_id", sheet2.getRow(0).getCell(3).getStringCellValue());
        assertEquals("date", sheet2.getRow(0).getCell(4).getStringCellValue());
        assertEquals("size_gb", sheet2.getRow(0).getCell(5).getStringCellValue());
        assertEquals("foo3", sheet2.getRow(0).getCell(6).getStringCellValue());
        assertEquals("size_gb2", sheet2.getRow(0).getCell(8).getStringCellValue());
    }

}