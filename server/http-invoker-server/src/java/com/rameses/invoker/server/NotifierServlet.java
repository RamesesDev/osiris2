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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jzamss
 * mapped to /disjoin
 * starts up / shutdowns the server
 */
public class NotifierServlet extends AbstractNotifierServlet {
    
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            NotificationServerMBean notifier = getNotifier();
            String action = req.getParameter("action");
            if(action.equalsIgnoreCase("join")) {
                String host = req.getParameter("host");
                notifier.addHost(host);
            } else if(action.equalsIgnoreCase("disjoin")) {
                String host = req.getParameter("host");
                notifier.removeHost(host);
            } else if(action.equalsIgnoreCase("send")) {
                String sessionid = req.getParameter("sessionid");
                String message = req.getParameter("message");
                notifier.signal(sessionid, message,false);
            } 
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
