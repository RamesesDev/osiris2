/*
 * ServiceLoader.java
 *
 * Created on April 20, 2011, 10:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;


public interface ServiceLoader {
    int getIndex();
    void load() throws Exception;
    void unload() throws Exception;
}
