/*
 * JoinNotifierServlet.java
 * Created on August 2, 2011, 9:28 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.invoker.server;

import com.rameses.eserver.AppContext;
import com.rameses.eserver.NotificationServerMBean;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 *
 *  this servlet initializes the environment
 */
public class AbstractNotifierServlet extends HttpServlet {
    
    private ServletConfig config;
    
    public NotificationServerMBean getNotifier() throws Exception {
        InitialContext ctx = new InitialContext();
        return (NotificationServerMBean)ctx.lookup(AppContext.getName()+"/NotificationServer");
    }
    
    public boolean isDebug() {
         ServletContext app = this.config.getServletContext();
         String debug = app.getInitParameter("debug");
         if(debug!=null) {
             try {
                return Boolean.parseBoolean( debug );
             }
             catch(Exception g){
                 return true;
             }
         }
         return false;
    }
    
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
    }

    public void destroy() {
        this.config = null;
    }

}
