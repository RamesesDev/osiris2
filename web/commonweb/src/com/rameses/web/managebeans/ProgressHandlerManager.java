
package com.rameses.web.managebeans;

import com.rameses.web.common.ProgressHandler;
import java.util.HashMap;


public class ProgressHandlerManager extends HashMap {
    
    public Object get(Object key) {
        if (key == null) return null;
        Object ph = super.get(key);
        if (ph == null) {
            ph = new ProgressHandler();
            super.put(key, ph);
        }
        
        return ph;
    }
    
}
