/*
 * ClusterResourceServlet.java
 * Created on October 17, 2011, 9:25 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.support;

import com.rameses.http.HttpClient;
import com.rameses.http.HttpClientOutputHandler;
import com.rameses.service.EJBServiceContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jzamss
 */
public class ClusterResourceServlet extends ResourceServlet {
    
    private final static String clusterServiceName = "ClusterService";
    
    private Map getConf() {
        ServletContext app = this.config.getServletContext();
        String appContext = app.getInitParameter("app.context");
        String host = app.getInitParameter("app.host");
        Map map = new HashMap();
        if(appContext!=null) map.put("app.context", appContext);
        if(host!=null)  map.put("app.host", host);
        return map;
    }
    
    private static interface WebClusterService  {
        Map findHost(String name);
        String getCurrentHostName();
    }
    
    private class ByteOutputHandler implements HttpClientOutputHandler {
        private HttpServletResponse resp;
        private String contentType;
        
        public ByteOutputHandler(HttpServletResponse res, String contentType) {
            this.contentType = contentType;
            this.resp = res;
        }
        public Object getResult(InputStream is) {
            OutputStream out = null;
            try {
                resp.setContentType(contentType);
                out = resp.getOutputStream();
                int i = 0;
                while( (i =is.read())!=-1 ) {
                    out.write( i );
                }
                out.flush();
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                try {is.close(); } catch(Exception e) {;}
                try {out.close(); } catch(Exception e) {;}
            }
            return null;
        }
    }
    
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String n = req.getPathInfo().substring(1);
            String clusterName = n.substring(0, n.indexOf("/"));
            
            //consult the server what host the photo belongs to.
            Map conf = this.getConf();
            EJBServiceContext sv = new EJBServiceContext(conf);
            
            WebClusterService clusterService = sv.create(clusterServiceName, WebClusterService.class);
            //check first if this is the current host.
            String hostName = clusterService.getCurrentHostName();
            String filePath = n.substring(n.indexOf("/"));
            if(hostName.equals(clusterName)) {
                super.writeResource( filePath, res );
            } else {
                Map remoteConf = clusterService.findHost( clusterName );
                String contentType = config.getServletContext().getMimeType(filePath);
                HttpClient hc = new HttpClient( (String) remoteConf.get("app.host") );
                hc.setEncrypted(false);
                hc.setOutputHandler(new ByteOutputHandler(res, contentType ));
                hc.setAppContext(req.getContextPath());
                
                hc.get( req.getServletPath() + req.getPathInfo() );
            }
        } catch(Exception e) {
            e.printStackTrace();
            res.sendError(res.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    
    
}
