/*
 * AbstractScriptService.java
 * Created on September 22, 2011, 9:11 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.support;

import com.rameses.web.common.RequestParser;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author jzamss
 */
public class AbstractScriptService extends HttpServlet {
    
    protected ServletConfig config;
    
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
    }
    
    public void destroy() {
        this.config = null;
    }
    
    protected Map getConf() {
        ServletContext app = this.config.getServletContext();
        String appContext = app.getInitParameter("app.context");
        String host = app.getInitParameter("app.host");
        Map map = new HashMap();
        if(appContext!=null) map.put("app.context", appContext);
        if(host!=null)  map.put("app.host", host);
        return map;
    }
    
    
}
