/*
 * ApplicationFactory.java
 *
 * Created on May 21, 2009, 2:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2;

import com.rameses.common.PropertyResolver;
import com.rameses.osiris2.custom.DefaultPropertyResolver;
import com.rameses.util.ParserUtil;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * static class for building Application
 * This cannot be used anymore in the old web design
 */
public abstract class AppContext {
    
    private static AppContext instance;
    
    public AppContext() {
    }
    
    public static void setInstance(AppContext ctx) {
        instance = ctx;
    }
    
    public static AppContext getInstance() {
        if(instance==null)
            throw new IllegalStateException("AppContext is null. You must call load(ClassLoader) at least once");
        return instance;
    }

    public abstract ExpressionProvider getExpressionProvider();
    public abstract ClassLoader getClassLoader();
    public abstract CodeProvider getCodeProvider();
    
    
    private PropertyResolver propertyResolver = new DefaultPropertyResolver();
    
    public PropertyResolver getPropertyResolver() {
        return propertyResolver;
    }
    
    
    protected Map env = new EnvMap();
    protected List invokers = new ArrayList();
    protected Map modules = new Hashtable();
    protected FolderManager folderManager;
    
    public void load() throws Exception {
        ClassLoader loader = getClassLoader();
        if( loader ==null ) 
            throw new IllegalStateException("ClassLoader is not specified");
        
        //clean up
        invokers.clear();
        modules.clear();
        
        //find all module conf and register as modules.
        Enumeration e = loader.getResources( "META-INF/module.conf");
        Properties props = new Properties();
        ModuleParser moduleParser = new ModuleParser(loader);
        folderManager = new FolderManager(this);
        
        while( e.hasMoreElements() ) {
            URL u = (URL)e.nextElement();
            props.clear();
            InputStream is = u.openStream();
            props.load( is );
                
            Module mod = new Module(this, u);
            String platform = (String)props.get("platform");
            String channel = (String)props.get("channel");
            mod.setPlatform(platform);
            mod.setChannel(channel);
            
            ParserUtil.updateAttributes(mod,mod.getProperties(),props, propertyResolver);
            modules.put( mod.getNamespace(), mod );
            moduleParser.parse( mod );
            
            //load invokers here
            Iterator iter = mod.getInvokers().iterator();
            while(iter.hasNext()) {
                Invoker inv = (Invoker)iter.next();    
                invokers.add( inv );
            }
        }     
        Collections.sort(invokers);
        
        //load the folders
        FolderParser.parseFolder(folderManager,loader);
    }
    
    public Module getModule(String name) {
        return (Module)modules.get(name);
    }
    
    public List getInvokers() {
        return invokers;
    }
    
    public WorkUnit getWorkUnit( String name ) {
        if(! name.contains(":"))
            throw new IllegalStateException("Workunit Name must have a namespace");
            
        String[] arr = name.split(":");
        String namespace = arr[0];
        String workunitName = arr[1];
        return getModule(namespace).getWorkunit(workunitName);
    }

    public Map getEnv() {
        return env;
    }
    
    public void setEnv(Map env) {
        this.env = env;
    }

    public FolderManager getFolderManager() {
        return folderManager;
    }
    
    public SessionContext createSession() {
        return new SessionContext(this);
    }
    
    
}
