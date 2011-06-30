/*
 * AsyncResponseServet.java
 * Created on June 29, 2011, 11:43 AM
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
public class AsyncResponseServlet extends HttpServlet {
    
    private ServletConfig config;
    
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        super.init(config);
    }
    
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Writer w = res.getWriter();
        res.setContentType("text/javascript");
        try {
            ServletContext app = this.config.getServletContext();
            String appContext = app.getInitParameter("app.context");
            String host = app.getInitParameter("app.host");
            
            String requestId = req.getParameter("requestId");
            Map packet = new HashMap();
            packet.put("requestId", requestId );
            DynamicHttpInvoker hp = new DynamicHttpInvoker(host,appContext);
            Object result  = hp.getService().getPollData( requestId );
            if(result==null) {
                packet.put("status", "NO DATA");
            }
            else if(result instanceof Exception) {
                packet.put("status", "ERROR");
                packet.put("result", ExceptionManager.getOriginal((Exception)result).getMessage() );
            }    
            else if( "EOF".equalsIgnoreCase( result+"" ) ) {
                packet.put("status", "EOF");
            }	
            else {
                packet.put("status", "OK");
                packet.put("result", result);
            }

            String s = JsonUtil.toString( packet );
            w.write(s);
        } catch(Exception e) {
            res.setStatus(res.SC_INTERNAL_SERVER_ERROR );
            w.write( ExceptionManager.getOriginal(e).getMessage()  );
            //res.sendError( res.SC_INTERNAL_SERVER_ERROR, ExceptionManager.getOriginal(e).getMessage() );
        } 
        finally {
            try { w.close(); } catch (Exception ex) {;}
        }
    }
}
