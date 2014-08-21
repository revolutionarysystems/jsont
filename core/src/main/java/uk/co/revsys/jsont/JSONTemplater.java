package uk.co.revsys.jsont;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import net.minidev.json.JSONValue;
import org.apache.commons.io.FileUtils;

public class JSONTemplater {

    MustacheFactory mustacheFactory = new DefaultMustacheFactory();
    
    public String evaluateJSON(String json, String template) {
        return evaluate(JSONValue.parse(json), template);
    }
    
    public String evaluateJSONUsingFile(String json, File templateFile) throws IOException{
        String template = FileUtils.readFileToString(templateFile);
        return evaluateJSON(json, template);
    }
    
    protected String evaluate(Object data, String template){
        Mustache mustache = mustacheFactory.compile(new StringReader(template), "");
        StringWriter writer = new StringWriter();
        mustache.execute(writer, data);
        return writer.toString();
    }

}
