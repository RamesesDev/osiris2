package com.rameses.osiris2.web;

import com.rameses.osiris2.ExpressionProvider;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;


public class WebExpressionProvider implements ExpressionProvider {
    
    public Object eval(String expr, Map params) {
        expr = expr.replaceAll("#\\{|\\}","");
        Object bean = params.get("bean");
        GroovyShell ge = null;
        try {
            Binding binding = new Binding(BeanUtils.describe(bean));
            ge = new GroovyShell(binding);
            return ge.evaluate( expr );
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        } finally {
            ge = null;
        }
    }
    
}
