package com.rameses.rules;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.util.HashMap;
import java.util.Map;
import org.drools.base.DefaultKnowledgeHelper;


public final class Expression {
    
    private String expression;
    private Map params;
    
    public Expression(String expr) {
        this.expression = expr;
        this.params = new HashMap();
    }
    
    public Expression add(String name, Object o ) {
        params.put(name, o);
        return this;
    }
    
    public Object eval() {
        Binding b = new Binding(params);
        GroovyShell shell = new GroovyShell(b);
        Object o = shell.evaluate( expression );
        shell = null;
        return o;
    }
    
    
}




