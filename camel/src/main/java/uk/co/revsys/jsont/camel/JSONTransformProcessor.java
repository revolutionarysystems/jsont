package uk.co.revsys.jsont.camel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import uk.co.revsys.jsont.JSONTransformer;

public class JSONTransformProcessor implements Processor{

    private JSONTransformer transformer = new JSONTransformer();
    
    private File transform;

    public File getTransform() {
        return transform;
    }

    public void setTransform(File transform) {
        this.transform = transform;
    }
    
    @Override
    public void process(Exchange exchange) throws Exception {
        String source = exchange.getIn().getBody(String.class);
        Map<String, Object> parameters = new HashMap<String, Object>(exchange.getIn().getHeaders());
        parameters.putAll(exchange.getProperties());
        System.out.println(getTransform().getAbsolutePath());
        String result = transformer.transformFromFile(source, getTransform(), parameters);
        exchange.getIn().setBody(result);
    }

}
