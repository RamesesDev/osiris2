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
import com.rameses.anubis.Project;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
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
public class ServiceInfoServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest hreq, HttpServletResponse hres) throws ServletException, IOException {
        ServletContext app =  super.getServletContext();
        Project project =AnubisContext.getCurrentContext().getProject();
        String path = hreq.getServletPath();
        if( hreq.getPathInfo()!=null) path += hreq.getPathInfo();
        path = path.substring(path.indexOf("/",1)+1,path.lastIndexOf("."));
        CmsWebUtil.setCachedHeader( hres );
        hres.setContentType("text/javascript");
        Writer w = hres.getWriter();
        try {
            if(AnubisContext.getCurrentContext().getModule()!=null) {
                path = path.substring( path.indexOf("/",1)+1 );
            }
            
            Map info = project.getServiceManager().getClassInfo( path );
            if( info !=null ) {
                writeJs( path, info, w );
            }
        } catch(Exception e) {
            throw new ServletException(e);
        } finally {
            try { w.close(); } catch (Exception ex) {;}
        }
        
    }
    
    private void writeJs(String serviceName, Map m, Writer w) throws Exception {
        w.write( "function " + m.get("name") + "() {\n"  );
        w.write( "this.proxy =  new RemoteProxy(\"" + serviceName + "\");\n"  );
        
        List<Map> methods = (List)m.get("methods");

        for( Map mth : methods ) {
            StringBuffer args = new StringBuffer();
            StringBuffer parms = new StringBuffer();
            
            String methodName = (String)mth.get("name");
            List params = (List)mth.get("params");
            
            int i=0;
            for(i=0;i<params.size();i++) {
                String clz = (String)params.get(i);
                //arguments
                args.append( "p" + i + ",");
                
                //parameters
                if( i > 0 ) parms.append(", ");
                parms.append( "p" + i);
            }
            
            w.write("this." + escapeMethodName(methodName) + "= function(");
            w.write(args.toString());
            w.write("handler ) {\n");
            //if( !mth.get("returnType").equals("void") ) w.write("return ");
            w.write( "return this.proxy.invoke(\"" + methodName + "\"" );
            w.write( ",");
            w.write("["+parms.toString()+"]");
            w.write(", handler ); \n");
            w.write("} \n");
        }
        
        w.write( "}" );
    }
    
    private String escapeMethodName(String name) {
        if("delete".equals(name)) {
            return "_" + name;
        } else if("export".equals(name)) {
            return "_" + name;
        } else if("function".equals(name)) {
            return "_" + name;
        } else if("var".equals(name)) {
            return "_" + name;
        } else if("yield".equals(name)) {
            return "_" + name;
        }
        return name;
    }
}
