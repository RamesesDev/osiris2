/*
 * ScriptObjectPool.java
 *
 * Created on October 21, 2010, 4:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import java.util.concurrent.ConcurrentHashMap;
/**
 *
 * @author ms
 */
public class ScriptObjectPool {
    
    private ConcurrentHashMap<String, ScriptObject> map = new ConcurrentHashMap();
    private ScriptLoader scriptLoader;
    
    public ScriptObjectPool(ScriptLoader loader) {
        this.scriptLoader = loader;
    }

    public Object get(Object key) {
        String name = (String)key;
        //we need to synchronize as only one script object poolmanager must be created.
        if(!map.containsKey(name)) {
            ScriptObject poolManager = new ScriptObject(name,scriptLoader);
            poolManager.init();
            map.put(name, poolManager );
        }    
        return map.get(name);
    }

    public void clear() {
        map.clear();
    }

    public Object remove(Object key) {
        return map.remove(key);
    }

    public void maintainPoolSize() {
        for(ScriptObject so : map.values()) {
            so.maintainPoolSize();
        }
    }
    
}
