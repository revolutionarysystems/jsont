
package uk.co.revsys.jsont;

import uk.co.revsys.jsont.jexl.JEXLJSONPathEvaluator;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSONPathEvaluatorTest {

    public JSONPathEvaluatorTest() {
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

    @Test
    public void testEvaluateList() {
        String json = "[1, 2, 3]";
        String jpath = "$[1]";
        JSONPathEvaluator instance = new JSONPathEvaluator();
        Object expResult = 2;
        Object result = instance.evaluate(json, jpath);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testEvaluateNull() {
        String json = "{id:ddb76724-e58a-4903-91c0-fcae6f318493,name:ABC}";
        String jpath = "$.id";
        JSONPathEvaluator instance = new JSONPathEvaluator();
        Object expResult = "ddb76724-e58a-4903-91c0-fcae6f318493";
        Object result = instance.evaluate(json, jpath);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testEvaluateList2() {
        String json = "[1, 2, 3]";
        String jpath = "$";
        JSONPathEvaluator instance = new JSONPathEvaluator();
        Object result = instance.evaluate(json, jpath);
        assertTrue(result instanceof List);
    }
    
    @Test
    public void testEvaluateExpression() {
        String json = "{a: 1, b: 2, c:3, d: \"abc\\\\d\"}";
        String jpath = "eval(v1 + v2, $.a, $.b)";
        JSONPathEvaluator instance = new JEXLJSONPathEvaluator();
        Object result = instance.evaluate(json, jpath);
        assertEquals(3, result);
        assertEquals(6, instance.evaluate(json, "eval(v1 + v2 + v3, $.a, $.b, $.c)"));
        assertEquals(6, instance.evaluate(json, "eval(v1 * v2, $.b, $.c)"));
        assertEquals(12, instance.evaluate(json, "eval((v1 * v2) * v1, $.b, $.c)"));
        assertEquals(12, instance.evaluate(json, "eval((v1 * v2) * v1, $.b, $.c)"));
        assertEquals(4.0d, instance.evaluate(json, "eval(math.pow(v1, 2), $.b)"));
        assertEquals("", instance.evaluate(json, "eval(util.toStringSafe(v1), $.e)"));
        assertEquals("abcd", instance.evaluate(json, "eval(v1.replace('\\\\', ''), $.d)"));
        assertEquals("abcd,", instance.evaluate(json, "eval(v1.replace('\\\\', '') + ',', $.d)"));
    }

}