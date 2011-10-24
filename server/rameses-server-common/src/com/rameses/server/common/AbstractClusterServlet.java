/*
 * PollerServlet.java
 * Created on September 21, 2011, 6:29 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.server.common;

import com.rameses.service.DefaultEJBServiceProxy;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author emn
 *
 * This class is installed in both http-invoker and web-support, therefore
 * this class is located under common. Generally this works this way:
 *
 * It parses the sessionid for host information. The cluster service
 * is consulted for the specific host address for the actual poll location.
 *
 * 1. http-invoker. Used by fat clients,swing based components.
 * 2. web-support. for web based clients
 *
 * steps:
 * 1. get the sessionid and tokenid
 * 2. extract the server host from the sessionid.
 * 3. lookup the host stored locally in servlet context attribute. If not found lookup from the cluster service
 * 4. redirect call remote or local ejb/SessionService depending on the host.
 * 5.
 */
public abstract class AbstractClusterServlet extends HttpServlet {
    
    private ServletConfig config;
    private static String HOST_MAP = "session-host";
    
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        ServletContext app = this.config.getServletContext();
        app.setAttribute(HOST_MAP, new HashMap() );
    }
    
    public void destroy() {
        this.config = null;
    }
    
    public abstract Map getClusterConf();
    
    protected Map findHostConf( String name ) {
        try {
            ServletContext app = this.config.getServletContext();
            Map hosts = (Map)app.getAttribute( HOST_MAP );
            Map host = (Map)hosts.get(name);
            if(host==null) {
                Map conf = getClusterConf();
                DefaultEJBServiceProxy proxy = new DefaultEJBServiceProxy("ClusterService",conf);
                Object[] args = new Object[]{name};
                Map m = (Map)proxy.invoke( "findHost", args );
                
                host = new HashMap();
                host.put( "app.context", m.get("context") );
                host.put( "app.host", m.get("host") );
                hosts.put(name, host);
            }
            return host;
        }   catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    
    
}
