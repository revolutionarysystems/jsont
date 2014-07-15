
package uk.co.revsys.jsont;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSONTemplaterTest {

    public JSONTemplaterTest() {
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
     * Test of evaluate method, of class JSONTemplater.
     */
    @Test
    public void testEvaluate() {
        String template = "p1 = {{p1}}";
        String json = "{'p1': 'v1'}";
        JSONTemplater instance = new JSONTemplater();
        String expResult = "p1 = v1";
        String result = instance.evaluateJSON(json, template);
        assertEquals(expResult, result);
    }

}