/*
 * JsonInvoker.java
 * Created on April 1, 2011, 4:30 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.support;

import com.rameses.invoker.client.SimpleHttpClient;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jzamss
 */
public class PollerServlet extends HttpServlet {
    
    private ServletConfig config;
    
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        super.init(config);
    }
    
    public void service(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        ServletContext app = this.config.getServletContext();
        String appContext = app.getInitParameter("app.context");
        String host = app.getInitParameter("app.host");
        
        boolean debug = false;
        String sdebug = app.getInitParameter("debug");
        if(sdebug!=null) {
            try {
                debug = Boolean.parseBoolean(sdebug);
            }
            catch(Exception ign){;}
        }
        
        if(host==null || host.trim().length()==0) {
            host = "localhost:8080";
        }
        StringBuilder sb = new StringBuilder();
        sb.append( "http://" + host + "/"+appContext );
        SimpleHttpClient client = new SimpleHttpClient(sb.toString());
        
        String sessionid = req.getParameter("sessionid");
        String tokenid = req.getParameter("tokenid");
        String thost = req.getParameter("host");
        if(thost!=null) host = thost;
        
        if(debug) {
            System.out.println("polling host from front servlet:" + host + " sessiond:"+sessionid + " tokenid:"+tokenid);
        }
        
        try {
            if(sessionid!=null && sessionid.trim().length()>0) {
                Map map = new HashMap();
                map.put("sessionid", sessionid);
                if(tokenid!=null) map.put("tokenid", tokenid);
                String result = client.post("poll", map );
                if(result!=null && result.trim().length()>0) {
                    PrintWriter out = response.getWriter();
                    out = response.getWriter();
                    response.setContentType("text/html;charset=UTF-8");
                    out.print(result);
                    out.flush();
                    out.close();
                }
            }
        } 
        catch(Exception ex) {
            throw new ServletException(ex);
        }
        
    }
    
    
    
    
}
