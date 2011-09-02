/*
 * RequestServlet.java
 * Created on August 29, 2011, 3:10 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */
package com.rameses.invoker.server;

import com.rameses.invoker.server.InvokerHelper.NameParser;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleScriptServiceServlet extends HttpServlet {
    
    private ServletConfig config;
    
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
    }
    
    public void destroy() {
        this.config = null;
    }
    
    public void service(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = null;
        try {
            Map request = new HashMap();
            Enumeration e= req.getParameterNames();
            while(e.hasMoreElements()) {
                String key = (String)e.nextElement();
                request.put(key, req.getParameter(key));
            }
            NameParser np = InvokerHelper.createNameParser(req);
            //replace parsed name with the proper ScriptService
            Object[] p = new Object[4];
            p[0] = np.getService();
            p[1] = np.getAction();
            p[2] = new Object[]{request};
            p[3] = new HashMap();
            np.setService( "ScriptService" );
            np.setAction("invoke");
            Object result = InvokerHelper.invoke(np, p);
            if(result!=null && result.toString().trim().length()>0) {
                out = response.getWriter();
                out = response.getWriter();
                response.setContentType("text/html;charset=UTF-8");
                out.print(result.toString());
                out.flush();
            }
        } catch(Exception ex) {
            throw new ServletException(ex.getMessage());
        }
        finally {
            try {out.close();} catch(Exception ign){;}
        }
    }
    
}
