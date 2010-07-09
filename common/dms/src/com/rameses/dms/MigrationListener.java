/*
 * MigrationListener.java
 *
 * Created on December 29, 2009, 8:59 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.dms;

/**
 *
 * @author elmo
 */
public interface MigrationListener {
    
    void start(TableInstance t);
    void startBatch(int start, int batchSize);
    void fetchRow(Object o);
    void endBatch();
    void stop();
    
}
