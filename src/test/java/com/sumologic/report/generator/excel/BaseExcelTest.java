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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumologic.BaseSumoReportGeneratorTest;
import com.sumologic.report.config.ReportConfig;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/spring-test-context.xml")
public abstract class BaseExcelTest extends BaseSumoReportGeneratorTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    protected ReportConfig getReportConfigFromResource(String resourcePath) throws IOException {
        copyResourceToFolder(resourcePath, tempFolderPath + "/config.json");
        ReportConfig reportConfig = mapper.readValue(new File(tempFolderPath + "/config.json"), ReportConfig.class);
        reportConfig.setDestinationFile(tempFolderPath + "/workbook.xlsx");
        return reportConfig;
    }

}