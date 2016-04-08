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

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;


public class ConvertToRealTimeUtilTest {

    @Test
    public void testWeekCalculation() throws Exception {
        // Get the date for 1 day out
        Date date = getParsedDate("-1.5w");
        // Get the current time
        long currentTimeMillis = System.currentTimeMillis();
        // The difference
        long timeDifference = currentTimeMillis - date.getTime();
        assertTrue(offset(timeDifference, 907200000));
    }

    @Test
    public void testDayCalculation() throws Exception {
        // Get the date for 1 day out
        Date date = getParsedDate("-1d");
        // Get the current time
        long currentTimeMillis = System.currentTimeMillis();
        // The difference
        long timeDifference = currentTimeMillis - date.getTime();
        assertTrue(offset(timeDifference, 86400000));
    }

    @Test
    public void testHourCalculation() throws Exception {
        // Get the date for 1 day out
        Date date = getParsedDate("-1.5h");
        // Get the current time
        long currentTimeMillis = System.currentTimeMillis();
        // The difference
        long timeDifference = currentTimeMillis - date.getTime();
        assertTrue(offset(timeDifference, 5400000));
    }

    @Test
    public void testMinuteCalculation() throws Exception {
        // Get the date for 1 day out
        Date date = getParsedDate("-5.3m");
        // Get the current time
        long currentTimeMillis = System.currentTimeMillis();
        // The difference
        long timeDifference = currentTimeMillis - date.getTime();
        assertTrue(offset(timeDifference, 318000));
    }

    @Test
    public void testSecondCalculation() throws Exception {
        // Get the date for 1 day out
        Date date = getParsedDate("-39s");
        // Get the current time
        long currentTimeMillis = System.currentTimeMillis();
        // The difference
        long timeDifference = currentTimeMillis - date.getTime();
        assertTrue(offset(timeDifference, 39000));
    }


    /**
     * Helper function to get the offset time we want to test
     * @param offSetTime
     * @return the Java Date of the offset time
     * @throws ParseException
     */
    private Date getParsedDate(String offSetTime) throws ParseException {
        String time[] = ConvertToRealTimeUtil.timeAgo(offSetTime).split("T");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(time[0] + " " + time[1]);
        return date;
    }

    /**
     * Since there is some calculation time required to process the logic, we need to allow for some
     * time margin. Here I am allowing a second processing time buffer for each calculation.
     * @param calculatedTime - the time calculated by the logic in milliseconds
     * @param timeToTest - how long in milleseconds this should take (for example 1 day = 86400000 milliseconds)
     * @return true - if the time is within the offset time to calculate the logic.
     */
    private boolean offset(long calculatedTime, long timeToTest) {
        if(calculatedTime - timeToTest < 1000) {
            return true;
        }
        else {
            return false;
        }
    }


}
