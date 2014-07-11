
package uk.co.revsys.jsont;

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

}