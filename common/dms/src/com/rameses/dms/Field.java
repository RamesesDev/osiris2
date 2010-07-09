/*
 * DataField.java
 *
 * Created on December 27, 2009, 9:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.dms;

/**
 *
 * @author elmo
 */
public class Field {
    
    private String name;
    
    //name of the field in the database
    private String fieldname;
    private String sourcefield;
    
    private DataType type;
    private int length;
    private int scale;
    private boolean primary;
    
    public Field() {
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public DataType getType() {
        return type;
    }
    
    public void setType(DataType type) {
        this.type = type;
    }
    
    public int getLength() {
        return length;
    }
    
    public void setLength(int length) {
        this.length = length;
    }
    
    public int getScale() {
        return scale;
    }
    
    public void setScale(int scale) {
        this.scale = scale;
    }
    
    public boolean isPrimary() {
        return primary;
    }
    
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
    
    public String getFieldname() {
        if(fieldname==null) return name;
        return fieldname;
    }
    
    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }
    
    public String getSourcefield() {
        if(sourcefield==null) {
            return name;
        }
        return sourcefield;
    }
    
    public void setSourcefield(String sourcefield) {
        this.sourcefield = sourcefield;
    }
    
    public boolean equals(Object obj) {
        if(obj==null) return false;
        if(!(obj instanceof Field)) return false;
        return getName().equals(  ((Field)obj).getName() );
    }
    
    
}
