
package uk.co.revsys.jsont;

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
        String source = "{\"accountName\": \"Test Account\", \"user\": {\"firstName\": \"Test\", \"surname\": \"User\"}}";
        String transform = "{\"$\": {\"name\": \"{{$.accountName}}\"}}";
        JSONTransformer instance = new JSONTransformer();
        String expResult = "{\"name\": \"Test Account\"}";
        String result = instance.transform(source, transform);
        assertEquals(new JSONObject(expResult).toString(), new JSONObject(result).toString());
        transform = "{\"$.user\": \"{{$}}\"}";
        expResult = "{\"firstName\": \"Test\", \"surname\": \"User\"}";
        result = instance.transform(source, transform);
        assertEquals(new JSONObject(expResult).toString(), new JSONObject(result).toString());
    }

}