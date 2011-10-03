/*
 * HttpClientUtils.java
 * Created on September 19, 2011, 9:14 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.http;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public final class HttpClientUtils {
    
    public static String stringifyParameters(Map params) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iter = params.keySet().iterator();
        boolean b = false;
        while(iter.hasNext()) {
            String key = (String) iter.next();
            Object val = params.get(key);
            if(val!=null) {
                if(!b) {
                    b = true;
                } else {
                    sb.append("&");
                }
                sb.append( key + "=" + URLEncoder.encode( val.toString() ));    
            }
        }
        return sb.toString();
    }
    
}
