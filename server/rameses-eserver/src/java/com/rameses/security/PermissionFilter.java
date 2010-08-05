/*
 * PermissionFilter.java
 *
 * Created on August 4, 2010, 2:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.security;

public interface PermissionFilter {
    
    boolean accept(Permission perm);
}
