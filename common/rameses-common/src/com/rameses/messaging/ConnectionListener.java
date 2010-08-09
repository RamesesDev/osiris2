/*
 * ConnectionListener.java
 *
 * Created on July 24, 2010, 9:38 AM
 * @author jaycverg
 */

package com.rameses.messaging;


public interface ConnectionListener {
    
    void onConnect();
    void onDisconnect();
    
}
