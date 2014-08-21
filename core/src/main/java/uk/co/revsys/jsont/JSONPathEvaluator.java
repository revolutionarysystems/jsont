package uk.co.revsys.jsont;

import com.jayway.jsonpath.JsonPath;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

public class JSONPathEvaluator {

    private JexlEngine jexlEngine;

    public JSONPathEvaluator() {
        jexlEngine = new JexlEngine();
    }

    public Object evaluate(String json, String jpath) {
        if(jpath.startsWith("eval(")){
            return evaluateEval(json, jpath);
        }
        return JsonPath.read(json, jpath);
    }
    
    private Object evaluateEval(String json, String expression){
        expression = expression.substring(5, expression.length()-1);
        JexlContext context = new MapContext();
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
            context.set("v" + i, result);
            i = i + 1;
        }
        Expression exp = jexlEngine.createExpression(expression);
        Object result = exp.evaluate(context);
        return result;
    }

}
