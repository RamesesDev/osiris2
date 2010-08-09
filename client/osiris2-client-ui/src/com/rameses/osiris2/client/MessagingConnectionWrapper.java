/*
 * MessagingConnectionWrapper.java
 *
 * Created on July 26, 2010, 10:33 AM
 * @author jaycverg
 */

package com.rameses.osiris2.client;

import com.rameses.messaging.Message;
import com.rameses.messaging.MessagingConnection;
import java.util.Map;


public class MessagingConnectionWrapper extends MessagingConnection {
    
    private MessagingConnection origCon;
    
    public MessagingConnectionWrapper(MessagingConnection con) {
        this.origCon = con;
    }
    
    public void open() throws Exception {
        if ( origCon != null ) {
            origCon.open();
        }
    }
    
    public void close() {
        if ( origCon != null ) {
            origCon.close();
        }
    }
    
    public void sendMessage(Message message) {
        if ( origCon != null ) {
            origCon.sendMessage( message );
        }
    }
    
    public Message createMessage(Map properties) {
        if ( origCon != null ) {
            return origCon.createMessage( properties );
        }
        
        return null;
    }

    public boolean isConnected() {
        if ( origCon != null ) {
            return origCon.isConnected();
        }
        
        return false;
    }
    
}
