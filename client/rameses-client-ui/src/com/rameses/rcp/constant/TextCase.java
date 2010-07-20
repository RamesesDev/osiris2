package com.rameses.rcp.constant;

public enum TextCase 
{  
    NONE(0),
    LOWER(1),
    UPPER(2);

    private int type;
    
    TextCase(int type) { 
        this.type = type; 
    }
    
    public String convert(String value)
    {
        if (value == null) return value;
        else if (type == 1) return value.toLowerCase();
        else if (type == 2) return value.toUpperCase();
        else return value;
    }
}
