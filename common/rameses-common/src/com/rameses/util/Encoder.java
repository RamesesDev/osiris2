/*
 * Encoder.java
 *
 * Created on June 2, 2010, 2:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.security.MessageDigest;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author elmo
 */
public abstract class Encoder {
    
    protected static Map<String,Encoder> encoders = new Hashtable<String,Encoder>();
    public abstract String encode(String v);
    
    public static MD5Encoder MD5 = new MD5Encoder();
    public static SHA1Encoder SHA1 = new SHA1Encoder();
    
    public Encoder() {
    }
    
    
    public static Encoder get(String name) {
        if(name.equalsIgnoreCase("md5")) {
            return MD5;
        } else if ( name.equalsIgnoreCase("sha1") ) {
            return SHA1;
        }
        return encoders.get(name);
    }
    
    public static class MD5Encoder extends Encoder {
        public String encode(String value) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(value.getBytes());
                
                byte[] hash =  md.digest();
                String hexDigit = "0123456789abcdef";
                StringBuffer sb = new StringBuffer(hash.length);
                for (int i=0; i< hash.length; i++) {
                    int b = hash[i] & 0xFF;
                    sb.append(hexDigit.charAt(b >>> 4));
                    sb.append(hexDigit.charAt(b & 0xF));
                }
                return sb.toString();
            } catch(Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }
    
    public static class SHA1Encoder extends Encoder {
        
        public String encode(String value) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(value.getBytes());
                
                byte[] hash =  md.digest();
                String hexDigit = "0123456789ABCDEF";
                StringBuffer sb = new StringBuffer(hash.length);
                for (int i=0; i< hash.length; i++) {
                    int b = hash[i] & 0xFF;
                    sb.append(hexDigit.charAt(b >>> 4));
                    sb.append(hexDigit.charAt(b & 0xF));
                }
                return sb.toString();
            } catch(Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        
    }
    
}
