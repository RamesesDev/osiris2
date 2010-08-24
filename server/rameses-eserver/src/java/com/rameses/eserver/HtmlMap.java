/*
 * HtmlMap.java
 *
 * Created on August 19, 2010, 12:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import java.util.HashMap;
import java.util.Iterator;
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
            buff.append("<tr><td align=\"left\"><b>")
            .append(key)
            .append("</b></td><td align=\"left\">")
            .append(this.get(key))
            .append("</td></tr>\n\r");
        }
        
        buff.append("</table>");
        
        return buff.toString();
    }
    
}

