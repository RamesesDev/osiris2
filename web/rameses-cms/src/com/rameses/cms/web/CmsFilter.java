/*
 * CmsFilter.java
 *
 * Created on June 18, 2012, 1:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms.web;

import com.rameses.cms.CmsManager;
import com.rameses.cms.PageInstance;
import com.rameses.cms.Project;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Elmo
 */
public class CmsFilter implements Filter {
    
    private FilterConfig config;
    
    
    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
    }
    
    public void destroy() {
        this.config = null;
    }
    
    private void populateParams(HttpServletRequest hreq, Map params) {
        Enumeration e = hreq.getParameterNames();
        while(e.hasMoreElements()) {
            String name = (String)e.nextElement();
            params.put( name, hreq.getParameter(name) );
        }
    }
    
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest hreq = (HttpServletRequest)servletRequest;
        HttpServletResponse hres = (HttpServletResponse)servletResponse;
        
        if(hreq.getServletPath()!=null && hreq.getServletPath().indexOf(".")<=0) {
            ServletContext app = config.getServletContext();
            CmsManager provider = (CmsManager)app.getAttribute( CmsWebConstants.CONF );
            //run the CMS file
            String serverName = hreq.getServerName();
            System.out.println("server name is " + serverName );
            
            Project project = provider.getProject( null );
            PageInstance pageInstance = project.getFileManager().getPageManager().getPage(hreq.getServletPath());
            populateParams( hreq, pageInstance.getParams() );
            
            if( hreq.getServletPath().startsWith( CmsWebConstants.FRAGMENT_PREFIX ) ) {
                printFragment( pageInstance, hres );
            } else {
               printPage( pageInstance, hres);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        
    }
    
    private void printPage( PageInstance pi, HttpServletResponse res ) throws ServletException{
        OutputStream os = null;
        try {
            res.setContentType("text/html");
            os = res.getOutputStream();
            InputStream is = pi.getInputStream();
            
            os.write( "<html>".getBytes() );
            os.write( "<head>".getBytes() );
            os.write( ("<title>"+ pi.getPage().get("title") + "</title>").getBytes() );
            
            //DISPLAY METAS
            Set metas = (Set)pi.getPage().get("meta");
            for(Object s: metas) {
                Map data = (Map)s;
                os.write( ("<meta name=\""+  data.get("name") + " content=\"" + data.get("content")  + "\"/>").getBytes() );
            }
            
            os.write( "<link href=\"/js/lib/css/jquery-ui/jquery.css\" type=\"text/css\" rel=\"stylesheet\" />".getBytes() );
            os.write( "<link href=\"/js/lib/css/rameses-lib.css\" type=\"text/css\" rel=\"stylesheet\" />".getBytes());
	    os.write( "<script src=\"/js/lib/jquery-all.js\"></script>".getBytes() );
	    os.write( "<script src=\"/js/lib/rameses-ext-lib.js\"></script>".getBytes());
	    os.write( "<script src=\"/js/lib/rameses-ui.js\"></script>".getBytes() );
	    os.write( "<script src=\"/js/lib/rameses-proxy.js\"></script>".getBytes() );
            os.write( "<link href=\"/css/default.css\" type=\"text/css\" rel=\"stylesheet\" />".getBytes() );
           
	    Set imports = (Set)pi.getPage().get("imports");
            for(Object s: imports) {
                os.write( ("<script src=\"" +  s + "\"></script>").getBytes() );
            }
            os.write(  "</head>".getBytes() );
            os.write(  "<body>".getBytes() );
            int i = 0;
            while( (i = is.read())!=-1 ) {
                os.write( i );
            }
            os.write(  "</body>".getBytes() );
            os.write( "</html>".getBytes() );
            
        } catch(Exception e) {
            throw new ServletException(e.getMessage());
        } finally {
            try {os.flush();} catch(Exception ign){;}
            try {os.close();} catch(Exception ign){;}
        }
        
    }
    
    private void printFragment( PageInstance pi, HttpServletResponse res ) throws ServletException{
        OutputStream os = null;
        try {
            res.setContentType("text/html");
            os = res.getOutputStream();
            InputStream is = pi.getInputStream();
            int i = 0;
            while( (i = is.read())!=-1 ) {
                os.write( i );
            }
        } catch(Exception e) {
            throw new ServletException(e.getMessage());
        } finally {
            try {os.flush();} catch(Exception ign){;}
            try {os.close();} catch(Exception ign){;}
        }
    }
    
}

