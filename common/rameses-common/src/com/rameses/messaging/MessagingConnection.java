/*
 * MessagingConnection.java
 *
 * Created on July 24, 2010, 9:37 AM
 * @author jaycverg
 */

package com.rameses.messaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class MessagingConnection {
    
    protected List<MessageListener> messageListeners = new ArrayList();
    protected List<ConnectionListener> connectionListener = new ArrayList();
    
    private String host;
    private int port;
    private String username;
    private String password;
    
    public abstract void open() throws Exception;
    public abstract void close();
    public abstract void sendMessage(Message message);
    public abstract Message createMessage(Map properties);
    
    public void processMessage(Object message) {
        for ( MessageListener m : messageListeners ) {
            m.onMessage(message);
        }
    }
    
    public void addListener(MessageListener listener) {
        if ( !messageListeners.contains(listener) ) {
            messageListeners.add(listener);
        }
    }
    
    public void removeListener(MessageListener listener) {
        messageListeners.remove(listener);
    }
    
    public void addListener(ConnectionListener listener) {
        if ( !connectionListener.contains(listener) ) {
            connectionListener.add(listener);
        }
    }
    
    public void removeListener(ConnectionListener listener) {
        connectionListener.remove(listener);
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
}
