package com.sumologic.cs.omus.commandline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumologic.cs.omus.report.generator.api.OmusReportGenerationException;
import com.sumologic.cs.omus.report.generator.api.ReportConfig;
import com.sumologic.cs.omus.report.generator.api.ReportGenerator;
import org.apache.commons.cli.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;

public class OMUSCommandLine {

    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = getOptions();
        try {
            parseArgs(args, parser, options);
        } catch (ParseException | OmusReportGenerationException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseArgs(String[] args, CommandLineParser parser, Options options) throws ParseException, IOException, OmusReportGenerationException {
        CommandLine commandLine = parser.parse(options, args);

        if (commandLine.hasOption("d")) {
            Logger.getRootLogger().setLevel(Level.DEBUG);
         } else if (commandLine.hasOption("t")) {
            Logger.getRootLogger().setLevel(Level.TRACE);
        }

        if (commandLine.hasOption("h")) {
            printHelp();
        } else if (commandLine.hasOption("c")) {
            ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/spring-context.xml");
            ReportGenerator reportGenerator = (ReportGenerator) context.getBean(ReportGenerator.class);
            ObjectMapper mapper = new ObjectMapper();
            ReportConfig reportConfig = mapper.readValue(new File(commandLine.getOptionValue("c")), ReportConfig.class);
            reportGenerator.generateReport(reportConfig);
        } else {
            System.out.println("invalid arguments!");
            printHelp();
        }
    }

    private static Options getOptions() {
        Options options = new Options();
        options.addOption( "c", "config", true, "Path to the report JSON configuration file." );
        options.addOption( "d", "debug", false, "Enable debug logging." );
        options.addOption( "h", "help", false, "Prints help information." );
        options.addOption( "t", "trace", false, "Enable trace logging." );
        return options;
    }

    private static void printHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("OMUS Command Line", getOptions());
    }

}