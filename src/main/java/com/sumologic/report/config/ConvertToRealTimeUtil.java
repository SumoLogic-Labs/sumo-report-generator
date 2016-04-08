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

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertToRealTimeUtil {

    /**
     * Calculates a time in the past
     * @param time - how far we want to go back
     * @return time formatted
     */
    public static String timeAgo(String time) {
        long minusTime = convertDurationToMillis(time);
        long milliSeconds = System.currentTimeMillis() - minusTime;
        return reformattedTime(milliSeconds);
    }

    public static String currentTime() {
        return reformattedTime(System.currentTimeMillis());
    }

    /**
     * Takes the milliseconds and converts it to the formatted time
     * @param milliSeconds
     * @return the date
     */
    public static String reformattedTime(long milliSeconds) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(milliSeconds));
        String[] dates = date.split(" ");
        date = dates[0] + "T" + dates[1];
        return date;
    }

    /**
     * Calls the correct functions to convert the time period to milliseconds
     * @param time
     * @return milliseconds
     */
    private static long convertDurationToMillis(String time) {

        if(time == null || (time != null && "".equals(time))) {
            return 0;
        }

        BigDecimal milliseconds = null;

        // String to be scanned to find the pattern.
        String line = time;
        String pattern = "(\\d+.\\d+|\\d+)(\\w+)";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(line);

        if (m.find()) {
            String duration = m.group(1);
            String timeScale = m.group(2).toLowerCase();

            switch (timeScale) {
                case "s":
                    milliseconds = convertSecondsToMillis(new BigDecimal(duration));
                    break;
                case "m":
                    milliseconds = convertMinutesToMillis(new BigDecimal(duration));
                    break;
                case "h":
                    milliseconds = convertHoursToMillis(new BigDecimal(duration));
                    break;
                case "d":
                    milliseconds = convertDaysToMillis(new BigDecimal(duration));
                    break;
                case "w":
                    milliseconds = convertWeeksToMillis(new BigDecimal(duration));
                    break;
            }
        }

        return milliseconds.longValueExact();
    }

    /**
     * Number of second = # of seconds *  1000 millis
     * @param seconds
     * @return seconds converted to milliseconds
     */
    private static BigDecimal convertSecondsToMillis(BigDecimal seconds) {
        return seconds.multiply(new BigDecimal(1000));
    }

    /**
     * Number of minutes = # of minutes * 60 seconds * 1000 millis
     * @param minutes
     * @return minutes converted to milliseconds
     */
    private static BigDecimal convertMinutesToMillis(BigDecimal minutes) {
        return minutes.multiply(convertSecondsToMillis(new BigDecimal(60)));
    }

    /**
     * Number of hours = # of hours * 60 seconds * 60 minutes * 1000 millis
     * @param hours
     * @return hours converted to milliseconds
     */
    private static BigDecimal convertHoursToMillis(BigDecimal hours) {
        return hours.multiply(convertMinutesToMillis(new BigDecimal(60)));
    }

    /**
     * Number of  days = Number of days * 24 hours * 60 seconds * 60 minutes * 1000 millis
     * @param days
     * @return days converted to milliseconds
     */
    private static BigDecimal convertDaysToMillis(BigDecimal days) {
        return days.multiply(convertHoursToMillis(new BigDecimal(24)));
    }

    /**
     * Number of weeks = # of weeks * 7 day * 24 hours * 60 seconds * 60 minutes * 1000 millis
     * @param weeks
     * @return weeks converted to milliseconds
     */
    private static BigDecimal convertWeeksToMillis(BigDecimal weeks) {
        return weeks.multiply(convertDaysToMillis(new BigDecimal(7)));
    }

}
