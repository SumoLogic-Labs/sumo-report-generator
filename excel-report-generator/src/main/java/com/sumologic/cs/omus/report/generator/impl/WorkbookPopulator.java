package com.sumologic.cs.omus.report.generator.impl;

import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

public interface WorkbookPopulator {

    void populateWorkbookWithData(ReportConfig reportConfig) throws IOException, InvalidFormatException;

}