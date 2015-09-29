package com.sumologic.cs.omus.commandline;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class OMUSCommandLine {

    private OMUSCommandLine() {
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/spring-context.xml");
        OMUSCommandParser parser = context.getBean(OMUSCommandParser.class);
        parser.parse(args);
    }

}