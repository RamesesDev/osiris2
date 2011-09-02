/*
 * JoinNotifierServlet.java
 * Created on August 2, 2011, 9:28 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.invoker.server;

import com.rameses.eserver.NotificationServerMBean;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jzamss
 * mapped to /disjoin
 * removes the host from this list
 */
public class PollServlet extends AbstractNotifierServlet {
    
    /*
     * if client wants to create a new connection, it must send the sessionid and blank tokenid 
     * if return value starts with TOKEN. This means anew connection was made
     * if return value is -1 then client needs to reconnect or poll back a blank token id.
    */
    
    public void service(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        try {
             
            boolean debug = isDebug();
            
            NotificationServerMBean notifier = getNotifier();
            
            String sessionid = req.getParameter("sessionid");
            String tokenid = req.getParameter("tokenid");
            
            String result = null;
            if(tokenid == null) {
                result = notifier.register( sessionid );
                if(debug) System.out.println("registering new token ");
            }
            else {
                if(debug) System.out.println("poll reconnect token value=" + tokenid);
               //get the result
                result = (String)notifier.poll(sessionid, tokenid);
            }
            if(result!=null && result.trim().length()>0) {
                PrintWriter out = response.getWriter();
                out = response.getWriter();
                response.setContentType("text/html;charset=UTF-8");
                out.print(result);
                out.flush();
                out.close();
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
