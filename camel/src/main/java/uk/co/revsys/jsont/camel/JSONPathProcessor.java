package uk.co.revsys.jsont.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import uk.co.revsys.jsont.JSONPathEvaluator;

public class JSONPathProcessor implements Processor{

    private JSONPathEvaluator jsonPathEvaluator = new JSONPathEvaluator();
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    @Override
    public void process(Exchange exchng) throws Exception {
        String json = exchng.getIn().getBody(String.class);
        Object result = jsonPathEvaluator.evaluate(json, getPath());
        exchng.getOut().setBody(result);
    }

}
