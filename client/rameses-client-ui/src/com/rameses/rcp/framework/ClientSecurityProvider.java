/*
 * ClientSecurityProvider.java
 *
 * Created on November 23, 2009, 9:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.framework;


public interface ClientSecurityProvider {
    boolean checkRoles(String name);
    boolean checkPermission(String name);
}
