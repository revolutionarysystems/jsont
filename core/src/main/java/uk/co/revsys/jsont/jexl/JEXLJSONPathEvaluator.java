package uk.co.revsys.jsont.jexl;

import com.jayway.jsonpath.JsonPath;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import uk.co.revsys.jsont.JSONPathEvaluator;

public class JEXLJSONPathEvaluator extends JSONPathEvaluator{

    private final JexlEngine jexlEngine;
    private final Map<String, Object> evalParameters; 

    public JEXLJSONPathEvaluator() {
        this(new HashMap<String, Object>());
    }
    
    public JEXLJSONPathEvaluator(Map<String, Object> evalParameters) {
        jexlEngine = new JexlEngine();
        this.evalParameters = new HashMap<String, Object>();
        this.evalParameters.put("util", new CoreUtil());
        this.evalParameters.put("math", new MathUtil());
        this.evalParameters.putAll(evalParameters);
    }

    @Override
    public Object evaluate(String json, String jpath) {
        if(jpath.startsWith("eval(")){
            return evaluateEval(json, jpath);
        }
        return JsonPath.read(json, jpath);
    }
    
    private Object evaluateEval(String json, String expression){
        expression = expression.substring(5, expression.length()-1);
        JexlContext context = new MapContext();
        for(Entry<String, Object> evalParameter: evalParameters.entrySet()){
            context.set(evalParameter.getKey(), evalParameter.getValue());
        }
        int i = 1;
        List<String> jpaths = new LinkedList<String>();
        while(expression.contains("$")){
            int index = expression.lastIndexOf("$");
            String jpath = expression.substring(index);
            jpaths.add(jpath);
            if(expression.charAt(index-1) == ','){
                expression = expression.substring(0, index-1);
            }else if(expression.charAt(index-2) == ','){
                expression = expression.substring(0, index-2);
            }else{
                throw new RuntimeException();
            }
        }
        Collections.reverse(jpaths);
        for(String jpath: jpaths){
            Object result = evaluate(json, jpath);
            System.out.println("result = " + result);
            context.set("v" + i, result);
            i = i + 1;
        }
        System.out.println("expression = " + expression);
        Expression exp = jexlEngine.createExpression(expression);
        Object result = exp.evaluate(context);
        System.out.println("result = " + result);
        return result;
    }

}
