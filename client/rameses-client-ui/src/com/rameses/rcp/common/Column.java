package com.rameses.rcp.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Column implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String caption;
    private String type = "string";
    private String fieldname;
    private int width;
    private int minwidth;
    private int maxwidth;
    private boolean resizable;
    private boolean editable;
    private boolean visible = true;
    private int rowheight;
    private boolean primary;
    private boolean htmlDisplay;
    private String format;
    private boolean required;
    
    private Map properties = new HashMap();
    
    public Column() {
    }
    
    public Column(String name, String caption){
        this.name = name;
        this.caption = caption;
    }
    
    public Column( String name, String caption, String type, Map props ) {
        this(name, caption);
        this.type = type;
        if( props != null) {
            this.properties = props;
        }
    }
    
    public Column( String name, String caption, String type, boolean editable, boolean required ) {
        this(name, caption, type, null, required);
        this.editable = editable;
    }
    
    public Column( String name, String caption, String type, Map props, boolean required ) {
        this(name, caption, type, props);
        this.required = required;
    }
    
    public Column( String name, String caption, String type, Map props, int width ) {
        this(name, caption, type, props);
        this.width = width;
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="GETTER/SETTER">
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCaption() {
        return caption;
    }
    
    public void setCaption(String caption) {
        this.caption = caption;
    }
    
    public int getWidth() {
        return width;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getMinwidth() {
        return minwidth;
    }
    
    public void setMinwidth(int minwidth) {
        this.minwidth = minwidth;
    }
    
    public int getMaxwidth() {
        return maxwidth;
    }
    
    public void setMaxwidth(int maxwidth) {
        this.maxwidth = maxwidth;
    }
    
    public boolean isResizable() {
        return resizable;
    }
    
    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }
    
    public boolean isEditable() {
        return editable;
    }
    
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public Map getProperties() {
        return properties;
    }
    
    public void setProperties(Map properties) {
        this.properties = properties;
    }
    //</editor-fold>
    
    
    /**
     * Do not remove this. This is used by the client support in the ListColumn Property Editor
     */
    public Column clone() {
        Column col = new Column(getName(), getCaption());
        col.type = type;
        col.width = width;
        col.minwidth = minwidth;
        col.maxwidth = maxwidth;
        col.resizable = resizable;
        col.editable = editable;
        col.visible = visible;
        col.properties = properties;
        col.fieldname = fieldname;
        col.rowheight = rowheight;
        col.primary = primary;
        col.htmlDisplay = htmlDisplay;
        col.format = format;
        return col;
    }
    
    public int getRowheight() {
        return rowheight;
    }
    
    public void setRowheight(int rowheight) {
        this.rowheight = rowheight;
    }
    
    public String getFieldname() {
        if(fieldname==null) return name;
        return fieldname;
    }
    
    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }
    
    public boolean isPrimary() {
        return primary;
    }
    
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
    
    public boolean isHtmlDisplay() {
        return htmlDisplay;
    }
    
    public void setHtmlDisplay(boolean htmlDisplay) {
        this.htmlDisplay = htmlDisplay;
    }
    
    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public boolean isRequired() {
        return required;
    }
    
    public void setRequired(boolean required) {
        this.required = required;
    }
    
    
}
