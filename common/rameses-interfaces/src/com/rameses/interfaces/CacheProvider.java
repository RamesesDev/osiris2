/*
 * CacheProvider.java
 *
 * Created on May 7, 2009, 9:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.interfaces;

public interface CacheProvider {
    
    String getNamespace();
    Object getValue(Object key);
    
}
