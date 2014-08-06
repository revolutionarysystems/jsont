package uk.co.revsys.jsont;

import com.jayway.jsonpath.JsonPath;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class JSONTransformer {

    public String transformFromFile(String source, File transform, Map parameters) throws IOException {
        return transform(source, FileUtils.readFileToString(transform), parameters);
    }

    public String transform(String source, String transform, Map parameters) {
        JSONObject transformJSON = new JSONObject(transform);
        Object key = transformJSON.keySet().toArray()[0];
        String jpath = (String) key;
        return transform(source, transformJSON, jpath, parameters);
    }

    private String transform(String source, JSONObject transformJSON, String jpath, Map parameters) {
        String template;
        Object templateObject = transformJSON.get(jpath);
        if (templateObject instanceof String) {
            template = "\"" + transformJSON.get(jpath).toString() + "\"";
        } else {
            template = transformJSON.get(jpath).toString();
        }
        StringBuilder result = new StringBuilder();
        List<String> matches = new LinkedList();
        Object jmatch = JsonPath.read(source, jpath);
        if (jmatch instanceof net.minidev.json.JSONObject) {
            matches.add(jmatch.toString());
        } else {
            matches = (List) jmatch;
        }
        for (String match : matches) {
            String partial = template;
            System.out.println("partial = " + partial);
            while (partial.contains("{{")) {
                boolean hasQuoteAtStart = false;
                boolean hasQuoteAtEnd = false;
                boolean wrappedInQuotes = false;
                String prefix = "{{";
                String suffix = "}}";
                int startIndex = partial.indexOf("{{");
                if (startIndex > 0 && partial.charAt(startIndex - 1) == '\"') {
                    hasQuoteAtStart = true;
                }
                String placeholder = partial.substring(startIndex + 2);
                int endIndex = placeholder.indexOf("}}");
                if (endIndex < placeholder.length() && placeholder.charAt(endIndex + 2) == '\"') {
                    hasQuoteAtEnd = true;
                    wrappedInQuotes = hasQuoteAtStart;
                    if (wrappedInQuotes) {
                        prefix = "\"{{";
                        suffix = "}}\"";
                    }
                }
                placeholder = placeholder.substring(0, endIndex);
                System.out.println("placeholder = " + placeholder);
                String replacementString;
                if (transformJSON.has(placeholder)) {
                    replacementString = transform(source, transformJSON, placeholder, parameters);
                } else {
                    try {
                        Object replacement = JsonPath.read(match, placeholder);
                        replacementString = parseObjectToString(replacement, wrappedInQuotes);
                    }
                    catch (com.jayway.jsonpath.PathNotFoundException ex){
                        replacementString = "null";
                    }
                }
                partial = partial.replace(prefix + placeholder + suffix, replacementString);
            }
            while (partial.contains("\"${")) {
                String placeholder = partial.substring(partial.indexOf("\"${") + 3);
                placeholder = placeholder.substring(0, placeholder.indexOf("}\""));
                Object replacement = parameters.get(placeholder);
                partial = partial.replace("\"${" + placeholder + "}\"", parseObjectToString(replacement, true));
            }
            result.append(partial);
        }
        return result.toString();
    }

    private String parseObjectToString(Object object, boolean wrapInQuotes) {
        if (object == null) {
            return "null";
        } else if (object instanceof String) {
            if (wrapInQuotes) {
                return "\"" + object.toString() + "\"";
            } else {
                return object.toString();
            }
        } else {
            return object.toString();
        }
    }

}
