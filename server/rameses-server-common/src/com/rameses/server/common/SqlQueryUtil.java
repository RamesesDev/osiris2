/*
 * SqlQueryUtil.java
 * Created on September 13, 2011, 8:16 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.server.common;

/**
 *
 * @author jzamss
 */
public final class SqlQueryUtil {
    
    public static String buildDynamicParams(String[] arr) {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        for(String l: arr) {
            if(!first) b.append(",");
            else first = false;
            b.append("?");
        }
        return b.toString();
    }
    
}
