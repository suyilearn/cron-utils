package com.cronutils.mapper;

import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.google.common.collect.Sets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/*
 * Copyright 2015 jmrozanec
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@RunWith(AndroidJUnit4.class)
public class CronMapperIntegrationTest {

    @Test
    public void testSpecificTimeCron4jToQuartz() {
        assertEquals("0 30 8 10 6 ? *", CronMapper.fromCron4jToQuartz().map(cron4jParser().parse("30 8 10 6 *")).asString());
    }

    @Test
    public void testMoreThanOneInstanceCron4jToQuartz() {
        assertEquals("0 0 11,16 * * ? *", CronMapper.fromCron4jToQuartz().map(cron4jParser().parse("0 11,16 * * *")).asString());
    }

    @Test
    public void testRangeOfTimeCron4jToQuartz() {
        String expression = "0 9-18 * * 1-3";
        String expected = "0 0 9-18 ? * 2-4 *";
        assertEquals(expected, CronMapper.fromCron4jToQuartz().map(cron4jParser().parse(expression)).asString());
    }

    @Test
    public void testSpecificTimeQuartzToCron4j() {
        String expression = "5 30 8 10 6 ? 1984";
        assertEquals("30 8 10 6 *", CronMapper.fromQuartzToCron4j().map(quartzParser().parse(expression)).asString());
    }

    @Test
    public void testMoreThanOneInstanceQuartzToCron4j() {
        String expression = "5 0 11,16 * * ? 1984";
        assertEquals("0 11,16 * * *", CronMapper.fromQuartzToCron4j().map(quartzParser().parse(expression)).asString());
    }

    @Test
    public void testRangeOfTimeQuartzToCron4j() {
        String expected = "0 9-18 * * 0-2";
        String expression = "5 0 9-18 ? * 1-3 1984";
        assertEquals(expected, CronMapper.fromQuartzToCron4j().map(quartzParser().parse(expression)).asString());
    }

    @Test
    public void testDaysOfWeekUnixToQuartz() {
        String input = "* * * * 3,5-6";
        String expected = "0 * * ? * 4,6-7 *";
        assertEquals(expected, CronMapper.fromUnixToQuartz().map(unixParser().parse(input)).asString());
    }

    /**
     * Issue #36, #56: Unix to Quartz not accurately mapping every minute pattern
     * or patterns that involve every day of month and every day of week
     */
    @Test
    public void testEveryMinuteUnixToQuartz() {
        String input = "* * * * *";
        String expected1 = "0 * * * * ? *";
        String expected2 = "0 * * ? * * *";
        String mapping = CronMapper.fromUnixToQuartz().map(unixParser().parse(input)).asString();
        assertTrue(
                String.format("Expected [%s] or [%s] but got [%s]", expected1, expected2, mapping),
                Sets.newHashSet(expected1, expected2).contains(mapping)
        );
    }

    /**
     * Issue #36, #56: Unix to Quartz not accurately mapping every minute pattern
     * or patterns that involve every day of month and every day of week
     */
    @Test
    public void testUnixToQuartzQuestionMarkRequired() {
        String input = "0 0 * * 1";
        String expected = "0 0 0 ? * 2 *";
        String mapping = CronMapper.fromUnixToQuartz().map(unixParser().parse(input)).asString();
        assertEquals(String.format("Expected [%s] but got [%s]", expected, mapping), expected, mapping);
    }

    private CronParser cron4jParser() {
        return new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.CRON4J));
    }

    private CronParser quartzParser() {
        return new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
    }

    private CronParser unixParser() {
        return new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));
    }
}
