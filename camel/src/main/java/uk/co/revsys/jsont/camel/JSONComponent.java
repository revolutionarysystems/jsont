package uk.co.revsys.jsont.camel;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.ProcessorEndpoint;

/**
 * Represents the component that manages {@link HelloWorldEndpoint}.
 */
public class JSONComponent extends DefaultComponent {

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        ProcessorEndpoint endpoint = new ProcessorEndpoint(uri, this, new JSONPathProcessor());
        setProperties(endpoint.getProcessor(), parameters);
        return endpoint;
    }
}
