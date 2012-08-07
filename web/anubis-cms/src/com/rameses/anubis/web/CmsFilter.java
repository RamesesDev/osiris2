/*
 * CmsFilter.java
 *
 * Created on June 18, 2012, 1:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.web;


import com.rameses.anubis.AnubisContext;
import com.rameses.anubis.File;

import com.rameses.anubis.FileInstance;
import com.rameses.anubis.Folder;

import com.rameses.anubis.Project;
import com.rameses.anubis.ProjectUtils;

import com.rameses.anubis.SessionContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;
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
    private String PATTERN;
    
    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
    }
    
    public void destroy() {
        this.config = null;
    }
    
    private String buildIgnoredPattern() {
        if( PATTERN != null) return PATTERN;
        
        String pathsIgnored = null; //(String) mconf.get(CmsWebConstants.PATHS_IGNORED);
        String extIgnored = null; //(String) mconf.get(CmsWebConstants.EXT_IGNORED);
        
        if( pathsIgnored == null || pathsIgnored.trim().length() == 0) {
            //we should exclude this because is standard in our web
            pathsIgnored = "/("+ CmsWebConstants.IGNORED_PATHS+")($|/.*)";
        } else {
            pathsIgnored = "/("+ pathsIgnored.replaceAll("\\s", "").replace(",","|") + ")($|/.*)";
        }
        
        if( extIgnored == null || extIgnored.trim().length()==0 ) {
            //we should exclude this because is standard in our web
            extIgnored = ".*\\.("+ CmsWebConstants.IGNORED_EXT +")";
        } else {
            extIgnored = ".*\\.("+ extIgnored.replaceAll("\\s", "").replace(",","|") + ")";
        }
        PATTERN =  "("+pathsIgnored+")|("+extIgnored+")";
        return PATTERN;
    }
    
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest hreq = (HttpServletRequest)servletRequest;
        HttpServletResponse hres = (HttpServletResponse)servletResponse;
        AnubisContext.setContext( new WebAnubisContext(config.getServletContext(), hreq, hres));
        try {
            ServletContext app = config.getServletContext();
            String pattern = buildIgnoredPattern();
            
            if( !hreq.getServletPath().matches(pattern)  ) {
                //run the CMS file
                
                Project project = AnubisContext.getCurrentContext().getProject();
                Map params = CmsWebUtil.buildRequestParams( hreq );
                
                String mimeType = app.getMimeType( hreq.getServletPath() );
                String ext = CmsWebConstants.PAGE_FILE_EXT;
                if( mimeType == null ) {
                    mimeType = "text/html";
                } else {
                    //if mimetype is not null then automatically we consider it as media.
                    ext = CmsWebConstants.MEDIA_FILE_EXT;
                }
                
                String spath = hreq.getServletPath();
                if( spath.equals("/")) {
                    Folder folder = project.getFileManager().getFolder("/");
                    File ff = ProjectUtils.findFirstVisibleFile( folder );
                    if( ff!=null) spath = ff.getPath();
                }
                else {
                    if(spath.endsWith("/")) spath = spath.substring(0, spath.length()-1);
                }
                
                String filename = spath+ext;
                
                FileInstance file = null;
                try {
                    file = project.getFileManager().getFile( filename, params );
                }
                catch(com.rameses.anubis.FileNotFoundException fe) {
                    hres.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    file = project.getFileManager().getFile( "/404.pg", params );
                    CmsWebUtil.output( app,mimeType,file.getContent(),hreq,hres );
                    return;
                }
                catch(Exception e) {
                    e.printStackTrace();
                    throw new ServletException(e.getMessage());
                }
                
                if( file.getHref()!=null) {
                    hres.sendRedirect( file.getHref() );
                    return;
                }
                
                SessionContext ctx = AnubisContext.getCurrentContext().getSession();

                //set authenicated as true if there is sessionid
                boolean allow_access = true;
                if(file.isSecured())
                {
                    if( !ctx.isLoggedIn() )
                        allow_access = false;
                    else if( !ctx.checkFilePermission(file) )
                        allow_access = false;
                }
                
                if( !allow_access) {
                    String path = CmsWebConstants.LOGIN_PAGE_PATH;
                    String requestPath = hreq.getRequestURI();
                    String qry = hreq.getQueryString();
                    if(qry!=null && qry.trim().length()>0){
                        requestPath += "?"+ qry;
                    }
                    hres.sendRedirect(path  + "?target=" + URLEncoder.encode(requestPath));
                } else {
                    InputStream is = file.getContent();
                    //we need to cache header if the content is not a page
                    if( ext.equals(CmsWebConstants.MEDIA_FILE_EXT)) {
                        CmsWebUtil.setCachedHeader(hres);
                    }
                    CmsWebUtil.output( app,mimeType,is,hreq,hres );
                }
            }
            else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            AnubisContext.removeContext();
        }
    }
    
    
}

