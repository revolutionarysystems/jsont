package uk.co.revsys.jsont.camel;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.language.ConstantExpression;
import org.apache.camel.model.language.SimpleExpression;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class JSONComponentTest extends CamelTestSupport {

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;
    
    @Test
    public void test() throws Exception {
        template.sendBody("{a:1, b:2}");
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMessageCount(1);
        //mock.expectedBodiesReceived(1); // Not sure why this isn't working
        assertMockEndpointsSatisfied();
        assertEquals(1, mock.getExchanges().get(0).getIn().getBody());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("direct:start")
                        .to("json:jsonpath?path=$.a")
                        .to("log:debug")
                        .to("mock:result");
            }
        };
    }
}
