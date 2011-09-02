/*
 * EventHandler.java
 *
 * Created on July 16, 2011, 10:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.concurrent;


public interface EventHandler {
    void onMessage(byte[] b);
}
