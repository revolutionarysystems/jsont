package uk.co.revsys.jsont.camel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.minidev.json.JSONValue;
import org.apache.commons.io.FileUtils;
import uk.co.revsys.jsont.JSONTemplater;

public class JSONCamelTemplater extends JSONTemplater{

    public String evaluateMessage(String json, Map<String, Object> headers, String template){
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("body", JSONValue.parse(json));
        data.put("headers", headers);
        return evaluate(data, template);
    }
    
    public String evaluateMessageUsingFile(String json, Map<String, Object> headers, File templateFile) throws IOException{
        String template = FileUtils.readFileToString(templateFile);
        return evaluateMessage(json, headers, template);
    }
    
}
