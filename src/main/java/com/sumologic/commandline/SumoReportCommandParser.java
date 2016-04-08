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
package com.sumologic.commandline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumologic.report.config.ReportConfig;
import com.sumologic.report.generator.ReportGenerationException;
import com.sumologic.report.generator.ReportGenerator;
import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class SumoReportCommandParser {

    private static final Log LOGGER = LogFactory.getLog(SumoReportCommandParser.class);

    @Autowired
    private ReportGenerator reportGenerator;

    public void parse(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            parseCommandLine(args, parser);
        } catch (ParseException e) {
            LOGGER.error("Unable to parse arguments!");
        } catch (ReportGenerationException e) {
            LOGGER.error("Unable to generate report!");
            LOGGER.error(e);
        } catch (IOException e) {
            LOGGER.error("Unable to open report config!");
            LOGGER.error(e);
        }
    }

    private void parseCommandLine(String[] args, CommandLineParser parser)
            throws ReportGenerationException, ParseException, IOException {
        CommandLine commandLine = parser.parse(getOptions(), args);
        parseModeOptions(commandLine);
        parseOptions(commandLine);
    }

    private void parseOptions(CommandLine commandLine) throws ReportGenerationException, IOException {
        if (commandLine.hasOption("h")) {
            printHelp();
        } else if (commandLine.hasOption("c")) {
            invokeReportGenerator(commandLine);
        } else if (commandLine.hasOption("v")) {
            System.out.println(getClass().getPackage().getImplementationVersion());
        } else {
            LOGGER.error("invalid arguments!");
            printHelp();
        }
    }

    private void invokeReportGenerator(CommandLine commandLine) throws ReportGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ReportConfig reportConfig;
        String reportConfigPath = commandLine.getOptionValue("c");
        reportConfig = mapper.readValue(new File(reportConfigPath), ReportConfig.class);
        reportGenerator.generateReport(reportConfig);
    }

    private void parseModeOptions(CommandLine commandLine) {
        if (commandLine.hasOption("d")) {
            Logger.getRootLogger().setLevel(Level.DEBUG);
        } else if (commandLine.hasOption("t")) {
            Logger.getRootLogger().setLevel(Level.TRACE);
        }
    }

    private static Options getOptions() {
        Options options = new Options();
        options.addOption( "c", "config", true, "Path to the report JSON configuration file." );
        options.addOption( "d", "debug", false, "Enable debug logging." );
        options.addOption( "h", "help", false, "Prints help information." );
        options.addOption( "t", "trace", false, "Enable trace logging." );
        options.addOption( "v", "version", false, "Get the version of the utility." );
        return options;
    }

    private static void printHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("SumoLogic Report Generator", getOptions());
    }

}