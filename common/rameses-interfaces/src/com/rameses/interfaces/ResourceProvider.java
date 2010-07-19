package com.rameses.interfaces;

import java.io.InputStream;


/***
 * This interface is used by db script local
 * 
 */
public interface ResourceProvider {
    
    public final static String SERVICE = "service";
    public final static String CONF = "conf";
    public final static String SQLCACHE = "sqlcache";
    
    String getNamespace();
    InputStream getResource(String name);
}
