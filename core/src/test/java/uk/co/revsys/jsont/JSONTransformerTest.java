package uk.co.revsys.jsont;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.co.revsys.jsont.jexl.JEXLJSONPathEvaluator;

public class JSONTransformerTest {

    public JSONTransformerTest() {
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
     * Test of transform method, of class JSONTransformer.
     */
    @Test
    public void testTransform() throws Exception{
        JSONTransformer instance = new JSONTransformer(new JSONPathEvaluator());
        String source = "{\"a\": 1}";
        String transform = "{\"$\": {\"type\": \"Test\", \"a\": \"{{$.a}}\"}}";
        String expResult = "{\"a\": 1, \"type\": \"Test\"}";
        String result = instance.transform(source, transform, null);
        assertTrue(result.contains("\"Test\""));
        assertEquals(new JSONObject(expResult).toString(), new JSONObject(result).toString());
        source = "{\"accountName\": \"Test Account\", \"accountId\": 9876, \"user\": {\"firstName\": \"Test\", \"surname\": \"User\"}}";
        transform = "{\"$\": {\"name\": \"{{$.accountName}}\", \"id\": \"{{$.accountId}}\", \"userid\": \"${userid}\", \"p1\": \"{{$.p1}}\", \"p2\": \"${p2}\", \"p3\": \"static\"}}";
        expResult = "{\"name\": \"Test Account\", \"id\": 9876, \"userid\": \"1234\", \"p1\": null, \"p2\": null, \"p3\": \"static\"}";
        Map parameters = new HashMap();
        parameters.put("userid", "1234");
        result = instance.transform(source, transform, parameters);
        assertEquals(new JSONObject(expResult).toString(), new JSONObject(result).toString());
        transform = "{\"$\": {\"accountName\": \"{{$.accountName}} 1\", \"user\": \"{{$.user}}\"}, \"$.user\": {\"id\": \"${userid}\", \"name\": \"{{$.firstName}} {{$.surname}}\"}}";
        expResult = "{\"accountName\": \"Test Account 1\", \"user\": {\"id\": \"1234\", \"name\": \"Test User\"}}";
        result = instance.transform(source, transform, parameters);
        assertEquals(new JSONObject(expResult).toString(), new JSONObject(result).toString());
        transform = "{\"$.user\": \"{{$}}\"}";
        expResult = "{\"firstName\": \"Test\", \"surname\": \"User\"}";
        result = instance.transform(source, transform, null);
        assertEquals(new JSONObject(expResult).toString(), new JSONObject(result).toString());
        instance = new JSONTransformer(new JEXLJSONPathEvaluator());
        transform = "{\"$\": {\"id\": \"{{eval(v1 + v1, $.accountId)}}\"}}";
        expResult = "{\"id\": 19752}";
        result = instance.transform(source, transform, null);
        assertEquals(new JSONObject(expResult).toString(), new JSONObject(result).toString());
        source = "[{\"value\": 1}, {\"value\": 2}]";
        transform = "{\"$\": {\"test\": \"{{$.value}}\"}}";
        expResult = "[{\"test\": 1}, {\"test\": 2}]";
        result = instance.transform(source, transform, null);
        System.out.println("result = " + result);
        assertEquals(new JSONArray(expResult).toString(), new JSONArray(result).toString());
        source = "{\"value1\": 1, \"value2\": 2}";
        transform = "{\"$\": {\"data\": {\"$copy\": \"$\"}}}";
        expResult = "{data: {\"value1\": 1, \"value2\": 2}}";
        result = instance.transform(source, transform, null);
        System.out.println("result = " + result);
        assertEquals(new JSONObject(expResult).toString(), new JSONObject(result).toString());
        source = "[    {        \"timestamp\": \"1421062457181\",        \"ruleSet\": \"interventions.rules\",        \"case\": {            \"timestamp\": \"1421062457172\",            \"ruleSet\": \"triggers\",            \"case\": {                \"owner\": null,                \"country\": {                    \"distribution\": [                        {                            \"relFreq\": 1,                            \"count\": 2,                            \"value\": \"GB\"                        }                    ]                },                \"city\": {                    \"distribution\": [                        {                            \"relFreq\": 1,                            \"count\": 2,                            \"value\": \"petersfield\"                        }                    ]                },                \"count\": 2,                \"ipAddress\": {                    \"distribution\": [                        {                            \"relFreq\": 1,                            \"count\": 2,                            \"value\": \"86.159.156.24\"                        }                    ]                },                \"sessionId\": {                    \"distribution\": [                        {                            \"relFreq\": 1,                            \"count\": 2,                            \"value\": \"054aadd7-0474-4f93-d627-a632fa98e195\"                        }                    ]                },                \"userId\": {                    \"distribution\": [                        {                            \"relFreq\": 1,                            \"count\": 2,                            \"value\": \"3dd06c22-c9df-4dbd-969b-960c2b1d0529\"                        }                    ]                },                \"duration\": 604800000,                \"fingerprint\": {                    \"distribution\": [                        {                            \"relFreq\": 1,                            \"count\": 2,                            \"value\": \"1,900,1600,Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36,petersfield,en-US,en;q=0.8,af;q=0.6\"                        }                    ]                },                \"customerId\": {                    \"distribution\": [                        {                            \"relFreq\": 1,                            \"count\": 1,                            \"value\": \"abc123\"                        },                        {                            \"relFreq\": 1,                            \"count\": 1,                            \"value\": \"xyz789\"                        }                    ]                },                \"startTime\": 1420976057159,                \"endTime\": 1421580857159,                \"clientTimeOffset\": {                    \"ave\": \"-2897.0\",                    \"total\": \"-5794.0\",                    \"std\": \"45.254833995939045\",                    \"nonNulls\": \"2\",                    \"min\": \"-2929.0\",                    \"max\": \"-2865.0\",                    \"var\": \"2048.0\",                    \"sumsquares\": \"1.6787266E7\"                }            },            \"tags\": [                \"trigger.duplicateCustomerId\"            ],            \"derived\": {                \"trigger\": \"duplicateCustomerId\"            }        },        \"tags\": [            \"action.alert\"        ],        \"derived\": {            \"action\": \"alert\"        }    }]";
        transform = "{  \"$\": {   \"sessionId\": \"{{$.case.case.sessionId.distribution.[0].value}}\",   \"sessionId1\": \"{{$.case.case.sessionId}}\",   \"sessionId2\": \"{{$.timestamp}}\",   \"userId\": \"{{$.case.userId}}\",   \"accountId\": \"{{$.case.accountId}}\",   \"agent-ip\": \"{{$.case.ipAddress}}\",   \"time\": \"{{$.case.time}}\",   \"location\": \"{{$.case.location}}\",  } }";
        result = instance.transform(source, transform, null);
        System.out.println("result = " + result);
    }


}