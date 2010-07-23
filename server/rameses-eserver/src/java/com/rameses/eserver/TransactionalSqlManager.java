/*
 * TransactionalSqlManager.java
 *
 * Created on July 22, 2010, 5:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.interfaces.TransactionContext;
import com.rameses.sql.SqlManager;
import java.sql.Connection;
import javax.sql.DataSource;

/**
 *
 * @author elmo
 */
public class TransactionalSqlManager extends SqlManager implements TransactionContext {
    
    public TransactionalSqlManager(DataSource ds) {
        super();
        dataSource = ds;
    }
    
    private Connection txnconn;
    
    public Connection getTransactionalConnection() throws Exception {
        if(txnconn==null) txnconn = super.getConnection();
        if(txnconn.isClosed()) txnconn = super.getConnection();
        return txnconn;
    }
    
    public void begin() {
        if(txnconn!=null) {
            System.out.println("begin trans in sql");
            /*
            try {
                conn.setAutoCommit(true);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
             */
        }
    }
    
    public void rollBack() {
        if(txnconn!=null) {
            System.out.println("rollback trans in sql");
            /*
            try {
                conn.rollback();
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
             */
        }
    }
    
    public void commit() {
        if(txnconn!=null) {
            System.out.println("commit trans in sql");
            /*
            try {
                conn.commit();
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
             */
        }
    }
    
    public void close() {
        if(txnconn!=null) {
            System.out.println("closing trans in sql");
            /*
            try {
                conn.close();
                conn = null;
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
             */
        }
    }
    
}
