/*
 * BasicParameterHandler.java
 *
 * Created on July 22, 2010, 8:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.sql.PreparedStatement;

/**
 *
 * @author elmo
 */
public class BasicParameterHandler implements ParameterHandler {
    
    public void setParameter(PreparedStatement ps, int colIndex, Object value, String name) throws Exception{
        ps.setObject(colIndex,value);
    }
    
}
