/*
 * ParameterHandler.java
 *
 * Created on July 22, 2010, 8:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.sql.PreparedStatement;

/**
 * This overrides the handling of setting parameters to a
 * prepared statement. Parameter name passed can be null.
 * This means a statement using ? was used.
 */
public interface ParameterHandler {
    
    void setParameter(PreparedStatement ps, int position, Object value, String name ) throws Exception;
    
}
