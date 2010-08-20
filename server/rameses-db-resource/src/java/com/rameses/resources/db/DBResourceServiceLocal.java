/*
 * DBScriptProviderServiceLocal.java
 *
 * Created on December 6, 2009, 12:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.resources.db;

import java.util.List;

/**
 *
 * @author elmo
 */
public interface DBResourceServiceLocal {
    
    List getInterceptors();
    List getDsList();
    
    byte[] getTemplateResource(String name);
    byte[] getScriptResource(String name);
    byte[] getDsResource(String name);

    byte[] getResource(Class clazz, String id);
    
    
}
