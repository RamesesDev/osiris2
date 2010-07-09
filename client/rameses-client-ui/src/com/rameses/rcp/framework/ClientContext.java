/*
 * ClientContext.java
 *
 * Created on June 18, 2010, 1:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.framework;

import com.rameses.platform.interfaces.Platform;
import com.rameses.rcp.impl.ClientContextImpl;
import com.rameses.rcp.impl.ControllerProviderImpl;
import com.rameses.rcp.util.DefaultNavigationHandler;
import com.rameses.util.ExpressionResolver;
import com.rameses.util.MethodResolver;
import com.rameses.util.PropertyResolver;
import com.rameses.util.ValueResolver;
import com.sun.jmx.remote.util.Service;
import java.util.Iterator;
import java.util.Locale;

/**
 *
 * @author jaycverg
 */
public abstract class ClientContext {
    
    private static ClientContext currentContext;
    private TaskManager taskManager;
    private NavigationHandler navigationHandler = new DefaultNavigationHandler();
    private ControllerProvider controllerProvider;
    
    
    //<editor-fold defaultstate="collapsed" desc="  abstract properties  ">
    public abstract ValueResolver getValueResolver();
    public abstract PropertyResolver getPropertyResolver();
    public abstract void setPropertyResolver(PropertyResolver pr);
    public abstract MethodResolver getMethodResolver();
    public abstract void setMethodResolver(MethodResolver mr);
    public abstract ExpressionResolver getExpressionResolver();
    public abstract void setExpressionResolver(ExpressionResolver er);
    public abstract Locale getLocale();
    public abstract void setLocale(Locale locale);
    public abstract Platform getPlatform();
    public abstract void setPlatform(Platform platform);
    public abstract ClassLoader getClassLoader();
    public abstract void setClassLoader(ClassLoader cl);
    public abstract ResourceProvider getResourceProvider();
    public abstract void setResourceProvider(ResourceProvider resProvider);
    public abstract ClientSecurityProvider getSecurityProvider();
    public abstract void setSecurityProvider(ClientSecurityProvider csp);
    //</editor-fold>
    
    public final ControllerProvider getControllerProvider() {
        if ( controllerProvider == null ) {
            Iterator itr = Service.providers(ControllerProvider.class, getClassLoader());
            if ( itr.hasNext() ) {
                controllerProvider = (ControllerProvider) itr.next();
            } else {
                System.out.print("No. ControllerProvider found. Using default ControllerProviderImpl");
                controllerProvider = new ControllerProviderImpl();
            }
        }
        return controllerProvider;
    }
    
    public NavigationHandler getNavigationHandler() {
        return navigationHandler;
    }
    
    public static final ClientContext getCurrentContext() {
        if ( currentContext == null ) {
            Iterator itr = Service.providers(ClientContext.class, Thread.currentThread().getContextClassLoader());
            if ( itr.hasNext() ) {
                currentContext = (ClientContext) itr.next();
            } else {
                System.out.println("No. clientContext found. Using default ClientContextImpl");
                currentContext = new ClientContextImpl();
            }
            currentContext.taskManager = new TaskManager();
        }
        return currentContext;
    }
    
    public final TaskManager getTaskManager() {
        return taskManager;
    }
}
