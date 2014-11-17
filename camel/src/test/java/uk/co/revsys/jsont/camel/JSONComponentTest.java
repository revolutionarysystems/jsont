package uk.co.revsys.jsont.camel;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.language.ConstantExpression;
import org.apache.camel.model.language.SimpleExpression;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.json.JSONObject;
import org.junit.Test;

public class JSONComponentTest extends CamelTestSupport {

    @Produce(uri = "direct:jsonpath")
    protected ProducerTemplate jsonPathTemplate;
    
    @Produce(uri = "direct:jsont")
    protected ProducerTemplate jsonTransformTemplate;
    
    @Test
    public void testJSONPath() throws Exception {
        jsonPathTemplate.sendBody("{a:1, b:2}");
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMessageCount(1);
        assertMockEndpointsSatisfied();
        assertEquals(1, mock.getExchanges().get(0).getIn().getBody());
    }
    
    @Test
    public void testJSONTransform() throws Exception {
        jsonTransformTemplate.sendBody("{a:1, b:2, c:3}");
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMessageCount(1);
        assertMockEndpointsSatisfied();
        JSONObject expectedResult = new JSONObject("{x:1, y:2, z:3}");
        assertEquals(expectedResult.toString(), new JSONObject(mock.getExchanges().get(0).getIn().getBody(String.class)).toString());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("direct:jsonpath")
                        .to("json:path?path=$.a")
                        .to("log:debug")
                        .to("mock:result");
                from("direct:jsont")
                        .to("json:transform?transform=src/test/resources/transform.json")
                        .to("log:debug")
                        .to("mock:result");
            }
        };
    }
}
