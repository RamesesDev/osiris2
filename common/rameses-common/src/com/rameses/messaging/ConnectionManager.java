/*
 * ConnectionManager.java
 *
 * Created on July 24, 2010, 9:37 AM
 * @author jaycverg
 */

package com.rameses.messaging;

public class ConnectionManager {
    
    private static ConnectionManager manager;
    
    private ConnectionManager() {}
    
    public final MessagingConnection getConnection(String driver, String url, String username, String password) throws Exception {
        Class driverClass = Thread.currentThread().getContextClassLoader().loadClass(driver);
        MessagingConnection con = (MessagingConnection) driverClass.newInstance();
        
        con.setHost(url);
        con.setUsername(username);
        con.setPassword(password);
        
        return con;
    }
    
    public final MessagingConnection getConnection(String driver, String url) throws Exception {
        Class driverClass = Thread.currentThread().getContextClassLoader().loadClass(driver);
        MessagingConnection con = (MessagingConnection) driverClass.newInstance();
        
        con.setHost(url);
        
        return con;
    }
    
    public static final ConnectionManager getInstance() {
        if ( manager == null ) {
            manager = new ConnectionManager();
        }
        return manager;
    }
    
}
