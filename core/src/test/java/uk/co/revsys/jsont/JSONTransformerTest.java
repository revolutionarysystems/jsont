
package uk.co.revsys.jsont;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testTransform() {
        String source = "{\"accountName\": \"Test Account\", \"accountId\": 9876, \"user\": {\"firstName\": \"Test\", \"surname\": \"User\"}}";
        String transform = "{\"$\": {\"name\": \"{{$.accountName}}\", \"id\": \"{{$.accountId}}\", \"userid\": \"${userid}\", \"p1\": \"{{$.p1}}\", \"p2\": \"${p2}\"}}";
        JSONTransformer instance = new JSONTransformer();
        String expResult = "{\"name\": \"Test Account\", \"id\": 9876, \"userid\": \"1234\", \"p1\": null, \"p2\": null}";
        Map parameters = new HashMap();
        parameters.put("userid", "1234");
        String result = instance.transform(source, transform, parameters);
        assertEquals(new JSONObject(expResult).toString(), new JSONObject(result).toString());
        transform = "{\"$\": {\"accountName\": \"{{$.accountName}} 1\", \"user\": \"{{$.user}}\"}, \"$.user\": {\"id\": \"${userid}\", \"name\": \"{{firstName}} {{surname}}\"}}";
        expResult = "{\"accountName\": \"Test Account 1\", \"user\": {\"id\": \"1234\", \"name\": \"Test User\"}}";
        result = instance.transform(source, transform, parameters);
        System.out.println("result = " + result);
        assertEquals(new JSONObject(expResult).toString(), new JSONObject(result).toString());
        transform = "{\"$.user\": \"{{$}}\"}";
        expResult = "{\"firstName\": \"Test\", \"surname\": \"User\"}";
        result = instance.transform(source, transform, null);
        assertEquals(new JSONObject(expResult).toString(), new JSONObject(result).toString());
    }

}