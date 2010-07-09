/*
 * ChangeLogHandler.java
 *
 * Created on January 4, 2010, 2:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.framework;

import java.util.Date;

/**
 *
 * @author elmo
 */
public interface ChangeLogHandler {
    
    void start();
    void startEntity( Object entity );
    void startField(String fieldName);
    void showChange( Object oldValue, Object newValue );
    void showHistory( Date timeChanged, Object oldValue, Object newValue );
    void endField();
    void endEntity();
    void end();
    void clear(ChangeLog log);
}
