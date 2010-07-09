/*
 * XmlWriterUtil.java
 *
 * Created on February 16, 2009, 5:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author elmo
 */
public final class XmlWriterUtil {
    
    public static String writeAttributes( Map m ) {
        StringBuffer sb = new StringBuffer();
        Iterator iter = m.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry e = (Map.Entry)iter.next();
            sb.append( " " + e.getKey() + "=\"" + e.getValue() + "\" " );
        }
        return sb.toString();
    }
    
}
