package com.rameses.dms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//</editor-fold>


public class TableInstance {
    
    private String tablename;
    private Table table;
    private String source;
    private int batchSize = 50;
    private List<String> fieldNames = new ArrayList<String>();
    private Map params = new HashMap();
    
    TableInstance(Table t, Map params) {
        this.table = t;
        this.source = source;
        this.tablename = t.getTablename();
        this.source = t.getSource();
        StringBuffer sb = new StringBuffer();
        if(params!=null) {
            this.params = params;
        }
        fieldNames.clear();
        for(Field f: t.getFields()) {
            fieldNames.add(f.getName());
        }
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }
    
    public List<Field> getFields() {
        return table.getFields();
    }

    public Table getTable() {
        return table;
    }

    public String getSource() {
        return source;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
    
    //built after parsing $P{fld}
    public Map getParams() {
        return params;
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }
    
}