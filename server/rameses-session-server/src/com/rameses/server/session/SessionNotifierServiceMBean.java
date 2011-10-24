/*
 * SessionClientMBean.java
 *
 * Created on September 27, 2011, 11:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.server.session;

/**
 *
 * @author jzamss
 */
public interface SessionNotifierServiceMBean {
    void start() throws Exception;
    void stop() throws Exception;
    void setUsername(String name);
    void setInfo(Object info);
    String getUsername();
    Object getInfo();
}
