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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
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
public class CmsPostActionServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest hreq, HttpServletResponse hres) throws ServletException, IOException {
        ServletContext app = super.getServletContext();
        Project project = AnubisContext.getCurrentContext().getProject();
        
        try {
            String path = hreq.getPathInfo();
            path = path.substring( 1);
            ActionManager manager = project.getActionManager();
            Map params = CmsWebUtil.buildRequestParams(hreq);
            
            ActionCommand command = manager.getActionCommand( path );
            
            StringWriter w = new StringWriter();
            Map env = new HashMap();
            env.put("out", w);
            Object o = command.execute( params, env );
            
            String content = w.toString();
            if( content.length() > 0 ) {
                CmsWebUtil.output(app,null,new ByteArrayInputStream(content.getBytes()),hreq,hres);
            }
            else if( o != null ) {
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
            CmsWebUtil.output(app,null,pi.getContent(),hreq,hres);
        }
    }
}
