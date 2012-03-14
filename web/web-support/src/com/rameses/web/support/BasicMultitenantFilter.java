/*
 * BasicMultitenantFilter.java
 *
 * Created on March 2, 2012, 1:15 PM
 * @author jaycverg
 */

package com.rameses.web.support;

import com.rameses.service.ScriptServiceContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class BasicMultitenantFilter implements javax.servlet.Filter {
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
        if( domainName !=null && serverName.contains(domainName) ) {
            if( !domainName.equals(serverName)) {    
                if( !domainName.startsWith(".") ) domainName = "." + domainName;
                tenantName = serverName.substring(0, serverName.indexOf(domainName));
            }
        }
        
        if( tenantName!=null ) {
            try {
                ServletContext app = this.config.getServletContext();
                String appHost = app.getInitParameter("app.host");
                String appContext = app.getInitParameter("app.context");
                Map conf = new HashMap();
                if(appHost!=null)conf.put( "app.host", appHost );
                conf.put( "app.context", appContext );
                ScriptServiceContext svc = new ScriptServiceContext(conf);
                MultitenantConfService msvc = svc.create("MultitenantConfService", MultitenantConfService.class );
                Map mconf = msvc.getConf( tenantName );
                hreq.setAttribute("APP_CONF", mconf);
            } 
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        chain.doFilter(req, resp);
    }
    
    private interface MultitenantConfService {
        Map getConf(String name);
    }
    
    public void destroy() {
        this.config = null;
    }
    
}
