/*
 * JsonInvoker.java
 * Created on April 1, 2011, 4:30 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */
package com.rameses.web.support;

import com.rameses.client.session.SessionConstant;
import com.rameses.server.common.JsonUtil;
import com.rameses.service.EJBServiceContext;
import com.rameses.web.common.ServletUtils;
import java.io.IOException;
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
    
    private interface SessionPoller {
        Object poll(String sessionid, String tokenid);
    }
    
    private static String SESSION_SERVICE = "SessionService";
    
    private void poll( Map conf, String sessionid, String tokenid, HttpServletResponse response ) throws Exception {
        //add a long read
        conf.put("readTimeout", "-1");
        
        EJBServiceContext ctx = new EJBServiceContext(conf);
        SessionPoller poller = ctx.create( SESSION_SERVICE, SessionPoller.class );
        Object result = poller.poll(sessionid,tokenid);
        if(result == null) {
            ServletUtils.writeText( response, "");
        }
        else if(result instanceof String) {
            ServletUtils.writeText(response, (String) result);
        }
        else if(result instanceof Exception) {
            throw (Exception)result;
        }
        else if( (result instanceof Map) && ((Map)result).containsKey(SessionConstant.SESSION_REDIRECT)) {
            //redirect polling to another server
            poll( (Map)result, sessionid, tokenid, response );
        }
        else {
            String txt = JsonUtil.toString(result);
            ServletUtils.writeText(response, txt);
        }
    }
    
    public void service(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        try {
            ServletContext app = this.config.getServletContext();
            String sessionHost = app.getInitParameter("session.host");
            String sessionContext = app.getInitParameter("session.context");
            Map conf = new HashMap();
            if(sessionHost!=null)conf.put( "app.host", sessionHost );
            conf.put( "app.context", sessionContext );
            String sessionid = req.getParameter("sessionid");
            String tokenid = req.getParameter("tokenid");
            //System.out.println("sessionid:" + sessionid + " tokenid:"+tokenid);
            poll( conf, sessionid, tokenid, response );
        } 
        catch(Exception e) {
            e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    
    
    
}
