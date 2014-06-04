package uk.co.revsys.jsont;

import com.jayway.jsonpath.JsonPath;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class JSONTransformer {

    public String transformFromFile(String source, File transform) throws IOException{
        return transform(source, FileUtils.readFileToString(transform));
    }
    
    public String transform(String source, String transform){
        JSONObject transformJSON = new JSONObject(transform);
        StringBuilder result = new StringBuilder();
        for(Object key: transformJSON.keySet()){
            String jpath = (String)key;
            String template = transformJSON.get(jpath).toString();
            List<String> matches = new LinkedList();
            Object jmatch = JsonPath.read(source, jpath);
            if(jmatch instanceof net.minidev.json.JSONObject){
                matches.add(jmatch.toString());
            }else{
                matches = (List)jmatch;
            }
            for(String match: matches){
                String partial = template;
                while(partial.contains("{{")){
                    String placeholder = partial.substring(partial.indexOf("{{")+2);
                    placeholder = placeholder.substring(0, placeholder.indexOf("}}"));
                    Object replacement = JsonPath.read(match, placeholder);
                    partial = partial.replace("{{" + placeholder + "}}", replacement.toString());
                }
                result.append(partial);
            }
        }
        return result.toString();
    }
    
}
