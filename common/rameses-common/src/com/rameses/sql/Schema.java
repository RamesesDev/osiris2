/*
 * TableSchema.java
 *
 * Created on July 22, 2010, 7:53 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class Schema {
    
    private String name;
    private String tablename;
    private String tabletype;
    private List<Field> primaryKeys = new ArrayList();
    private List<Field> fields = new ArrayList<Field>();
    private Map<String,Field> fieldIndex = new Hashtable();
    
    private List<IndexKey> indices = new ArrayList();
    
    
    public Schema() {
    }
    
    
    public Field addField(String name, String fieldName, String dataType, boolean primary ) {
        Field f = new Field();
        f.name = name;
        f.fieldname = fieldName;
        f.datatype = dataType;
        fields.add(f);
        fieldIndex.put(name,f);
        if(primary) primaryKeys.add(f);
        return f;
    }
    
    public Field getSinglePrimaryKey() {
        return primaryKeys.get(0);
    }
    
    public List<Field> getPrimaryKeys() {
        return primaryKeys;
    }
    
    public Field getFieldByName(String name) {
        return fieldIndex.get(name);
    }
    
    public List<Field> getFields() {
        return fields;
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="IndexKey">
    public static class IndexKey {
        private String[] fields;
        IndexKey(String[] f) {
            this.fields = f;
        }
        public String[] getFields() {
            return fields;
        }
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Field">
    public static class Field {
        
        Field() {;}
        
        private String name;
        private String fieldname;
        private String datatype;
        private int scaling;
        private int length;
        private boolean nullable;
        private boolean unique;

        public String getName() {
            return name;
        }

        public String getFieldname() {
            return fieldname;
        }

        public String getDatatype() {
            return datatype;
        }

        public int getScaling() {
            return scaling;
        }

        public void setScaling(int scaling) {
            this.scaling = scaling;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public boolean isNullable() {
            return nullable;
        }

        public void setNullable(boolean nullable) {
            this.nullable = nullable;
        }

        public boolean isUnique() {
            return unique;
        }

        public void setUnique(boolean unique) {
            this.unique = unique;
        }
        
    }
    //</editor-fold>

    public String getName() {
        return name;
    }

    public String getTablename() {
        return tablename;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getTabletype() {
        return tabletype;
    }

    public void setTabletype(String tabletype) {
        this.tabletype = tabletype;
    }
}
