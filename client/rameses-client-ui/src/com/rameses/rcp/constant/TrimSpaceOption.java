package com.rameses.rcp.constant;

public enum TrimSpaceOption 
{
    NONE(0),
    ALL(1);
    
    private int type;
    
    TrimSpaceOption(int type) { 
        this.type = type; 
    }

    public String trim(String value) {
        if(value==null || type==0) return value;
        String str = value.replaceAll("\\s{2,}", " ");
        if(str.length()>1) str = str.trim();
        return str;
    }
    
}
