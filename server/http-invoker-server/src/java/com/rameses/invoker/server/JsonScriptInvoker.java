package com.rameses.invoker.server;

import com.rameses.invoker.server.InvokerHelper.NameParser;
import java.io.*;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.http.*;

public class JsonScriptInvoker extends HttpServlet {
    
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Writer w = res.getWriter();
        try {
            Object[] args = null;
            String parm = req.getParameter("args");
            if(parm!=null && parm.length()>0) {
                if(!parm.startsWith("["))
                    throw new Exception("args must be enclosed with []");
                args = JsonUtil.toObjectArray( parm );
            }
            NameParser np = InvokerHelper.createNameParser(req);
            //replace parsed name with the proper ScriptService
            Object[] p = new Object[4];
            p[0] = np.getService();
            p[1] = np.getAction();
            p[2] = args;
            p[3] = new HashMap();
            np.setService( "ScriptService" );
            np.setAction("invoke");
            Object response = InvokerHelper.invoke(np, p);
            String s = JsonUtil.toString( response );
            w.write(s);
        } 
        catch(Exception e) {
            throw new ServletException(e);
        } 
        finally {
            try { w.close(); } catch (Exception ex) {;}
        }
    }
    
    
}
