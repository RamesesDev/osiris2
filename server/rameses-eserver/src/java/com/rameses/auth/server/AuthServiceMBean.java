/*
 * AuthServiceLocal.java
 *
 * Created on April 8, 2010, 4:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.auth.server;

import java.util.List;


public interface AuthServiceMBean {

    List getPermissions(String role);
    List getPermissions(String role, String exclude, String allow, String disallow);
    void start();
    void stop();
    void flush(String role);
    void flushAll();
    
}
