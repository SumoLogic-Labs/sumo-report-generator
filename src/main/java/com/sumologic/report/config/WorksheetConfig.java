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
package com.sumologic.report.config;

import com.sumologic.service.SumoDataService;
import org.apache.poi.ss.usermodel.Sheet;

public class WorksheetConfig {

    private Sheet workbookSheet;
    private ReportSheet reportSheet;
    private SumoDataService sumoDataService;
    private ReportConfig reportConfig;

    public Sheet getWorkbookSheet() {
        return workbookSheet;
    }

    public void setWorkbookSheet(Sheet workbookSheet) {
        this.workbookSheet = workbookSheet;
    }

    public ReportSheet getReportSheet() {
        return reportSheet;
    }

    public void setReportSheet(ReportSheet reportSheet) {
        this.reportSheet = reportSheet;
    }

    public SumoDataService getSumoDataService() {
        return sumoDataService;
    }

    public void setSumoDataService(SumoDataService sumoDataService) {
        this.sumoDataService = sumoDataService;
    }

    public ReportConfig getReportConfig() {
        return reportConfig;
    }

    public void setReportConfig(ReportConfig reportConfig) {
        this.reportConfig = reportConfig;
    }

}
