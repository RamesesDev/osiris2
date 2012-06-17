/*
 * ClientNotifier.java
 *
 * Created on June 15, 2012, 4:05 PM
 * @author jaycverg
 */

package com.rameses.osiris2.client;

import java.util.Map;


public interface ClientNotifier {
    
    void addListener();
    void sendMessage(String username, Map Message);
    
    
    public static interface Listener {
        
        void onMessage(Map message);
        
    }
    
}
