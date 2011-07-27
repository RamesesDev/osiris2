/*
 * EventQueueListener.java
 *
 * Created on July 23, 2011, 12:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.messaging;

/**
 *
 * @author jzamss
 */
public interface EventQueueListener {
    void onStart();
    void onStop();
}
