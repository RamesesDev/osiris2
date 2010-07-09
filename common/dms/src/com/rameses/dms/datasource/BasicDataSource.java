/*
 * MyDataSource.java
 *
 * Created on December 28, 2009, 5:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.dms.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;

/**
 *
 * @author elmo
 */
public class BasicDataSource implements DataSource {
    
    private String name;
    private String url;
    private String driverClass;
    private String username;
    private String password;
    
    private PrintWriter writer = new PrintWriter(System.out,true);
    private int timeout;
    
    BasicDataSource(Map props) {
        name = (String)props.get("name");
        url = (String)props.get("connection-url");
        driverClass = (String)props.get("driver-class");
        username = (String)props.get("user-name");
        password = (String)props.get("password");
        
        System.out.println("*************");
        System.out.println(name);
        System.out.println(url);
        System.out.println(driverClass);
        System.out.println(username);
        System.out.println(password);
    }
    
    public Connection getConnection() throws SQLException {
        try {
            Class.forName(driverClass);
            return DriverManager.getConnection(url,username,password);
        } catch(Exception e) {
            throw new SQLException(e.getMessage());
        }
    }
    
    public Connection getConnection(String usrname, String pwd) throws SQLException {
        try {
            Class.forName(driverClass);
            return DriverManager.getConnection(url,usrname,pwd);
        } catch(Exception e) {
            throw new SQLException(e.getMessage());
        }
    }
    
    public PrintWriter getLogWriter() throws SQLException {
        return writer;
    }
    
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.writer = out;
    }
    
    public void setLoginTimeout(int seconds) throws SQLException {
        this.timeout = seconds;
    }
    
    public int getLoginTimeout() throws SQLException {
        return timeout;
    }

    public String getName() {
        return name;
    }
    
}
