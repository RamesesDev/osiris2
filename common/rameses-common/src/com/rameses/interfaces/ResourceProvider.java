package com.rameses.interfaces;

import java.io.InputStream;
import java.io.Serializable;

/***
 * This interface is used by db script local.
 * The higher number is more prioritized  
 */
public interface ResourceProvider  extends Comparable, Serializable {

    String getName();
    String getDescription();
    int getPriority();
    boolean accept(String nameSpace);
    InputStream getResource(String name) throws Exception;
    
    void scanResources(String name, ResourceHandler handler) throws Exception; 
    
    
}
