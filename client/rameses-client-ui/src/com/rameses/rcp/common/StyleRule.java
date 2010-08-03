package com.rameses.rcp.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StyleRule implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String pattern;
    private Map properties = new LinkedHashMap();
    private String expression;
    
    public StyleRule() {
        
    }
    
    public StyleRule(String pattern, String expr) {
        this.pattern = pattern;
        this.expression = expr;
    }
    
    public StyleRule(String pattern, String expr, Map props) {
        this.pattern = pattern;
        this.expression = expr;
        this.properties = props;
    }
    
    public void destroy() {
        pattern = null;
        properties.clear();
        properties = null;
        expression = null;
    }
    
    public String getPattern() {
        return pattern;
    }
    
    public Map getProperties() {
        return properties;
    }
    
    public String getExpression() {
        return expression;
    }
    
    public StyleRule clone() {
        StyleRule r = new StyleRule();
        r.pattern = pattern;
        r.properties = properties;
        r.expression = expression;
        return r;
    }
    
    public StyleRule add(String name, Object property) {
        properties.put( name, property );
        return this;
    }
    
    public String toString() {
        return "{pattern: \"" + pattern + "\", " +
                "expression: \"" + expression + "\", " +
                "properties: " + properties + "}";
    }
    
}
