/*
 * TransactionContext.java
 *
 * Created on July 22, 2010, 4:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.interfaces;

/**
 *
 * @author elmo
 */
public interface TransactionContext {
    void begin();
    void rollBack();
    void commit();
    void close();
}
