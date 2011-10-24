/*
 * HtmlMap.java
 *
 * Created on August 19, 2010, 12:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.server.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author elmo
 */
public class HtmlMap extends HashMap {
    
    public HtmlMap(final Map map) {
        super(map);
    }
    
    public String toString() {
        StringBuffer buff = new StringBuffer();
        
        buff.append("<table>");
        
        SortedSet keys = new TreeSet(this.keySet());
        Iterator iter = keys.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            buff.append("<tr><td align=\"left\"><b>");
            buff.append(key);
            buff.append("</b></td><td align=\"left\">");
            Object val = this.get(key);
            if(val instanceof Map) {
                buff.append( new HtmlMap((Map)val)) ;
            }
            else if(val instanceof List) {
                List list = (List)val;
                for(Object o : list) {
                    buff.append("<br>");
                    if(o instanceof Map) {
                        buff.append( new HtmlMap((Map)val)) ;
                    }
                    else {
                        buff.append(o);
                    }
                }
            }
            buff.append(this.get(key));
            buff.append("</td></tr>\n\r");
        }
        
        buff.append("</table>");
        
        return buff.toString();
    }
    
}

