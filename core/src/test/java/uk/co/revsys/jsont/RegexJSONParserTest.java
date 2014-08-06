/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.revsys.jsont;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew
 */
public class RegexJSONParserTest {
    
    public RegexJSONParserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of parseString method, of class RegexJSONParser.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        String input = "Mr Samuel Watson, samw@test.com";
        String regex = "(?<title>\\b\\w*\\b)\\s*(?<firstName>\\b\\w*\\b)\\s*(?<surname>\\b\\w*\\b)\\s*,.*";
        RegexJSONParser instance = new RegexJSONParser();
        Object expResult = "{ \"title\" : \"Mr\", \"firstName\" : \"Samuel\", \"surname\" : \"Watson\" }";
        Object result = instance.parseString(input, regex);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testParseLogLine() {
        System.out.println("parse");
        String input = "DEBUG 04 Aug 13:56:09 uk.co.revsys.oddball.consumer.kinesis.KinesisRecordProcessor#processRecordsWithRetries:93 thread:pool-1-thread-1 [Processing record]";
        String input2 ="DEBUG 04 Aug 17:58:55 uk.co.revsys.oddball.service.rest.OddballRestService#<init>:35 thread:localhost-startStop-1 [Initialising]";
        //input2 = "DEBUG 04 Aug 17:59:27 uk.co.revsys.oddball.consumer.kinesis.KinesisRecordProcessorFactory#<init>:20 thread:ContainerBackgroundProcessor[StandardEngine[Catalina]] [ruleSets = {screenshot=[ECScreenshot], base=[ECBase, ECBaseExt.{accountId}]}]";
        String regex = "^(?<level>\\w*)\\s+(?<timestamp>\\d\\d\\s\\w{3}\\s\\d\\d:\\d\\d:\\d\\d)\\s(?<class>.*)#(?<method>\\w*)\\:(?<line>\\d\\d)\\sthread\\:(?<thread>[^\\s]*)\\s\\[(?<message>.*)\\]";
        RegexJSONParser instance = new RegexJSONParser();
        Object expResult = "{ \"level\" : \"DEBUG\", \"timestamp\" : \"04 Aug 13:56:09\", \"class\" : \"uk.co.revsys.oddball.consumer.kinesis.KinesisRecordProcessor\", \"method\" : \"processRecordsWithRetries\", \"line\" : \"93\", \"thread\" : \"pool-1-thread-1\", \"message\" : \"Processing record\" }";
        Object result = instance.parseString(input, regex);
        assertEquals(expResult, result);
        String regex2 = "\\s*(?<level>\\w*)\\s+.*";
        //Object expResult2 = "{ \"level\" : \"DEBUG\", \"timestamp\" : \"04 Aug 13:56:09\", \"class\" : \"uk.co.revsys.oddball.consumer.kinesis.KinesisRecordProcessor\", \"method\" : \"processRecordsWithRetries\", \"line\" : \"93\", \"thread\" : \"pool-1-thread-1\", \"message\" : \"Processing record\" }";
        Object expResult2 = "{ \"level\" : \"DEBUG\" }";
        Object result2 = instance.parseString(input2, regex2);
        assertEquals(expResult2, result2);
    }


}
