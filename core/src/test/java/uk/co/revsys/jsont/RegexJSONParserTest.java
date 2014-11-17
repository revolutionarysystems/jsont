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
        //String regex = "^(?<level>\\w*)\\s+(?<timestamp>\\d\\d\\s\\w{3}\\s\\d\\d:\\d\\d:\\d\\d)\\s(?<class>.*)#(?<method>\\w*)\\:(?<line>\\d\\d)\\sthread\\:(?<thread>[^\\s]*)\\s\\[(?<message>.*)\\]";
        String regex = "^(?<level>\\w*)\\s+(?<timestamp>\\d\\d\\s\\w{3}\\s\\d\\d:\\d\\d:\\d\\d)\\s(?<class>.*)#(?<method>\\<?\\w*\\>?)\\:(?<line>\\d+)\\sthread\\:(?<thread>[^\\s]*)\\s\\[(?<message>.*)\\]";
                RegexJSONParser instance = new RegexJSONParser();
        Object expResult = "{ \"level\" : \"DEBUG\", \"timestamp\" : \"04 Aug 13:56:09\", \"class\" : \"uk.co.revsys.oddball.consumer.kinesis.KinesisRecordProcessor\", \"method\" : \"processRecordsWithRetries\", \"line\" : \"93\", \"thread\" : \"pool-1-thread-1\", \"message\" : \"Processing record\" }";
        Object result = instance.parseString(input, regex);
        assertEquals(expResult, result);
    }

    @Test
    public void testParseLogLine2() {
        System.out.println("parse");
        String input ="DEBUG 04 Aug 17:58:55 uk.co.revsys.oddball.service.rest.OddballRestService#<init>:35 thread:localhost-startStop-1 [Initialising]";
        String regex = "^(?<level>\\w*)\\s+(?<timestamp>\\d\\d\\s\\w{3}\\s\\d\\d:\\d\\d:\\d\\d)\\s(?<class>.*)#(?<method>\\<?\\w*\\>?)\\:(?<line>\\d+)\\sthread\\:(?<thread>[^\\s]*)\\s\\[(?<message>.*)\\]";
        RegexJSONParser instance = new RegexJSONParser();
        Object expResult = "{ \"level\" : \"DEBUG\", \"timestamp\" : \"04 Aug 17:58:55\", \"class\" : \"uk.co.revsys.oddball.service.rest.OddballRestService\", \"method\" : \"<init>\", \"line\" : \"35\", \"thread\" : \"localhost-startStop-1\", \"message\" : \"Initialising\" }";
        Object result = instance.parseString(input, regex);
        assertEquals(expResult, result);
    }

    @Test
    public void testParseLogLineNewlineEnd() {
        System.out.println("parse");
        String input = "DEBUG 04 Aug 13:56:09 uk.co.revsys.oddball.consumer.kinesis.KinesisRecordProcessor#processRecordsWithRetries:93 thread:pool-1-thread-1 [Processing record]\n";
        //String regex = "^(?<level>\\w*)\\s+(?<timestamp>\\d\\d\\s\\w{3}\\s\\d\\d:\\d\\d:\\d\\d)\\s(?<class>.*)#(?<method>\\w*)\\:(?<line>\\d\\d)\\sthread\\:(?<thread>[^\\s]*)\\s\\[(?<message>.*)\\]";
        String regex = "^(?<level>\\w*)\\s+(?<timestamp>\\d\\d\\s\\w{3}\\s\\d\\d:\\d\\d:\\d\\d)\\s(?<class>.*)#(?<method>\\<?\\w*\\>?)\\:(?<line>\\d+)\\sthread\\:(?<thread>[^\\s]*)\\s\\[(?<message>.*)\\]";
        RegexJSONParser instance = new RegexJSONParser();
        Object expResult = "{ \"level\" : \"DEBUG\", \"timestamp\" : \"04 Aug 13:56:09\", \"class\" : \"uk.co.revsys.oddball.consumer.kinesis.KinesisRecordProcessor\", \"method\" : \"processRecordsWithRetries\", \"line\" : \"93\", \"thread\" : \"pool-1-thread-1\", \"message\" : \"Processing record\" }";
        Object result = instance.parseString(input, regex);
        assertEquals(expResult, result);
    }

    @Test
    public void testParseLogLineEmbeddedJSON() {
        System.out.println("parse");
        String input = "INFO  06 Aug 16:27:09 uk.co.revsys.oddball.consumer.kinesis.KinesisRecordProcessor#processRecordsWithRetries:98 thread:pool-1-thread-1 [49540437523742567016182684081176742386203525543305936897, base, {\"accountId\":\"echo-central-master-account\",\"userId\":\"237fccd5-4a3b-4400-bb0d-ca872f15ab6d\",\"sessionId\":\"82e585db-4199-4a79-a763-1c6158e19db9\",\"domain\":\"dev.echo-central.com\",\"device\":{\"platform\":\"Win32\",\"screen\":{\"availWidth\":1600,\"availHeight\":860,\"availTop\":0,\"availLeft\":0,\"pixelDepth\":24,\"colorDepth\":24,\"width\":1600,\"height\":900},\"pixelRatio\":1},\"browser\":{\"vendor\":\"Google Inc.\",\"vendorSub\":\"\",\"product\":\"Gecko\",\"productSub\":\"20030107\",\"userAgent\":\"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36\",\"mimeTypes\":65,\"plugins\":21,\"language\":\"en-US\"},\"page\":{\"location\":{\"ancestorOrigins\":{\"length\":0},\"origin\":\"http://dev.echo-central.com\",\"hash\":\"\",\"search\":\"\",\"pathname\":\"/\",\"port\":\"\",\"hostname\":\"dev.echo-central.com\",\"host\":\"dev.echo-central.com\",\"protocol\":\"http:\",\"href\":\"http://dev.echo-central.com/\"},\"performance\":{\"loadEventEnd\":1407338818388,\"loadEventStart\":1407338818211,\"domComplete\":1407338818211,\"domContentLoadedEventEnd\":1407338815705,\"domContentLoadedEventStart\":1407338815703,\"domInteractive\":1407338815703,\"domLoading\":1407338815022,\"responseEnd\":1407338815084,\"responseStart\":1407338814994,\"requestStart\":1407338813385,\"secureConnectionStart\":0,\"connectEnd\":1407338813267,\"connectStart\":1407338813267,\"domainLookupEnd\":1407338813267,\"domainLookupStart\":1407338813267,\"fetchStart\":1407338813267,\"redirectEnd\":0,\"redirectStart\":0,\"unloadEventEnd\":1407338814995,\"unloadEventStart\":1407338814995,\"navigationStart\":1407338813267},\"title\":\"echoCHECK\"},\"connection\":{\"bandwidth\":\"not supported\",\"metered\":\"not supported\"},\"locale\":{\"country\":\"us\",\"lang\":\"en\"}}]";
        //String regex = "^(?<level>\\w*)\\s+(?<timestamp>\\d\\d\\s\\w{3}\\s\\d\\d:\\d\\d:\\d\\d)\\s(?<class>.*)#(?<method>\\w*)\\:(?<line>\\d\\d)\\sthread\\:(?<thread>[^\\s]*)\\s\\[(?<message>.*)\\]";
        String regex = "^(?<level>\\w*)\\s+(?<timestamp>\\d\\d\\s\\w{3}\\s\\d\\d:\\d\\d:\\d\\d)\\s(?<class>.*)#(?<method>\\<?\\w*\\>?)\\:(?<line>\\d+)\\sthread\\:(?<thread>[^\\s]*)\\s\\[(?<message>.*)\\]";
        RegexJSONParser instance = new RegexJSONParser();
        Object expResult = "{ \"level\" : \"INFO\", \"timestamp\" : \"06 Aug 16:27:09\", \"class\" : \"uk.co.revsys.oddball.consumer.kinesis.KinesisRecordProcessor\", \"method\" : \"processRecordsWithRetries\", \"line\" : \"98\", \"thread\" : \"pool-1-thread-1\", \"message\" : \"49540437523742567016182684081176742386203525543305936897, base, {\\\"accountId\\\":\\\"echo-central-master-account\\\",\\\"userId\\\":\\\"237fccd5-4a3b-4400-bb0d-ca872f15ab6d\\\",\\\"sessionId\\\":\\\"82e585db-4199-4a79-a763-1c6158e19db9\\\",\\\"domain\\\":\\\"dev.echo-central.com\\\",\\\"device\\\":{\\\"platform\\\":\\\"Win32\\\",\\\"screen\\\":{\\\"availWidth\\\":1600,\\\"availHeight\\\":860,\\\"availTop\\\":0,\\\"availLeft\\\":0,\\\"pixelDepth\\\":24,\\\"colorDepth\\\":24,\\\"width\\\":1600,\\\"height\\\":900},\\\"pixelRatio\\\":1},\\\"browser\\\":{\\\"vendor\\\":\\\"Google Inc.\\\",\\\"vendorSub\\\":\\\"\\\",\\\"product\\\":\\\"Gecko\\\",\\\"productSub\\\":\\\"20030107\\\",\\\"userAgent\\\":\\\"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36\\\",\\\"mimeTypes\\\":65,\\\"plugins\\\":21,\\\"language\\\":\\\"en-US\\\"},\\\"page\\\":{\\\"location\\\":{\\\"ancestorOrigins\\\":{\\\"length\\\":0},\\\"origin\\\":\\\"http://dev.echo-central.com\\\",\\\"hash\\\":\\\"\\\",\\\"search\\\":\\\"\\\",\\\"pathname\\\":\\\"/\\\",\\\"port\\\":\\\"\\\",\\\"hostname\\\":\\\"dev.echo-central.com\\\",\\\"host\\\":\\\"dev.echo-central.com\\\",\\\"protocol\\\":\\\"http:\\\",\\\"href\\\":\\\"http://dev.echo-central.com/\\\"},\\\"performance\\\":{\\\"loadEventEnd\\\":1407338818388,\\\"loadEventStart\\\":1407338818211,\\\"domComplete\\\":1407338818211,\\\"domContentLoadedEventEnd\\\":1407338815705,\\\"domContentLoadedEventStart\\\":1407338815703,\\\"domInteractive\\\":1407338815703,\\\"domLoading\\\":1407338815022,\\\"responseEnd\\\":1407338815084,\\\"responseStart\\\":1407338814994,\\\"requestStart\\\":1407338813385,\\\"secureConnectionStart\\\":0,\\\"connectEnd\\\":1407338813267,\\\"connectStart\\\":1407338813267,\\\"domainLookupEnd\\\":1407338813267,\\\"domainLookupStart\\\":1407338813267,\\\"fetchStart\\\":1407338813267,\\\"redirectEnd\\\":0,\\\"redirectStart\\\":0,\\\"unloadEventEnd\\\":1407338814995,\\\"unloadEventStart\\\":1407338814995,\\\"navigationStart\\\":1407338813267},\\\"title\\\":\\\"echoCHECK\\\"},\\\"connection\\\":{\\\"bandwidth\\\":\\\"not supported\\\",\\\"metered\\\":\\\"not supported\\\"},\\\"locale\\\":{\\\"country\\\":\\\"us\\\",\\\"lang\\\":\\\"en\\\"}}\" }";
        Object result = instance.parseString(input, regex);
        System.out.println(result);
        assertEquals(expResult, result);
    }


}
