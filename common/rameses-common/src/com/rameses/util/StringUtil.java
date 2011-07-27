/*
 * StringUtil.java
 * Created on July 19, 2011, 10:00 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.util;

/**
 *
 * @author jzamss
 */
public final class StringUtil {
    
    public static String padLeft(String s, char c, int strSize ) {
        StringBuffer sb = new StringBuffer();
        int len = s.length();
        int remainder = strSize-len;
        if(remainder>0) {
            for(int i=0;i<remainder;i++) {
                sb.append(c);
            }
            sb.append( s );
            return sb.toString();    
        }
        else {
            return s;
        }
    }
    
    public static String padRight(String s, char c, int strSize ) {
        StringBuffer sb = new StringBuffer();
        int len = s.length();
        int remainder = strSize-len;
        if(remainder>0) {
            sb.append( s );
            for(int i=0;i<remainder;i++) {
                sb.append(c);
            }
            return sb.toString();    
        }
        else {
            return s;
        }
    }


}
