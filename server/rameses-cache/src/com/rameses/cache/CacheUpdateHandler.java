/*
 * CacheUpdateHandler.java
 *
 * Created on September 30, 2011, 10:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cache;

import java.io.Serializable;

/**
 *
 * @author jzamss
 */
public interface CacheUpdateHandler extends Serializable {
    String getMode();
    void update(String cacheId, Object cache, Object updateValue);
}
