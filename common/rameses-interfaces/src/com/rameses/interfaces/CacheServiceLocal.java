/*
 * CacheServiceLocal.java
 *
 * Created on May 7, 2009, 9:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.interfaces;

/**
 *
 * @author elmo
 */
public interface CacheServiceLocal {
    Object get(String name);
    void addProvider(CacheProvider p);
    void removeProvider(String name);
}
