/*
 * DataSet.java
 *
 * Created on December 27, 2009, 9:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.dms;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * This is an instance of the DataDef
 */
public class DataSet {
    
    private Schema schema;
    
    private TableInstance table;
    private Map<String,TableInstance> subtables;
    private Map params = new HashMap();
    
    DataSet(Schema p) {
        this.schema = p;
        table = new TableInstance(p.getTable(), params);
        subtables = new Hashtable<String, TableInstance>();
        for(Map.Entry me: p.getSubtables().entrySet()) {
            Map parms = new HashMap();
            subtables.put(me.getKey()+"", new TableInstance((Table)me.getValue(), parms));
        }
    }
    
    public Schema getSchema() {
        return schema;
    }

    public TableInstance getTableInstance() {
        return table;
    }

    public Map<String, TableInstance> getSubtables() {
        return subtables;
    }

    public Map getParams() {
        return params;
    }
    
    
}
