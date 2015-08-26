package uk.co.revsys.jsont;

import com.jayway.jsonpath.PathNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import net.minidev.json.JSONArray;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class JSONTransformer {

    private JSONPathEvaluator jsonPathEvaluator;

    public JSONTransformer() {
        this.jsonPathEvaluator = new JSONPathEvaluator();
    }

    public JSONTransformer(JSONPathEvaluator jsonPathEvaluator) {
        this.jsonPathEvaluator = jsonPathEvaluator;
    }

    public String transformFromFile(String source, File transform, Map parameters) throws IOException {
        return transform(source, FileUtils.readFileToString(transform), parameters);
    }

    public String transform(String source, String transform, Map parameters) {
        JSONObject transformJSON = new JSONObject(transform);
        Object key = transformJSON.keySet().toArray()[0];
        String jpath = (String) key;
        String result = transform(source, transformJSON, jpath, parameters);
        if (result.startsWith("{")) {
            return new JSONObject(result).toString();
        } else if (result.startsWith("[")) {
            return new org.json.JSONArray(result).toString();
        } else {
            return result;
        }
    }

    private String transform(String source, JSONObject transformJSON, String jpath, Map parameters) {
        Object templateObject = transformJSON.get(jpath);
        Object match = jsonPathEvaluator.evaluate(source, jpath);
        return transform(source, transformJSON, jpath, templateObject, match, parameters);
    }

    private String transform(String source, JSONObject transform, String jpath, Object templateObject, Object match, Map parameters) {
        if (match instanceof net.minidev.json.JSONObject) {
            if (templateObject instanceof String) {
                return parseString(source, transform, jpath, (String) templateObject, parameters);
            } else if (templateObject instanceof JSONObject) {
                JSONObject template = (JSONObject) templateObject;
                if (template.length() == 0) {
                    return "{}";
                }
                String result = "{";
                for (Object k : template.keySet()) {
                    String key = (String) k;
                    if (key.startsWith("$copy")) {
                        String path = template.getString(key);
                        try {
                            net.minidev.json.JSONObject target = (net.minidev.json.JSONObject) jsonPathEvaluator.evaluate(source, path);
                            String targetString = target.toString();
                            targetString = targetString.substring(1, targetString.length() - 1);
                            result = result + targetString + ",";
                        } catch (PathNotFoundException ex) {
                            // Fail silently
                        }
                    } else {
                        Object templatePartial = template.get(key);
                        result = result + "\"" + key + "\":" + transform(source, transform, jpath, templatePartial, match, parameters) + ",";
                    }
                }
                return result.substring(0, result.length() - 1) + "}";
            }
            return null;
        } else if (match instanceof JSONArray) {
            JSONArray jarray = (JSONArray) match;
            if (jarray.isEmpty()) {
                return "[]";
            }
            String result = "[";
            for (int i = 0; i < jarray.size(); i++) {
                Object item = jarray.get(i);
                result = result + transform(source, transform, jpath + "[" + i + "]", templateObject, item, parameters) + ",";
            }
            return result.substring(0, result.length() - 1) + "]";
        } else {
            return null;
        }
    }

    private String parseString(String json, JSONObject transform, String path, String s, Map parameters) {
        boolean wrapInQuotes = false;
        while (s.contains("{{")) {
            String placeholder = s.substring(s.indexOf("{{") + 2);
            placeholder = placeholder.substring(0, placeholder.indexOf("}}"));
            String jpath = placeholder;
            if (jpath.startsWith("$$")) {
                jpath = jpath.substring(1);
            } else if (jpath.startsWith("$")) {
                jpath = path + jpath.substring(1);
            }
            if (transform.has(jpath) && !jpath.equals(path)) {
                return transform(json, transform, jpath, parameters);
            }
            Object result;
            try {
                result = jsonPathEvaluator.evaluate(json, jpath);
            } catch (PathNotFoundException ex) {
                result = null;
            }
            if (result == null) {
                result = "null";
            } else if (result instanceof String) {
                wrapInQuotes = true;
            }
            s = s.replace("{{" + placeholder + "}}", result.toString());
        }
        while (s.contains("${")) {
            String placeholder = s.substring(s.indexOf("${") + 2);
            placeholder = placeholder.substring(0, placeholder.indexOf("}"));
            Object replacement = parameters.get(placeholder);
            if (replacement == null) {
                replacement = "null";
            } else if (replacement instanceof String) {
                wrapInQuotes = true;
            }
            s = s.replace("${" + placeholder + "}", replacement.toString());
        }
        if (wrapInQuotes) {
            s = "\"" + s + "\"";
        }
        return s;
    }

}
