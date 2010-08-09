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
    
    //byte[] getInfo(String name, String className);
    
    byte[] getConf(String name);
    
    List getInterceptors();
    List getPermissions();
    List getConfCategory(String category);
    List getDsList();
    
    byte[] getPermissionResource(String name);    
    byte[] getTemplateResource(String name);
    byte[] getScriptResource(String name);
    byte[] getSqlResource(String name);
    byte[] getDsResource(String name);

}
