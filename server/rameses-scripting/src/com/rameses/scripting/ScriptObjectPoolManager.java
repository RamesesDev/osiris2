/*
 * ScriptObjectPoolManager.java
 *
 * Created on October 22, 2010, 8:59 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * this contains only one type of object
 * returns the active object. 
 */
public class ScriptObjectPoolManager {
    
    private String name;
    private ScriptLoader scriptLoader;
    private BlockingQueue<ScriptObject> pool = new LinkedBlockingQueue();
    
    private int minPoolSize = 5;
    
    public ScriptObjectPoolManager(String name, ScriptLoader loader) {
        this.name = name;
        this.scriptLoader = loader;
    }

    public void init() {
        for( int i = 0; i< minPoolSize; i++) {
            ScriptObject o = scriptLoader.findScript(name);
            o.setScriptObjectPoolManager(this);
            pool.add( o );
        }
    }
    
    public ScriptObject getPooledObject() {
        ScriptObject o = pool.poll();
        if(o==null) {
            o = scriptLoader.findScript( name );
            o.setScriptObjectPoolManager(this);
        }
        return o;
    }
    
    public void addBackToPool(ScriptObject p) {
        pool.add( p );
    }
    
    
}
