/*
 * ScriptInfoServlet.java
 *
 * Created on July 7, 2012, 3:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.web;

import com.rameses.anubis.AnubisContext;
import com.rameses.anubis.JsonUtil;
import com.rameses.anubis.Project;
import com.rameses.util.ExceptionManager;
import groovy.lang.GroovyObject;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Elmo
 */
public class ServiceInvokerServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest hreq, HttpServletResponse hres) throws ServletException, IOException {
        try {
            ServletContext app =  super.getServletContext();
            String path = hreq.getServletPath();
            if( hreq.getPathInfo()!=null) path += hreq.getPathInfo();
            String serviceName = path.substring( path.indexOf("/",1)+1, path.lastIndexOf("."));
            String action = path.substring(path.lastIndexOf(".")+1);
            
            //get the arguments
            String _args = hreq.getParameter("args");
            Object[] args = null;
            if(_args!=null && _args.length()>0) {
                if(!_args.startsWith("["))
                    throw new RuntimeException("args must be enclosed with []");
                args = JsonUtil.toObjectArray( _args );
            }
            
            Project project = AnubisContext.getCurrentContext().getProject();
              if(AnubisContext.getCurrentContext().getModule()!=null) {
                serviceName = serviceName.substring( serviceName.indexOf("/",1)+1 );
            }
            
            //ServiceInvoker invoker = (ServiceInvoker) project.getServiceManager().create( serviceName );
            GroovyObject gobj =(GroovyObject) project.getServiceManager().lookup( serviceName );
           
            if(args==null) args = new Object[]{};
            Object result = gobj.invokeMethod( action, args  );
            writeResponse( JsonUtil.toString(result), hres );
        } 
        catch(Exception e) {
            e = ExceptionManager.getOriginal(e);
            hres.setStatus(hres.SC_INTERNAL_SERVER_ERROR);
            writeResponse(e.getMessage(), hres );
        }
    }
    
    
    private void writeResponse(String result, HttpServletResponse hres  ) throws ServletException {
        Writer w = null;
        try {
            w= hres.getWriter();
            w.write( result );
        } catch(Exception e) {
            throw new ServletException(e.getMessage());
        } finally {
            try{w.close();} catch(Exception e){;}
        }
    }
    
}
