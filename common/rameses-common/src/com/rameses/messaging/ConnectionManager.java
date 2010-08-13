/*
 * ConnectionManager.java
 *
 * Created on July 24, 2010, 9:37 AM
 * @author jaycverg
 */

package com.rameses.messaging;

import java.util.Hashtable;
import java.util.Map;

public class ConnectionManager {
    
    private static ConnectionManager manager;
    private Map<String, MessagingConnection> connections = new Hashtable();
    
    public ConnectionManager() {}
    
    
    public final MessagingConnection createConnection(String name, String driver, String url, String username, String password) throws Exception {
        if ( !connections.containsKey(name) ) {
            MessagingConnection con = getConnection(driver, url);
            con.setUsername(username);
            con.setPassword(password);
            connections.put(name, con);
        }
        
        return connections.get(name);
    }
    
    public final MessagingConnection createConnection(String name, String driver, String url) throws Exception {
        if ( !connections.containsKey(name) ) {
            MessagingConnection con = getConnection(driver, url);
            connections.put(name, con);
        }
        
        return connections.get(name);
    }
    
    public final MessagingConnection getConnection(String name) {
        return connections.get(name);
    }
    
    private MessagingConnection getConnection(String driver, String url) throws Exception {
        Class driverClass = Thread.currentThread().getContextClassLoader().loadClass(driver);
        MessagingConnection con = (MessagingConnection) driverClass.newInstance();
        con.setHost( url );
        return con;
    }
    
    public static final ConnectionManager getInstance() {
        if ( manager == null ) {
            manager = new ConnectionManager();
        }
        return manager;
    }
    
}
