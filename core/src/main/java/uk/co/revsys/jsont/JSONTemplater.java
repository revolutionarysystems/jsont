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

    public String evaluateTemplate(String template, String json) {
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        Mustache mustache = mustacheFactory.compile(new StringReader(template), "");
        StringWriter writer = new StringWriter();
        mustache.execute(writer, JSONValue.parse(json));
        return writer.toString();
    }
    
    public String evaluateTemplateFile(File templateFile, String json) throws IOException{
        String template = FileUtils.readFileToString(templateFile);
        return evaluateTemplate(template, json);
    }

}
