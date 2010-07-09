/*
 * EnvMap.java
 *
 * Created on May 18, 2009, 12:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2;

import java.util.HashMap;
import java.util.Map;

public class EnvMap extends HashMap {
    
    private Map parent;
    
    public EnvMap() {
    }

    public EnvMap(Map parent) {
        this.parent = parent;
    }
      
    public Object get(Object k ) {
        if( super.containsKey(k)) {
            return super.get(k);
        }
        else if( parent!=null && parent.containsKey(k)) {
            return parent.get(k);
        }
        else {
            k = (k + "").replaceAll("_", ".");
            return System.getProperty( k + "");
        }
    }
    
}
