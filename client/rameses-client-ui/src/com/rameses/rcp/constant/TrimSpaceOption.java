package com.rameses.rcp.constant;

public enum TrimSpaceOption 
{
    NONE(0),
    NORMAL(1),
    ALL(2);
    
    private int type;
    
    TrimSpaceOption(int type) { 
        this.type = type; 
    }

    public String trim(String value) {
        if(value==null || type==0) return value;
        
        String str = value.trim();
        if(type == 2) {
            str = str.replaceAll("\\s{2,}", " ");
        }
        return str;
    }
    
}
