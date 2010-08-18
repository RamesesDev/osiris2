/*
 * SchemaTypeUtil.java
 *
 * Created on August 14, 2010, 5:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author elmo
 */
public final class SchemaUtil {
    
    //if it returns true, then the required rule passes. 
    public static boolean checkRequired( SchemaField f, Class clazz, Object value ) {
        if(!f.isRequired()) return true;
        
        if( clazz==null ) {
            return false;
        }
        //if class is not null, check the object
       
        
        if( value==null ) {
            return false;
        } 
        else if( value instanceof String ) {
            if( ((String)value).trim().length()==0 ) {
                return false;
            }
        }
        return true;
    }
    
    //if this function returns true, the class type against the field is matched.
    public static boolean checkType( SimpleField f, Class clazz ) {
        String type = f.getType();
        if(type==null || type.trim().length()==0) return true;
        
        Boolean pass = null;
        if( type.equalsIgnoreCase(SimpleFieldTypes.STRING) ) {
            pass = (clazz == String.class);
        } 
        else if( type.equalsIgnoreCase(SimpleFieldTypes.TIMESTAMP) ) {
            pass = (clazz==Timestamp.class); 
        } 
        else if( type.equalsIgnoreCase(SimpleFieldTypes.DATE) ) {
            pass = (clazz==Date.class ) || (clazz==java.sql.Date.class); 
        } 
        else if( type.equalsIgnoreCase(SimpleFieldTypes.DECIMAL) ) {
            pass = (clazz==BigDecimal.class);
        } 
        else if( type.equalsIgnoreCase(SimpleFieldTypes.DOUBLE) ) {
            pass = (clazz==Double.class) || (clazz==double.class);
        } 
        else if( type.equalsIgnoreCase(SimpleFieldTypes.INTEGER) ) {
            pass = (clazz==Integer.class) || (clazz==int.class);
        } 
        else if( type.equalsIgnoreCase(SimpleFieldTypes.BOOLEAN) ) {
            pass = (clazz==Integer.class) || (clazz==int.class);
        }
        else if( type.equalsIgnoreCase(SimpleFieldTypes.LONG) ) {
            pass = (clazz==Long.class) || (clazz==long.class);
        }
        if(pass==null) {
            return false;
        }
        else if(!pass) {
            return false;
        }
        return true;
    }
    
}
