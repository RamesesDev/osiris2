/*
 * SessionFilter.java
 * Created on August 17, 2011, 8:58 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.support;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jzamss
 */
public class SessionFilter implements javax.servlet.Filter {
    
    private FilterConfig filterConfig;
    
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
    
    public void destroy() {
        this.filterConfig = null;
    }
    
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse res = (HttpServletResponse)servletResponse;
        //if the request already contains session id then do not mind this anymore.
        if(req.getAttribute("SESSIONID")==null) {
            //System.out.println(req.getRequestURL());
            //check if there is a session id.
            String sessionId = null;
            
            //check if there is a session id from the different sources in this order ->cookie, input parameter
            Cookie cookies [] = req.getCookies();
            if(cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    String cookieName = cookies[i].getName();
                    if(cookieName.equalsIgnoreCase("sessionid")) {
                        sessionId = java.net.URLDecoder.decode(cookies[i].getValue());
                        break;
                    }
                }
            }
            if(sessionId==null) sessionId = req.getParameter("sessionid");
            if(sessionId==null) sessionId = req.getParameter("SESSIONID");
            
            //if there is a session id, store values in request attribute
            if(sessionId!=null) {
                req.setAttribute("SESSIONID", sessionId);
            }
        }
        
        filterChain.doFilter(servletRequest,servletResponse);
    }
    
    
}
