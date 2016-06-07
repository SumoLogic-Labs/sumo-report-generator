#Sumo Report Generator
This tool allows a user to execute multiple searches, and compile the data in a single report.  Currently, the only format is Excel.  Each tab in Excel would correspond to a search executed in Sumo Logic. NOTE: You must have access to the Sumo Search API in order to use this tool.

##Installation
Simply [download](https://github.com/SumoLogic/sumo-report-generator/releases) the jar file and run :
```
java -jar sumo-report-generator.jar
```

##Usage
See the [wiki](https://github.com/SumoLogic/sumo-report-generator/wiki) for detailed usage.
```
$java -jar sumo-report-generator.jar

usage: SumoLogic Report Generator
 -c,--config <arg>   Path to the report JSON configuration file.
 -d,--debug          Enable debug logging.
 -h,--help           Prints help information.
 -t,--trace          Enable trace logging.
 -v,--version        Get the version of the utility.
```


##License
Released under Apache 2.0 License.