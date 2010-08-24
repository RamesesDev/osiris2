/*
 * SqlUnitCacheProvider.java
 *
 * Created on August 14, 2010, 6:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

/**
 *
 * @author elmo
 */
public interface SchemaCacheProvider {
    void putCache(String key, Schema schema);
    Schema getCache(String key);
    void destroy();
}
