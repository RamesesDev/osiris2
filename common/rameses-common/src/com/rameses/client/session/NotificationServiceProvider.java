/*
 * SessionServiceProvider.java
 *
 * Created on September 20, 2011, 11:24 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.session;

/**
 *
 * @author jzamss
 */
public interface NotificationServiceProvider {
    Object poll(String sessionid, String tokenid);
}
