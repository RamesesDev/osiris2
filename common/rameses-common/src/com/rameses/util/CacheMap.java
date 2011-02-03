/*
 * CacheMap.java
 *
 * Created on January 31, 2011, 11:57 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.util.Hashtable;

/**
 *
 * @author ms
 */
public abstract class CacheMap extends Hashtable {
    
    public Object get(Object key) {
        Object data = super.get( key );
        if(data==null) {
            data = fetch(key);
            super.put(key, data);
        }
        return data;
    }
    
    public abstract Object fetch(Object key);
}
