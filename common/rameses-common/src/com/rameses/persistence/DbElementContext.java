/*
 * DbElementContext.java
 *
 * Created on August 31, 2010, 3:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.persistence;

import com.rameses.schema.SchemaElement;
import com.rameses.sql.AbstractSqlTxn;
import java.util.Stack;

/**
 * this class is used for tracking DB aware contexts during parsing
 */
public class DbElementContext {
    
    private SchemaElement rootElement;
    private Stack<String> contextNames = new Stack();
    private String prefix = "";
    private AbstractSqlTxn sqlTxn;
    
    /** Creates a new instance of DbElementContext */
    public DbElementContext(SchemaElement rootElement) {
        this.rootElement = rootElement;
    }
    
    public void addContext(String name) {
        contextNames.push(name);
        rebuildPrefix();
    }
    
    public void removeContext() {
        contextNames.pop();
        rebuildPrefix();
    }
    
    
    private void rebuildPrefix() {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for(String s: contextNames) {
            if(i++>0) sb.append( "_" );
            sb.append( s );
        }
        prefix = sb.toString();
    }
    
    public String getFieldPrefix() {
        return prefix;
    }

    public String correctName( String name ) {
        if(prefix==null || prefix.trim().length()==0) {
            return name;
        } 
        else {
            return prefix + "_" + name;
        }
    }
    
    public AbstractSqlTxn getSqlTxn() {
        return sqlTxn;
    }

    public void setSqlTxn(AbstractSqlTxn sqlTxn) {
        this.sqlTxn = sqlTxn;
    }
    
    
}
