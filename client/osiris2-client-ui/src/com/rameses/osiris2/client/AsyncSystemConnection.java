/*
 * AsyncSystemConnection.java
 *
 * Created on August 9, 2010, 4:19 PM
 * @author jaycverg
 */

package com.rameses.osiris2.client;

import com.rameses.messaging.MessageListener;
import com.rameses.messaging.SystemConnection;
import com.rameses.messaging.SystemMessage;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class AsyncSystemConnection implements MessageListener {
    
    private SystemConnection connection;
    private Map<String, AsyncResponse> listeners = new Hashtable();
    
    
    public AsyncSystemConnection() {
    }
    
    public SystemConnection getConnection() {
        return connection;
    }
    
    public void setConnection(SystemConnection con) {
        connection = con;
        System.out.println("setting connection " + con);
        connection.addMessageListener(this);
    }
    
    public void disconnect() {
        if ( connection != null && connection.isConnected() ) {
            connection.close();
            listeners.clear();
        }
    }
    
    public boolean isConnected() {
        if ( connection != null ) {
            return connection.isConnected();
        }
        return false;
    }
    
    public void registerListener(String id, AsyncResponse listener) {
        if ( !listeners.containsKey(id) ) {
            listeners.put(id, listener);
        }
    }
    
    public void onMessage(Object message) {
        System.out.println("firing on message..... " + message);
        if ( message instanceof SystemMessage ) {
            SystemMessage msg = (SystemMessage) message;
            String key = msg.getRequestId()+"";
            if ( !listeners.containsKey(key) ) return;
            
            AsyncResponse resp = listeners.get(key);
            
            Map m = new HashMap();
            m.put("pushId", msg.getPushId());
            
            resp.onResponse( m );
        }
    }
    
}
