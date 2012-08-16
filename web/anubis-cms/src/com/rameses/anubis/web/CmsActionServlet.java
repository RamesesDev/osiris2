/*
 * CmsPostAction.java
 *
 * Created on July 10, 2012, 10:20 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.web;

import com.rameses.anubis.ActionCommand;
import com.rameses.anubis.ActionManager;
import com.rameses.anubis.AnubisContext;
import com.rameses.anubis.PageFileInstance;
import com.rameses.anubis.Project;
import com.rameses.util.ExceptionManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Elmo
 */
public class CmsActionServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest hreq, HttpServletResponse hres) throws ServletException, IOException {
        ServletContext app = super.getServletContext();
        Project project = AnubisContext.getCurrentContext().getProject();
        
        try {
            String path = hreq.getPathInfo();
            path = path.substring( 1);
            ActionManager manager = project.getActionManager();
            Map params = CmsWebUtil.buildRequestParams(hreq);
            
            ActionCommand command = manager.getActionCommand( path );
            
            Map env = new HashMap();
            env.put("request", hreq);
            env.put("response", hres);
            env.put("application", app );
            Object o = command.execute( params, env );
            if( o != null ) {
                //redirect to the page
                hres.sendRedirect( o.toString() );
            }
        } catch(Exception e) {
            Map m = new HashMap();
            e = ExceptionManager.getOriginal(e);
            m.put("message", e.getMessage());
            m.put("exception", e);
            PageFileInstance pi = (PageFileInstance)project.getFileManager().getFile( "/error.pg", m );
            pi.setParams(m);
            ResponseWriter.write(app,hreq,hres,null,pi.getContent());
        }
    }
    
    
    protected void doGet(HttpServletRequest hreq, HttpServletResponse hres) throws ServletException, IOException {
        ServletContext app = super.getServletContext();
        Project project = AnubisContext.getCurrentContext().getProject();
        
        try {
            String path = hreq.getPathInfo();
            path = path.substring( 1);
            ActionManager manager = project.getActionManager();
            Map params = CmsWebUtil.buildRequestParams(hreq);
            
            ActionCommand command = manager.getActionCommand( path );
            
            Map env = new HashMap();
            env.put("request", hreq);
            env.put("response", hres);
            env.put("application", app );

            Object o = command.execute( params, env );
            if( o != null ) {
                //redirect to the page
                hres.sendRedirect( o.toString() );
            }
        } catch(Exception e) {
            Map m = new HashMap();
            e = ExceptionManager.getOriginal(e);
            m.put("message", e.getMessage());
            m.put("exception", e);
            PageFileInstance pi = (PageFileInstance)project.getFileManager().getFile( "/error.pg", m );
            pi.setParams(m);
            ResponseWriter.write(app,hreq,hres,null,pi.getContent());
        }
    }
}
