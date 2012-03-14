/*
 * BasicMultitenantFilter.java
 *
 * Created on March 2, 2012, 1:15 PM
 * @author jaycverg
 */

package com.rameses.web.support;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


public class BasicMultitenantFilter implements javax.servlet.Filter
{
    private FilterConfig config;
    
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException 
    {
        String domainName = config.getInitParameter("domain-name");
        
        HttpServletRequest hreq = (HttpServletRequest) req;
        String serverName = hreq.getServerName();
        if( serverName.toLowerCase().startsWith("www.") ) {
            serverName = serverName.substring(4);
        }
        
        String tenantName = null;
        if( domainName != null ) {
            if( !domainName.startsWith(".") )
                domainName = "." + domainName;
            tenantName = serverName.substring(0, serverName.indexOf(domainName));
        }
        else if(serverName.contains(".")) {
            tenantName = serverName.substring(0, serverName.indexOf("."));
        }
        
        if( tenantName!=null ) {
            hreq.setAttribute("TENANT_NAME", tenantName);
        }
        
        chain.doFilter(req, resp);        
    }

    public void destroy() {
        this.config = null;
    }
    
}
