package com.example.file_processing.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ExpressionEvaluator {

    public static double evaluate(String expression) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return ((Number) engine.eval(expression)).doubleValue();
        } catch (Exception e) {
            throw new RuntimeException("Error evaluating expression: " + expression, e);
        }
    }
}