package uk.co.revsys.jsont;

import com.jayway.jsonpath.JsonPath;

public class JSONPathEvaluator {

    public Object evaluate(String json, String jpath){
        return JsonPath.read(json, jpath);
    }
    
}
