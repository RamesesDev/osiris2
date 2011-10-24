/*
 * EJBServlet.java
 * Created on September 19, 2011, 2:43 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.invoker.server;

import com.rameses.server.common.LocalEJBServiceProxy;
import com.rameses.web.common.RequestParser;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author emn
 */
public class EJBServlet extends HttpServlet {
    
    //from the name - /appContext/ServiceName/local.data
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            RequestParser p = new RequestParser(req);
            Map conf = new HashMap();
            conf.put("app.context", p.getAppContext());
            LocalEJBServiceProxy loc = new LocalEJBServiceProxy(p.getService(),conf );
            Object result = loc.invoke(p.getAction(),p.getArgs());
            ResultWriter.print( resp, result, p.isEncrypted(), req.getContentType() );
        } 
        catch(Exception e) {
            e.printStackTrace();
            resp.sendError(resp.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    
}
