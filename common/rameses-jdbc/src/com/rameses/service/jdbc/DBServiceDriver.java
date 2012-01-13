/*
 * DBServiceDriver.java
 * Created on December 28, 2011, 3:42 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.service.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author jzamss
 */
public class DBServiceDriver implements Driver {
    private static String JDBC_PREFIX = "jdbc:rameses://";
    
    static {
        try {
            java.sql.DriverManager.registerDriver(new DBServiceDriver());
        } catch (SQLException E) {
            throw new RuntimeException("Can't register driver!");
        }
    }
    
    public Connection connect(String url, Properties info) throws SQLException {
        String s = url.substring( JDBC_PREFIX.length() );
        String arr[] = s.split("/");
        return new DBServiceConnection(arr[0],arr[1]);
    }
    
    public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith(JDBC_PREFIX);
    }
    
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[]{};
    }
    
    public int getMajorVersion() {
        return 1;
    }
    
    public int getMinorVersion() {
        return 0;
    }
    
    public boolean jdbcCompliant() {
        return true;
    }
    
}
