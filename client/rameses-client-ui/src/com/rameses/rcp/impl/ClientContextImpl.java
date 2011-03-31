package com.rameses.rcp.impl;
import com.rameses.platform.interfaces.Platform;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ClientSecurityProvider;
import com.rameses.rcp.framework.ResourceProvider;
import com.rameses.common.ExpressionResolver;
import com.rameses.common.MethodResolver;
import com.rameses.common.PropertyResolver;
import com.rameses.common.ValueResolver;
import java.util.Locale;

/**
 *
 * @author jaycverg
 * Default Implementation of ClientContext
 */
public class ClientContextImpl extends ClientContext {
    
    private Platform platform;
    private PropertyResolver propResolver = new PropertyResolverImpl();
    private MethodResolver methodResolver = new MethodResolverImpl();
    private ClassLoader loader = Thread.currentThread().getContextClassLoader();
    private Locale locale = new Locale("ph", "PH");
    private ExpressionResolver expressResolver = new ExpressionResolverImpl();
    private ClientSecurityProvider  secProvider = new SecurityProviderImpl();
    private ResourceProvider resProvider = new ResourceProviderImpl();
    
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setter  ">
    public ValueResolver getValueResolver() {
        return null;
    }
    
    public MethodResolver getMethodResolver() {
        return methodResolver;
    }
    
    public void setMethodResolver(MethodResolver mr) {
        this.methodResolver = mr;
    }
    
    public ExpressionResolver getExpressionResolver() {
        return expressResolver;
    }
        
    public void setExpressionResolver(ExpressionResolver er) {
        this.expressResolver = er;
    }
    
    public PropertyResolver getPropertyResolver() {
        return propResolver;
    }
    
    public void setPropertyResolver(PropertyResolver pr) {
        this.propResolver = pr;
    }
    
    public Locale getLocale() {
        return locale;
    }
    
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    
    public Platform getPlatform() {
        if ( platform == null ) 
            platform = new PlatformImpl(); //default impl
        
        return platform;
    }
    
    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
    
    public ClassLoader getClassLoader() {
        return loader;
    }
    
    public void setClassLoader(ClassLoader cl) {
        this.loader = cl;
    }
    
    public ResourceProvider getResourceProvider() {
        return resProvider;
    }
    
    public void setResourceProvider(ResourceProvider resProvider) {
        this.resProvider = resProvider;
    }
    
    public ClientSecurityProvider getSecurityProvider() {
        return secProvider;
    }
    
    public void setSecurityProvider(ClientSecurityProvider csp) {
        this.secProvider = csp;
    }
    //</editor-fold>
    
}
