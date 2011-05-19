/*
 * JsonInvoker.java
 * Created on April 1, 2011, 4:30 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.support;

import com.rameses.invoker.client.DynamicHttpInvoker;
import com.rameses.util.ExceptionManager;
import java.io.IOException;
import java.io.Writer;
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
public class JsonInvoker extends HttpServlet {
    
    private ServletConfig config;
    
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        super.init(config);
    }
    
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Writer w = res.getWriter();
        res.setContentType("text/javascript");
        try {
            NameParser np = new NameParser(req);
            ServletContext app = this.config.getServletContext();
            
            String host = app.getInitParameter(np.getContext()+".host");
            if(host==null || host.trim().length()==0) host = np.getHost();
            
            DynamicHttpInvoker hp = new DynamicHttpInvoker(host,np.getContext());
            DynamicHttpInvoker.Action action = null;
            if( np.getEnv()==null) {
                action = hp.create( np.getService() );
            }
            else {
                action = hp.create( np.getService(), np.getEnv());
            }
            Object response = null;
            if( np.getArgs()!=null) {
                response = action.invoke(np.getAction(), np.getArgs());
            }
            else {
                response = action.invoke( np.getAction() );
            }
            String s = JsonUtil.toString( response );
            w.write(s);
        } catch(Exception e) {
            res.setStatus(res.SC_INTERNAL_SERVER_ERROR );
            w.write( ExceptionManager.getOriginal(e).getMessage()  );
            //res.sendError( res.SC_INTERNAL_SERVER_ERROR, ExceptionManager.getOriginal(e).getMessage() );
        } finally {
            try { w.close(); } catch (Exception ex) {;}
        }
    }
    
    
    
    
}
