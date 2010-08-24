/*
 * KeyGen.java
 *
 * Created on August 20, 2010, 1:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.security.SecureRandom;

/**
 *
 * @author elmo
 */
public final class KeyGen {
    
    
    public static final String generateIntKey( String prefix, int length ) {
        SecureRandom sr = new SecureRandom();
        sr.setSeed(System.currentTimeMillis() + System.identityHashCode(sr));
        String snum = sr.nextInt()+"";
        
        StringBuffer key = new StringBuffer();
        if (prefix!=null) {
            key.append(prefix.toString().replaceAll(" ","").trim());
        }
        
        if (snum.length() < 10) {
            int len = 10 - snum.length();
            for (int i=0; i<len; i++) {
                key.append("0");
            }
        }
        
        key.append(snum);
        return key.toString();
    }
    
    public static final String generateAlphanumKey( String prefix, int length ) {
        String alphanum = "ACDEFHJKLMNPQRTUVWXY1234567890";
        
        StringBuffer sbuff = new StringBuffer();
        if(prefix!=null) sbuff.append(prefix);
        
        for(int i=0; i<length;i++) {
            int idx = (int) (Math.random() * alphanum.length() );
            sbuff.append( alphanum.substring(idx, idx+1));
        }
        return sbuff.toString();
    }
    
    
}
