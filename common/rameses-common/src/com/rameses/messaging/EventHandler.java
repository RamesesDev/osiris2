/*
 * MessageHandler.java
 *
 * Created on July 23, 2011, 9:23 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.messaging;

import java.io.Serializable;

/**
 *
 * @author jzamss
 */
public interface EventHandler extends Serializable {
    void onMessage( QueueMessage m );
}
