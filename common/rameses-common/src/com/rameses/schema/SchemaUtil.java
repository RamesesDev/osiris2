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
    public static boolean checkRequired( SchemaField f, Object value ) {
        if(!f.isRequired()) return true;
        
        
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
    
    //sends a value, like defaults and transforms it to the expected type
    public static Object formatType( SimpleField f, Object value ) {
        if( value == null ) return null;
        if((value instanceof String) && ((String)value).trim().length()==0 ) return value;
        
        String type = f.getType();
        if(type==null || type.trim().length()==0) return value;
        
        if( type.equalsIgnoreCase(SimpleFieldTypes.STRING) ) {
            return value.toString();
        } 
        else if( type.equalsIgnoreCase(SimpleFieldTypes.TIMESTAMP) ) {
            return Timestamp.valueOf( value.toString() );
        } 
        else if( type.equalsIgnoreCase(SimpleFieldTypes.DATE) ) {
            return java.sql.Date.valueOf( value.toString() );
        } 
        else if( type.equalsIgnoreCase(SimpleFieldTypes.DECIMAL) ) {
            return new BigDecimal(value.toString());
        } 
        else if( type.equalsIgnoreCase(SimpleFieldTypes.DOUBLE) ) {
            return Double.valueOf(value.toString());
        } 
        else if( type.equalsIgnoreCase(SimpleFieldTypes.INTEGER) ) {
            return Integer.valueOf(value.toString());
        } 
        else if( type.equalsIgnoreCase(SimpleFieldTypes.BOOLEAN) ) {
            return Boolean.valueOf(value.toString());
        }
        else if( type.equalsIgnoreCase(SimpleFieldTypes.LONG) ) {
            return Long.valueOf( value.toString() );
        }
        return null;
    }
    
    
}
