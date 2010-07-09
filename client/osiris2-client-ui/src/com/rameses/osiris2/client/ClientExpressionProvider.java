package com.rameses.osiris2.client;

import com.rameses.rcp.framework.ClientContext;
import com.rameses.osiris2.ExpressionProvider;
import java.util.Map;


public class ClientExpressionProvider implements ExpressionProvider {
    
    public Object eval(String expr, Map params) {
        Object bean = params.get("bean");
        return ClientContext.getCurrentContext().getExpressionResolver().evaluate( bean, expr );
        /*
        Binding binding = new Binding(params);
        GroovyShell ge = new GroovyShell(binding);        
        try {
            return ge.evaluate( expr );
        }
        catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
        finally {
            ge = null;
        }
         */
    }
    
}
