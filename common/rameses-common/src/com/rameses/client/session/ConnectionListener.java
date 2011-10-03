/*
 * ConnectionListener.java
 *
 * Created on September 26, 2011, 4:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.session;

/**
 *
 * @author jzamss
 */
public interface ConnectionListener {
    void started();
    void ended(String status);
}
