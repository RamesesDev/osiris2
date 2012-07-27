/*
 * RequestUtils.java
 *
 * Created on May 11, 2012, 1:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.web.support;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Elmo
 */
public class RequestUtils {
    
    public static Map requestMap(HttpServletRequest req) {
        Map m = new HashMap();
        Enumeration e = req.getParameterNames();
        while(e.hasMoreElements() ) {
            String n = (String)e.nextElement();
            m.put(n, req.getParameter(n) );
        }
        return m;
    }
    
}
