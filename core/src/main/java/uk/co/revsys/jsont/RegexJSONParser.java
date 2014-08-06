package uk.co.revsys.jsont;

import com.jayway.jsonpath.JsonPath;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexJSONParser {

//    public String parse(String input, String regex, String nameList){
    public String parseString(String input, String regex){
        Pattern parsePattern = Pattern.compile(regex);
        System.out.println(input);
        Matcher m = parsePattern.matcher(input);
        ArrayList<String>names = extractNames(regex);
        System.out.println(m.toString());
        System.out.println(names);
        if (m.matches()){
            System.out.println("Match");
            StringBuilder json = new StringBuilder("{ ");
            for (String name : names){
                json.append("\""+name+"\" : \""+m.group(name)+"\", ");
            }
            if (json.length()>3){
                json.delete(json.lastIndexOf(","), json.length());
            }
            json.append(" }");
            return json.toString();
        } else {
            System.out.println("No match");
            return null;
        }
    }
    
    public ArrayList<String> extractNames(String regex){
        Pattern parsePattern = Pattern.compile("\\(\\?.(\\w*).");
        Matcher m = parsePattern.matcher(regex);
        ArrayList<String> result = new ArrayList<String>();
        while (m.find()){
            result.add(m.group(1));
        }
        return result;
    }
    
}
