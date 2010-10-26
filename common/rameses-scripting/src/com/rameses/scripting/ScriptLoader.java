/*
 * ScriptLoader.java
 *
 * Created on October 15, 2010, 8:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

/**
 *
 * @author ms
 */
public interface ScriptLoader {
    ScriptObjectPoolItem findScript(String name);
}
