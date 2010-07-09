package com.rameses.osiris2.web;

import com.rameses.osiris2.AppContext;
import com.rameses.osiris2.CodeProvider;
import com.rameses.osiris2.ExpressionProvider;
import com.rameses.osiris2.SessionContext;

/**
 *
 * @author jaycverg
 */
public class OsirisWebAppContext extends AppContext {
    
    private ClassLoader classLoader;
    private ExpressionProvider exprProvider = new WebExpressionProvider();
    private CodeProvider codeProvider;
    
    /** Creates a new instance of OsirisWebAppContext */
    public OsirisWebAppContext(ClassLoader loader) {
        super();
        classLoader = loader;
        codeProvider = new GroovyCodeProvider(loader);
    }
    
    public ExpressionProvider getExpressionProvider() {
        return exprProvider;
    }
    
    public ClassLoader getClassLoader() {
        return classLoader;
    }
    
    public CodeProvider getCodeProvider() {
        return codeProvider;
    }

    public SessionContext createSession() {
        return new OsirisWebSessionContext(this);
    }
        
}
