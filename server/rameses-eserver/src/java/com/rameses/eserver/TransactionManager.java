/*
 * TransactionManager.java
 *
 * Created on July 22, 2010, 4:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.interfaces.TransactionContext;
import java.util.ArrayList;
import java.util.List;

/**
 * this provides tracking of the transactions.
 * TransactionContexts are registered in this transaction manager. 
 */
public class TransactionManager {
   
    private List<TransactionContext> contexts = new ArrayList();
    
    public TransactionManager() {
    }

    public void begin() {
        for(TransactionContext tc: contexts) {
            tc.begin();
        }
    }
    
    public void rollBack() {
        for(TransactionContext tc: contexts) {
            tc.rollBack();
        }
    }
    
    public void commit() {
        for(TransactionContext tc: contexts) {
            tc.commit();
        }
    }
    
    public void close() {
        for(TransactionContext tc: contexts) {
            tc.close();
        }
        contexts.clear();
        contexts = null;
    }
    
    public void register(TransactionContext tc) {
        contexts.add(tc);
    }
    
    
}
