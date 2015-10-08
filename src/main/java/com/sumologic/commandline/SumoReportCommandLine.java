package com.sumologic.commandline;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SumoReportCommandLine {

    private SumoReportCommandLine() {
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/spring-context.xml");
        SumoReportCommandParser parser = context.getBean(SumoReportCommandParser.class);
        parser.parse(args);
    }

}