/*
 * WebUtil.java
 * Created on May 4, 2011, 8:04 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.anubis.web;

import java.util.Hashtable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jzamss
 */
public final class CookieUtil {
    
    public static Hashtable getCookieMap(Cookie[] cookies) {
        Hashtable tbl = new Hashtable();
        if (cookies != null) {
            for (int i=0; i < cookies.length; i++) {
                tbl.put(cookies[i].getName(), cookies[i].getValue());
            }
        }
        return tbl;
    }
    
    public static Cookie addCookie( String key , String value, HttpServletResponse res ) {
        Cookie cookie = new Cookie(key,value);
        cookie.setPath("/");
        cookie.setMaxAge(365 * 24 * 60 * 60);
        res.addCookie(cookie);
        return cookie;
    }
    
    public static Cookie getCookie(String key, HttpServletRequest req ) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (int i=0; i < cookies.length; i++) {
                if( cookies[i].getName().equals(key)) {
                    return cookies[i];
                }
            }
        }
        return null;
    }
    
    public static void removeCookie( String key, HttpServletRequest req, HttpServletResponse hres ) {
        Cookie cook = getCookie(key,req);
        if(cook!=null) {
            cook.setPath("/");
            cook.setValue("");
            cook.setMaxAge(0);
            hres.addCookie(cook);
        }
    }
    
}
