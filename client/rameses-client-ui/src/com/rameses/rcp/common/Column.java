package com.rameses.rcp.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Column implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String caption;
    private String type = "string";
    private Object handler;
    
    //for combo box support
    private String items;
    
    private String fieldname;
    private int width;
    private int minWidth;
    private int maxWidth;
    private boolean resizable = true;
    private boolean editable;
    private String editableWhen;
    private boolean visible = true;
    private int rowheight;
    private boolean primary;
    private boolean htmlDisplay;
    private String format;
    private boolean required;
    private Class fieldType;
    
    //alignment support
    private String alignment;
    private String vAlignment;
    
    //icon support
    private String iconVisibleWhen;
    private String icon;
    private String toggleIcon;
    private String headerIcon;
    
    
    private String expression;
    private String category;
    
    //checkbox support
    private Object checkValue;
    private Object uncheckValue;
    
    
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
            this.setProperties(props);
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
    
    
    /**
     * Do not remove this. This is used by the client support in the ListColumn Property Editor
     */
    public Column clone() {
        Column col = new Column(getName(), getCaption());
        col.type = type;
        col.width = width;
        col.minWidth = minWidth;
        col.maxWidth = maxWidth;
        col.resizable = resizable;
        col.editable = editable;
        col.visible = visible;
        col.setProperties(getProperties());
        col.fieldname = fieldname;
        col.rowheight = rowheight;
        col.primary = primary;
        col.htmlDisplay = htmlDisplay;
        col.format = format;
        return col;
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
    
    public int getMinWidth() {
        return minWidth;
    }
    
    public void setMinWidth(int minwidth) {
        this.minWidth = minwidth;
    }
    
    public int getMaxWidth() {
        return maxWidth;
    }
    
    public void setMaxWidth(int maxwidth) {
        this.maxWidth = maxwidth;
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
    
    public Object getHandler() {
        return handler;
    }
    
    public void setHandler(Object handler) {
        this.handler = handler;
    }
    
    public String getItems() {
        return items;
    }
    
    public void setItems(String items) {
        this.items = items;
    }
    
    public Class getFieldType() {
        return fieldType;
    }
    
    public void setFieldType(Class fieldType) {
        this.fieldType = fieldType;
    }
    
    public String getEditableWhen() {
        return editableWhen;
    }
    
    public void setEditableWhen(String editableWhen) {
        this.editableWhen = editableWhen;
    }
    
    //alias for getHAlignment (old method)
    public String getAlignment() {
        return alignment;
    }
    
    //alias for setHAlignment (old method)
    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }
    
    public String getHAlignment() {
        return alignment;
    }
    
    public void setHAlignment(String alignment) {
        this.alignment = alignment;
    }
    
    public String getVAlignment() {
        return vAlignment;
    }
    
    public void setVAlignment(String vAlignment) {
        this.vAlignment = vAlignment;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public String getHeaderIcon() {
        return headerIcon;
    }
    
    public void setHeaderIcon(String headerIcon) {
        this.headerIcon = headerIcon;
    }
    
    public String getExpression() {
        return expression;
    }
    
    public void setExpression(String expression) {
        this.expression = expression;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    //</editor-fold>

    public String getToggleIcon() {
        return toggleIcon;
    }

    public void setToggleIcon(String toggleIcon) {
        this.toggleIcon = toggleIcon;
    }

    public String getIconVisibleWhen() {
        return iconVisibleWhen;
    }

    public void setIconVisibleWhen(String iconVisibleWhen) {
        this.iconVisibleWhen = iconVisibleWhen;
    }

    public Object getUncheckValue() {
        return uncheckValue;
    }

    public void setUncheckValue(Object uncheckValue) {
        this.uncheckValue = uncheckValue;
    }

    public Object getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(Object checkValue) {
        this.checkValue = checkValue;
    }
    
}
