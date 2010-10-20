package com.rameses.util;

import java.util.HashMap;
import java.util.Map;


public class SysMap extends HashMap {
    private Map map;
    
    public SysMap(Map m) {
        map = m;
    }
    
    public SysMap() {
    }
    
    public Object get(Object o) {
        Object retval = null;
        if( map != null )
            retval = map.get(o);
        if (retval==null)
            retval = System.getProperty(o+"");
        return retval;
    }
    
    
}