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
import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jzamss
 */
public class JsonInvoker extends HttpServlet {
    
    
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Writer w = res.getWriter();
        try {
            NameParser np = new NameParser(req);
            DynamicHttpInvoker hp = new DynamicHttpInvoker(np.getHost(),np.getContext());
            DynamicHttpInvoker.Action action = hp.create( np.getService() );
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
            throw new ServletException(e);
        } finally {
            try { w.close(); } catch (Exception ex) {;}
        }
    }
    
    
}
