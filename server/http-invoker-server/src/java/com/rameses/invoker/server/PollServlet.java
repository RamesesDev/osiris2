/*
 * SessionPollServlet.java
 * Created on September 23, 2011, 10:21 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.invoker.server;

import com.rameses.client.session.NotificationServiceProvider;
import com.rameses.client.session.SessionConstant;
import com.rameses.server.common.AppContext;
import com.rameses.service.EJBServiceContext;
import com.rameses.util.ExceptionManager;
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
 * @author emn
 * This servlet is accessed by fat clients only like swing.
 */
public class PollServlet extends HttpServlet {
    
    private ServletConfig config;
    
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        super.init(config);
    }
    
    private static String SESSION_SERVICE = "SessionService";
    
    private void poll( Map conf, String sessionid, String tokenid, HttpServletResponse response ) throws Exception {
        //add a long read
        conf.put("readTimeout", "-1");
        
        EJBServiceContext ctx = new EJBServiceContext(conf);
        NotificationServiceProvider poller = ctx.create( SESSION_SERVICE, NotificationServiceProvider.class );
        Object result = poller.poll(sessionid,tokenid);
        
        if(result == null) {
            ServletUtils.writeText( response, "#NULL");
        } else if(result instanceof String) {
            ServletUtils.writeText(response, (String) result);
        } else if(result instanceof Exception) {
            throw (Exception)result;
        } else if( (result instanceof Map) && ((Map)result).containsKey(SessionConstant.SESSION_REDIRECT)) {
            //redirect polling to another server
            poll( (Map)result, sessionid, tokenid, response );
        } else {
            ServletUtils.writeObject(response,result);
        }
    }
    
    public void service(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        try {
            ServletContext app = this.config.getServletContext();
            AppContext.load();
            Map conf = new HashMap();
            conf.put("app.context", AppContext.getName());
            Map params = (Map)ServletUtils.getRequestInfo(req);
            String sessionid = (String)params.get("sessionid");
            String tokenid = (String)params.get("tokenid");
            poll( conf, sessionid, tokenid, response );
        } catch(Exception e) {
            e.printStackTrace();
            Exception orig = ExceptionManager.getOriginal(e);
            response.setHeader("Error-Message", orig.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, orig.getMessage());
        }
    }
    
    
}
