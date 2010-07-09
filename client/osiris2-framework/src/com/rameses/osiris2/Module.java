/*
 * DefaultWorkUnitProvider.java
 *
 * Created on February 22, 2009, 7:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * this is a package level class created only by 
 * the ApplicationFactory
 */
public class Module {
    
    //contains workunits
    private String platform;
    private String channel;
    private String name;
    private String title;
    private String contextPath;
    private Map properties = new HashMap();
    private Map workunits = new Hashtable();
    private List invokers = new ArrayList();
    private AppContext context;
    
    Module(AppContext ctx, URL u) {
        this.contextPath = u.toExternalForm().replaceAll("/META-INF/module.conf", "");
        this.context = ctx;
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="GETTER/SETTER">
    public String getNamespace() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContextPath() {
        return contextPath;
    }
    
    public Map getProperties() {
        return properties;
    }
    
    public Map getWorkUnits() {
        return workunits;
    }
    
    public List getInvokers() {
        return invokers;
    }
    //</editor-fold>

    //check first if name ends with extension, use that extension instead. 
    //get the inputstream source. Afterwards, parse it using the defined
    //xml parser. Place the resulting workunit in the hashtable.
    public WorkUnit getWorkunit(String name) {
        return (WorkUnit)workunits.get(name);
    }
    
    public InputStream getResourceAsStream( String name ) {
        try {
            return getResource(name).openStream();
        }
        catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public URL getResource(String name) {
        try {
            if(!name.startsWith("/")) name = "/" + name;
            return new URL( this.contextPath + name );
        }
        catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    
    // <editor-fold defaultstate="collapsed" desc="For deprecation">
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
    //</editor-fold>

    public AppContext getAppContext() {
        return context;
    }

    
}
