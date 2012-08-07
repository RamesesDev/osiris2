/*
 * WebAnubisContext.java
 *
 * Created on July 15, 2012, 3:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.web;

import com.rameses.anubis.AnubisContext;
import com.rameses.anubis.Module;
import com.rameses.anubis.NameParser;
import com.rameses.anubis.NameParser.MatchResult;
import com.rameses.anubis.Project;
import com.rameses.anubis.SessionContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Elmo
 */
public class WebAnubisContext extends AnubisContext {
    
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext context;
    private Project project;
    private Module module;
    private String lang;
    
    
    private WebSessionContext session;
    
    /** Creates a new instance of WebAnubisContext */
    WebAnubisContext(ServletContext ctx, HttpServletRequest req, HttpServletResponse res) {
        this.request = req;
        this.response = res;
        this.context = ctx;
    }
    
    //create the project if project is null
    public Project getProject() {
        if(project==null) {
            ProjectResolver resolver = (ProjectResolver)context.getAttribute( ProjectResolver.class.getName() );
            String serverName = request.getServerName();
            //find the project name parser.
            NameParser np = resolver.findNameParser(serverName);
            MatchResult mr = np.buildResult( serverName );
            project =  resolver.getProjectFromUrl( mr.getResolvedPath() );
            this.lang = (String) mr.getTokens().get("lang");
        }
        return project;
    }
    
    public Module getModule() {
        if(module==null) {
            //determine also the module. to do this check module path if it exists
            String pathInfo = request.getServletPath();
            if( pathInfo.startsWith("/services")) pathInfo = pathInfo.substring( "/services".length() );
            if( pathInfo.startsWith("/invoke")) pathInfo = pathInfo.substring( "/invoke".length() );
            
            if(  request.getPathInfo()!=null) pathInfo += request.getPathInfo();
            if( pathInfo.indexOf("/",1)>0) {
                String testModuleName = pathInfo.substring(1, pathInfo.indexOf("/",1));
                this.module = getProject().getModules().get(testModuleName);
            }
        }
        return module;
    }
    
    public String getLang() {
        if(lang==null) getProject();
        return lang;
    }
    
    
    public SessionContext getSession() {
        if(session!=null) return session;
        session = new WebSessionContext();
        return session;
    }
    
    public HttpServletRequest getRequest() {
        return request;
    }
    
    void setRequest(HttpServletRequest req) {
        this.request = req;
    }
    
    public HttpServletResponse getResponse() {
        return response;
    }
    
    void setResponse(HttpServletResponse resp) {
        this.response = resp;
    }

    public ServletContext getServletContext() {
        return this.context;
    }
    
    
}
