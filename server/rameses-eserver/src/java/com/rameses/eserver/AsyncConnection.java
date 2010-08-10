package com.rameses.eserver;

import com.rameses.messaging.ConnectionListener;
import com.rameses.messaging.ConnectionManager;
import com.rameses.messaging.Message;
import com.rameses.messaging.MessageListener;
import com.rameses.messaging.MessagingConnection;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.naming.InitialContext;

/*
 * AsyncConnection.java
 *
 * Created on July 24, 2010, 1:35 PM
 * @author jaycverg
 */

public class AsyncConnection implements AsyncConnectionMBean, Serializable, MessageListener, ConnectionListener {
    
    private String jndiName = CONSTANTS.SYSTEM_NOTIFIER;
    private String host;
    private int port;
    private String username;
    private String password;
    private String driverClass;
    
    private MessagingConnection con;
    
    public AsyncConnection() {
    }
    
    public void start() throws Exception {
        System.out.println("STARTING ASYNC CONNECTION: " + jndiName);
        InitialContext ctx = new InitialContext();        
        JndiUtil.bind(ctx, jndiName, this);
        con = ConnectionManager.getInstance().getConnection(driverClass, host, username, password);
        con.addMessageListener(this); 
        con.addConnectionListener(this);
        con.open();
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING ASYNC CONNECTION: " + jndiName);
        con.removeConnectionListener(this);
        con.removeMessageListener(this);
        con.close();
        con = null;
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind(ctx, jndiName);
    }
    
    public Map getConf() {
        if(con==null)
            throw new IllegalStateException("No connection available");
        Map m = new HashMap();
        m.put("host", con.getHost());
        m.put("port", con.getPort());
        m.put("driverClass", getDriverClass());
        m.put("user",con.getUsername());
        return m;
    }
    
    public MessagingConnection getConnection() throws Exception {
        return con;
    }

    public void onMessage(Message message) {
        System.out.println("receive msg " + message);
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public void onConnect() {
        System.out.println("CONNECTED " + jndiName);
    }

    public void onDisconnect() {
        System.out.println(jndiName + " is disconnected");
    }
    
}
