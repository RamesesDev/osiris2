/*
 * MessagingConnection.java
 *
 * Created on July 24, 2010, 9:37 AM
 * @author jaycverg
 */

package com.rameses.messaging;

import java.util.ArrayList;
import java.util.List;

public abstract class MessagingConnection {
    
    protected List<MessageListener> messageListeners = new ArrayList();
    protected List<ConnectionListener> connectionListeners = new ArrayList();
    
    private String host;
    private int port;
    private String username;
    private String password;
    
    public abstract void open() throws Exception;
    public abstract void close();
    public abstract void sendMessage(Message message);

    public abstract boolean isConnected();
    
    protected void notifyConnected() {
        for(ConnectionListener cl: connectionListeners) {
            cl.onDisconnect();
        }
    }
    
    protected void notifyDisconnected() {
        for(ConnectionListener cl: connectionListeners) {
            cl.onDisconnect();
        }
    }
    
    public void processMessage(Object message) {
        if(message instanceof Message) {
            for ( MessageListener m : messageListeners ) {
                m.onMessage( (Message) message);
            }
        }
    }
    
    public void addMessageListener(MessageListener listener) {
        if ( !messageListeners.contains(listener) ) {
            messageListeners.add(listener);
        }
    }
    
    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }
    
    public void addConnectionListener(ConnectionListener listener) {
        if ( !connectionListeners.contains(listener) ) {
            connectionListeners.add(listener);
        }
    }
    
    public void removeConnectionListener(ConnectionListener listener) {
        connectionListeners.remove(listener);
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
