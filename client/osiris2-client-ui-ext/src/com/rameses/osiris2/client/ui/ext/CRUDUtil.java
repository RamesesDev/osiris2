/*
 * CRUDUtil.java
 *
 * Created on January 25, 2010, 11:29 AM
 * @author jaycverg
 */

package com.rameses.osiris2.client.ui.ext;

import com.rameses.rcp.common.Column;


public class CRUDUtil {
    
    public static String getMapQL(String entityClass, Column[] cols, String cond, String sortOrder ) {
        StringBuffer sb = new StringBuffer();
        
        sb.append( "select new map(");
        boolean b = false;
        for( Column c: cols ) {
            String fieldName = c.getFieldname();
            String name = c.getName();
            
            if(name.trim().startsWith("#")) continue;
            
            if( name.startsWith("item")) {
                name = name.substring(name.indexOf(".")+1);
            }
            if( fieldName.startsWith("item")) {
                fieldName = fieldName.substring(fieldName.indexOf(".")+1);
            }
            
            
            if( b ) {
                sb.append(",");
            } else {
                b = true;
            }
            sb.append( "o." + fieldName + " as " + name );
        }
        sb.append( ") from " + entityClass + " o " );
        
        if ( cond != null && cond.trim().length() > 0 ) 
            sb.append( " where " + cond);
        if ( sortOrder != null && sortOrder.trim().length() > 0 ) 
            sb.append(" order by " + sortOrder);
        
        return sb.toString();
    }
    
    public static String getEntityQL(String entityClass, Column[] cols, String cond, String sortOrder) {
        StringBuffer sb = new StringBuffer();
        
        sb.append( "select o from " + entityClass + " o ");
        if ( cond != null && cond.trim().length() > 0 )
            sb.append( " where " + cond);
        if ( sortOrder != null && sortOrder.trim().length() > 0 )
            sb.append(" order by " + sortOrder);
        
        
        return sb.toString();
        
    }
    
}
