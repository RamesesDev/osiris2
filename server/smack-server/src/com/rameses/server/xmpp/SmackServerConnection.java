/*
 * SmackServerConnection.java
 *
 * Created on December 17, 2010, 11:53 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.server.xmpp;

import com.rameses.eserver.JndiUtil;
import com.rameses.messaging.Message;
import com.rameses.messaging.MessageListener;
import com.rameses.messaging.TextMessage;
import com.rameses.messaging.xmpp.SmackConnection;
import java.io.Serializable;
import java.util.Map;
import javax.naming.InitialContext;

/**
 *
 * @author ms
 */
public class SmackServerConnection implements Serializable, SmackServerConnectionMBean, MessageListener {
    
    private String jndiName;
    private String username;
    private String password;
    private String host;
    private int port = 5222;
    
    private LocalChatServer conn;
    
    public void setUsername(String name) {
        this.username = name;
    }

    public String getUsername() {
        return this.username;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return this.host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }

    public void start() throws Exception {
        System.out.println("STARTING XMPP CONNECTION");
        conn = new LocalChatServer();
        conn.setAutoCreateAccount(true);
        conn.setUsername(username);
        conn.setPassword( password );
        conn.setHost( host );
        conn.setPort(port);
        conn.addMessageListener(this);
        conn.open();
        InitialContext ctx = new InitialContext();
        JndiUtil.bind( ctx, jndiName, this );
    }

    public void stop() throws Exception {
        System.out.println("STOPPING XMPP CONNECTION");
        conn.close();
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind( ctx, jndiName );
    }

    public void onMessage(Message message) {
        System.out.println("on message received " + message.getBody() + " from " + message.getSender() );
    }

    public void send(Message message) {
        conn.sendMessage( message );
    }

    public void setJndiName(String name) {
        this.jndiName = name;
    }

    public String getJndiName() {
        return jndiName;
    }

    
    private static class LocalChatServer extends SmackConnection implements Serializable {
        public LocalChatServer() {
            super();
        }
    }
    
}
