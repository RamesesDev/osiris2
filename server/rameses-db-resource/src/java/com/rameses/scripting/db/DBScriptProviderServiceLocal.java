/*
 * DBScriptProviderServiceLocal.java
 *
 * Created on December 6, 2009, 12:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting.db;

/**
 *
 * @author elmo
 */
public interface DBScriptProviderServiceLocal {
    
    byte[] getInfo(String name, String className);
    byte[] getAllInterceptors();
    
}
