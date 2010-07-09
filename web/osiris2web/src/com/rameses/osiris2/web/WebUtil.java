/*
 * WebUtil.java
 *
 * Created on May 21, 2010, 5:11 PM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

public class WebUtil {
    
    public static final String HASH_KEY = "/sessid";
    public static final String OPENER_KEY = "__Opener:";
    public static final String LOGOUT_OUTCOME = "_logout";
    public static final String CLOSE_OUTCOME = "_close";
    public static final String OPENER_OUTCOME = Opener.class.getName();
    
    public static final String removeHash(String path) {
        if ( path != null && path.contains(HASH_KEY)) {
            String exp = WebUtil.HASH_KEY + "\\d+(\\..*)$";
            path = path.replaceAll(exp, "$1");
        }
        return path;
    }
    
    public static final String addHash(String path, int hashCode) {
        String exp = "(\\..*)$";
        if ( !path.contains(HASH_KEY)) {
            return path.replaceAll(exp, WebUtil.HASH_KEY + hashCode + "$1");
        } else {
            return removeHash(path).replaceAll(exp, WebUtil.HASH_KEY + hashCode + "$1");
        }
    }
    
}
