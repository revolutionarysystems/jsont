package uk.co.revsys.jsont.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import uk.co.revsys.esb.component.ParameterAwareProcessor;
import uk.co.revsys.jsont.JSONPathEvaluator;

public class JSONPathProcessor implements Processor, ParameterAwareProcessor{

    private JSONPathEvaluator jsonPathEvaluator = new JSONPathEvaluator();
    private String path;
    private String header;
    private String property;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    @Override
    public void process(Exchange exchange) throws Exception {
        String json = exchange.getIn().getBody(String.class);
        Object result = jsonPathEvaluator.evaluate(json, getPath());
        if(getHeader()!=null){
            exchange.getIn().setHeader(getHeader(), result);
        }else if(getProperty()!=null){
            exchange.setProperty(getProperty(), result);
        }else{
            exchange.getIn().setBody(result);
        }
    }

}
