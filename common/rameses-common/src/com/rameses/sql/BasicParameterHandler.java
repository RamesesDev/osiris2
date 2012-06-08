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
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 *
 * @author elmo
 */
public class BasicParameterHandler implements ParameterHandler {
    
    public void setParameter(PreparedStatement ps, int colIndex, Object value, String name) throws Exception{
        if( value instanceof java.util.Date ) {
            value = parseDate((java.util.Date) value);
        }
        ps.setObject(colIndex,value);
    }
    
    private static final Format TS_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private Object parseDate(java.util.Date value) {
        if( value instanceof java.sql.Date )      return value;
        if( value instanceof java.sql.Timestamp ) return value;
        
        String dtstr = TS_FORMAT.format(value);
        return java.sql.Timestamp.valueOf(dtstr);
    }
    
}
