package com.sumologic.cs.omus.commandline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumologic.cs.omus.report.generator.api.OmusReportGenerationException;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.report.generator.api.ReportGenerator;
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
public class OMUSCommandParser {

    private static final Log LOGGER = LogFactory.getLog(OMUSCommandParser.class);

    @Autowired
    private ReportGenerator reportGenerator;

    public void parse(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            parseCommandLine(args, parser);
        } catch (ParseException e) {
            LOGGER.error("Unable to parse arguments!");
        } catch (OmusReportGenerationException e) {
            LOGGER.error("Unable to generate report!");
            LOGGER.error(e);
        }
    }

    private void parseCommandLine(String[] args, CommandLineParser parser)
            throws OmusReportGenerationException, ParseException {
        CommandLine commandLine = parser.parse(getOptions(), args);
        parseModeOptions(commandLine);
        parseOptions(commandLine);
    }

    private void parseOptions(CommandLine commandLine) throws OmusReportGenerationException {
        if (commandLine.hasOption("h")) {
            printHelp();
        } else if (commandLine.hasOption("c")) {
            invokeReportGenerator(commandLine);
        } else if (commandLine.hasOption("v")) {
            System.out.println((getClass().getPackage().getImplementationVersion()));
        } else {
            LOGGER.error("invalid arguments!");
            printHelp();
        }
    }

    private void invokeReportGenerator(CommandLine commandLine) throws OmusReportGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        ReportConfig reportConfig;
        String reportConfigPath = commandLine.getOptionValue("c");
        try {
            reportConfig = mapper.readValue(new File(reportConfigPath), ReportConfig.class);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new OmusReportGenerationException("Unable to open report config " + reportConfigPath);
        }
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
        options.addOption( "v", "version", false, "Get the version of the utility" );
        return options;
    }

    private static void printHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("OMUS Command Line", getOptions());
    }

}