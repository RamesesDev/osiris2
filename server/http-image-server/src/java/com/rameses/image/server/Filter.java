/*
 * Filter.java
 *
 * Created on December 10, 2010, 11:07 AM
 */

package com.rameses.image.server;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  jaycverg
 */

public class Filter implements javax.servlet.Filter {
    
    public Filter() {
    }
    
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if( req.getHeader("content-type")!=null ) {
            if( req.getHeader("content-type").startsWith("multipart")) {
                request = new HttpMultipartRequestWrapper( req );
            }
        }
        
        chain.doFilter(request, response);
    }
    
    public void destroy() {}
    
    
}
