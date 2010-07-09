/*
 * OsirisAppContext.java
 *
 * Created on April 28, 2010, 7:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client;

import com.rameses.osiris2.AppContext;
import com.rameses.osiris2.CodeProvider;
import com.rameses.osiris2.ExpressionProvider;
import com.rameses.osiris2.SecurityProvider;
import com.rameses.osiris2.SessionContext;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class OsirisAppContext extends AppContext {
    
    private ClassLoader classLoader ;
    private ExpressionProvider expressionProvider;
    private CodeProvider codeProvider;
    
    public OsirisAppContext(ClassLoader loader) {
        this.classLoader = loader;
        if(classLoader==null)
            classLoader = Thread.currentThread().getContextClassLoader();
        codeProvider = new GroovyControllerProvider(classLoader);
        expressionProvider = new ClientExpressionProvider();
    }
    
    public ExpressionProvider getExpressionProvider() {
        return expressionProvider;
    }
    
    public ClassLoader getClassLoader() {
        return classLoader;
    }
    
    public CodeProvider getCodeProvider() {
        return codeProvider;
    }
    
    public void setEnvMap(Map map) {
        for(Object o: map.entrySet()) {
            Map.Entry me = (Map.Entry)o;
            env.put(me.getKey(),me.getValue());
        }
    }
    
    public SessionContext createSession() {
        SessionContext ctx = new OsirisSessionContext(this);
        SecurityProvider p = (SecurityProvider)env.get(SecurityProvider.class);
        if(p!=null) ctx.setSecurityProvider( p );
        return ctx;
    }
    
    
}
