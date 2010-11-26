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
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author elmo
 */
public abstract class Encoder {
    
    protected static Map<String,Encoder> encoders = new Hashtable<String,Encoder>();
    public abstract String encode(String v);
    public abstract String encode(String v, String seed);
    
    //caseType = 0=any case 1=lowercase 2=uppercase. lowercase is default.
    public abstract String encode(String v, String seed, int caseType);
    
    public static MD5Encoder MD5 = new MD5Encoder();
    public static SHA1Encoder SHA1 = new SHA1Encoder();
    
    public Encoder() {
    }
    
    public String toHexString(byte[] hash) {
        String hexDigit = "0123456789abcdef";
        StringBuffer sb = new StringBuffer(hash.length);
        for (int i=0; i< hash.length; i++) {
            int b = hash[i] & 0xFF;
            sb.append(hexDigit.charAt(b >>> 4));
            sb.append(hexDigit.charAt(b & 0xF));
        }
        return sb.toString();
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
                return toHexString(hash);
            } catch(Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        public String encode(String v, String seed) {
            return encode(v,seed,1);
        }
        
        public String encode(String v, String seed, int caseType) {
            try {
                if(caseType==1)seed = seed.toLowerCase();
                else if(caseType==2)seed = seed.toUpperCase();
                SecretKeySpec skey = new SecretKeySpec(seed.getBytes(), "HmacMD5");
                Mac mac = Mac.getInstance("HmacMD5");
                mac.init(skey);
                byte[] hash = mac.doFinal(v.getBytes());
                return toHexString(hash);
            } catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }
    
    public static class SHA1Encoder extends Encoder {
        
        public String encode(String value) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(value.getBytes());
                byte[] hash =  md.digest();
                return toHexString(hash);
            } catch(Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        
        public String encode(String v, String seed) {
            return encode(v,seed,1);
        }

        public String encode(String v, String seed, int caseType) {
            try {
                if(caseType==1)seed = seed.toLowerCase();
                else if(caseType==2)seed = seed.toUpperCase();
                SecretKeySpec skey = new SecretKeySpec(seed.getBytes(), "HmacSHA1");
                Mac mac = Mac.getInstance("HmacSHA1");
                mac.init(skey);
                byte[] hash = mac.doFinal(v.getBytes());
                return toHexString(hash);
            } catch(Exception e){
                throw new RuntimeException(e);
            }
        }
        
    }
    
}
