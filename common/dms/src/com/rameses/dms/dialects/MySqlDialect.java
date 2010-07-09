/*
 * MySqlDialect.java
 *
 * Created on December 28, 2009, 7:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.dms.dialects;

import com.rameses.dms.*;

public class MySqlDialect extends SqlDialect {
    
    public MySqlDialect() {
    }
    
    public String getName() {
        return "mysql";
    }
    
    public String getDataType(DataType t ) {
        if(t == DataType.STRING) return "VARCHAR";
        if(t == DataType.DECIMAL) return "DECIMAL";
        if(t == DataType.INTEGER) return "INT";
        if(t == DataType.DATE) return "DATE";
        if(t == DataType.TIMESTAMP) return "TIMESTAMP";
        return "VARCHAR";
    }
    
    public String getCreateTable(TableInstance d) {
        StringBuffer sb = new StringBuffer();
        sb.append( "CREATE TABLE IF NOT EXISTS " + d.getTablename()+ " (");
        int i = 0;
        for(Field f: d.getFields()) {
            if(i++ > 0)sb.append(",");
            sb.append( f.getFieldname() + " ");
            sb.append( getDataType(f.getType()) );
            
            //check length
            int len = f.getLength();
            if(len==0) {
                if( f.getType() == DataType.STRING ) len = 50;
                else len = 10;
            }
            sb.append( "(" + len );
            if( f.getType() == DataType.DECIMAL ) {
                int scale = f.getScale();
                if(scale==0) scale = 2;
                sb.append( "," + scale );
            }
            sb.append(")");
        }
        sb.append( ")" );
        return sb.toString();
    }
    
    public String getDropTable(TableInstance t) {
        return "DROP TABLE " + t.getTablename();
    }
    
    public boolean isPagingSupported() {
        return true;
    }
    
    public String getSourceListSql(TableInstance t) {
        StringBuffer sb = new StringBuffer();
        sb.append( "SELECT " );
        int i = 0;
        for( Field f: t.getFields() ) {
            if(i++>0) sb.append(",");
            sb.append( f.getSourcefield() + " AS " + f.getName());
        }
        sb.append( " FROM " + t.getSource());
        if( isPagingSupported() ) sb.append( " LIMIT $P{start},$P{limit}");
        return sb.toString();
    }

    public String getTargetListSql(TableInstance t) {
        StringBuffer sb = new StringBuffer();
        sb.append( "SELECT " );
        int i = 0;
        for( Field f: t.getFields() ) {
            if(i++>0) sb.append(",");
            sb.append( f.getFieldname() + " AS " + f.getName());
        }
        sb.append( " FROM " + t.getTablename());
        sb.append( " LIMIT ?,?");
        return sb.toString();
    }

    public String getInsertSql(TableInstance t) {
        StringBuffer sb = new StringBuffer();
        StringBuffer tail = new StringBuffer();
        sb.append( "INSERT INTO " + t.getTablename() + "(");
        int i = 0;
        for( Field f: t.getFields() ) {
            if(i++>0) {
                sb.append(",");
                tail.append(",");
            }
            sb.append(f.getFieldname());
            tail.append("?");
        }
        sb.append(") VALUES (" );
        sb.append(tail);
        sb.append(")");
        return sb.toString();
    }

   
    
    
}
