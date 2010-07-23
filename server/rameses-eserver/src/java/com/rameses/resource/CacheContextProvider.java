/*
 * CacheContextProvider.java
 *
 * Created on July 16, 2010, 5:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.resource;

import java.util.Map;

/**
 *
 * @author elmo
 * Priority is provided for overriding implementations. 
 * The higher the priority can override the lower ones.
 * Only one cache context provider will be considered.
 */
public abstract class CacheContextProvider implements Comparable{
    
    public abstract String getName();
    
    //this is used for checking during deployment.
    public abstract String getDescription();
    public abstract Map getCacheMap();
    public abstract int getPriority();
    
     public int compareTo(Object o) {
        CacheContextProvider rs = (CacheContextProvider)o;
        if( rs == null ) return 0;
        if( getPriority() < rs.getPriority()) return 1;
        else if( getPriority() > rs.getPriority() ) return -1;
        return 0;
    }
    
}
