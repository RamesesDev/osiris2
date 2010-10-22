/*
 * ScriptObjectPool.java
 *
 * Created on October 21, 2010, 4:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import java.util.HashMap;
import java.util.Hashtable;

/**
 *
 * @author ms
 */
public class ScriptObjectPool extends HashMap {
    
    private Hashtable<String, ScriptObjectPoolManager> map = new Hashtable();
    private ScriptLoader scriptLoader;
    
    public ScriptObjectPool(ScriptLoader loader) {
        this.scriptLoader = loader;
    }

    public Object get(Object key) {
        String name = (String)key;
        ScriptObjectPoolManager m = map.get(name);
        if( m!=null) return m.getPooledObject();
        
        //we need to synchronize as only one script object poolmanager must be created.
        synchronized(map) {
            if(!map.containsKey(name)) {
                ScriptObjectPoolManager poolManager = new ScriptObjectPoolManager(name,scriptLoader);
                poolManager.init();
                map.put(name, poolManager );
            }    
        }
        return map.get(name).getPooledObject();
    }

    public void clear() {
        map.clear();
    }

    public Object remove(Object key) {
        return map.remove(key);
    }

    
}
