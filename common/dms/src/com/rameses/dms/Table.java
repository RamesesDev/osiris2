/*
 * AbstractTable.java
 *
 * Created on December 28, 2009, 6:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.dms;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elmo
 */
public class Table {
    
    private String name;
    private List<Field> fields = new ArrayList<Field>();
    private List<Parameter> parameters = new ArrayList<Parameter>();
    private String tablename;
    private String source;
    
    public Table() {
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<Field> getFields() {
        return fields;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public String getTablename() {
        if(tablename==null) return name;
        return tablename;
    }

    public void setTablename(String sourcetable) {
        this.tablename = sourcetable;
    }

    public String getSource() {
        if( source == null ) return name;
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    
    
}
