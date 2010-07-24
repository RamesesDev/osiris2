package com.rameses.eserver;

import java.io.InputStream;
import java.io.Serializable;

/***
 * This interface is used by db script local.
 * The higher number is more prioritized  
 */
public abstract class ResourceProvider  implements Comparable, Serializable {

    public abstract String getName();
    public abstract String getDescription();
    public abstract int getPriority();
    public abstract boolean accept(String nameSpace);
    public abstract InputStream getResource(String name) throws Exception;
    
    public void scanResources(String name, MultiResourceHandler handler) throws Exception {
        //do nothing...
        throw new Exception("There is no scanResources implementation for " + getName());
    }
    
    public int compareTo(Object o) {
        ResourceProvider rs = (ResourceProvider)o;
        if( rs == null ) return 0;
        if( getPriority() < rs.getPriority()) return 1;
        else if( getPriority() > rs.getPriority() ) return -1;
        return 0;
    }
    
}
