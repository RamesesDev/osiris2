package com.rameses.util;

import java.util.HashMap;
import java.util.Map;


public class Warning extends RuntimeException {
    
    public static long serialVersionUID = 1L;
    
    private Map info = new HashMap();
    
    public Warning(String msg) {
        super(msg);
    }

    public Warning(String msg, Map props  ) {
        super(msg);
        info = props;
    }

    public Map getInfo() {
        return info;
    }

}
