package com.rameses.eserver;

import com.rameses.messaging.ConnectionManager;
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

public class AsyncConnection implements AsyncConnectionMBean, Serializable, MessageListener {
    
    private String jndiName;
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
        con.addListener(this);        
        con.open();
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING ASYNC CONNECTION: " + jndiName);
        con.close();
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind(ctx, jndiName);
    }
    
    public Map getConf() {
        Map m = new HashMap();
        m.put("host", host);
        m.put("port", port);
        m.put("driverClass", driverClass);
        m.put("user", username);
        return m;
    }
    
    public MessagingConnection getConnection() throws Exception {
        return con;
    }

    public void onMessage(Object message) {
        //System.out.println("receiving message....." + message);
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
    
}
