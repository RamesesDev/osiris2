/*
 * TestSessionBeanLocal.java
 *
 * Created on September 17, 2011, 4:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

/**
 *
 * @author jzamss
 */
public interface TestSessionBeanMBean {
    void start() throws Exception;
    void stop() throws Exception;
    Object test(Object o);
}
