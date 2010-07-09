/*
 * DataDef.java
 *
 * Created on December 27, 2009, 9:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.dms;

import java.util.Hashtable;
import java.util.Map;


public class Schema {
    
    private String name;
    private String targetdialect;
    private String sourcedialect;
    
    private Table table;
    private Map<String,SubTable> subtables = new Hashtable<String,SubTable>();
    
    public Schema() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetdialect() {
        return targetdialect;
    }

    public void setTargetdialect(String dialect) {
        this.targetdialect = dialect;
    }

    public String getSourcedialect() {
        return sourcedialect;
    }

    public void setSourcedialect(String s) {
        this.sourcedialect = s;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Map<String, SubTable> getSubtables() {
        return subtables;
    }
    
    public DataSet newInstance() {
        return new DataSet(this);
    }
    
}
