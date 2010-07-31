/*
 * ExceptionHandler.java
 *
 * Created on July 30, 2010, 5:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

/**
 *
 * @author elmo
 */
public interface ExecutorExceptionHandler {
    
    void handleException(SqlExecutor se, Exception ex);
    
    
}
