/*
 * ISecurityProvider.java
 *
 * Created on March 7, 2009, 8:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2;


public interface SecurityProvider {

    boolean checkRoles(String name);
    boolean checkPermission(String name);
    
    
}
