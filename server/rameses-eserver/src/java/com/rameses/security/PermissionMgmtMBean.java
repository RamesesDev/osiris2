/*
 * PermissionMgmtMBean.java
 *
 * Created on August 4, 2010, 4:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.security;

import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public interface PermissionMgmtMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    
    void reload() throws Exception;
    void addPermissions(String permSet ) throws Exception;
    
    Map getPermissionSets(List<String> names, List<String> excludes);
    List<Map> getPermissionItems(List<String> names, List<String> excludes);
    List getRoleDomainPermissionSets(String domainName);
    
    String getJndiName() ;

    void setJndiName(String jndiName) ;
}
