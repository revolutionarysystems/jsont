JSONT
=====

== Using the JEXL Path Evaluator

The JEXL path evaluator allows you to evaluate expressions against your json data.

Examples

Given the following json data

```
{a: 1, b: 2, c: 3, d: 'testing'}
```

The following json paths are possible

```sh
eval(v1 + v2, $.a, $.b) # returns 3
eval(v1 * v2, $.b, $.c) # returns 6
eval(v1.substring(0, 4), $.d) # returns 'test'
```

You can find more information on exactly what is possible with JEXL here

http://commons.apache.org/proper/commons-jexl/reference/syntax.html

To make things easier you can also add helper classes to use in your json expressions by passing a map of objects you want available to the constructor of the JEXLPathEvaluator.

For example lets add a helper class to convert a date in milliseconds to a string.

First we create the helper class

```java
public class DateUtils{
	
	public String format(long ms, String format){
		Date date = new Date(ms);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

}
```

Then pass it to the constructor of our path evaluator

```java
Map evalFunctions = new HashMap();
evalFunctions.put("dateUtils", new DateUtils());
JSONPathEvaluator pathEvaluator = new JEXLJSONPathEvaluator(evalFunctions);
```

We can now use the following eval expression in our json path

```sh
eval(dateUtils.format(v1, 'dd/MM/yyyy'), $.myDate)
```
