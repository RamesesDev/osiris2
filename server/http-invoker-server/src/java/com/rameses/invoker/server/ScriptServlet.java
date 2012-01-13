package com.rameses.invoker.server;


import com.rameses.server.common.LocalScriptServiceProxy;
import com.rameses.util.ExceptionManager;
import com.rameses.web.common.RequestParser;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * This servlet invokes a scripts usually through simple get/post methods.
 * This is intended for partners who want to invoke services from us.
 * The parameters, represented as key value pairs are collected,
 * packaged as map and sent to the service.
 * caveat: The receiving script service must receive only one parameter.
 * i.e. a map.
 * Example groovy script service:
 * class SampleScript {
 *     @ProxyMethod
 *     public void execute( def o ) {
 *          print o.lastname + " " + o.firstname;
 *     }
 * }
 * to access this from url (post/get):
 *    http://localhost:8080/webcontext/service/SampleScript.execute?lastname=elmo&firstname=nazareno
 * This script service is accesible if you are accessing something in script.
 */
public class ScriptServlet extends HttpServlet {
    
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            RequestParser p = new RequestParser(req);
            Map conf = new HashMap();
            conf.put("app.context", p.getAppContext());
            LocalScriptServiceProxy loc = new LocalScriptServiceProxy(p.getService(),conf,null);
            Object result = loc.invoke(p.getAction(),p.getArgs());
            ResultWriter.print( resp, result, p.isEncrypted(), req.getContentType() );
        } catch(Exception e) {
            Exception orig = ExceptionManager.getOriginal(e);
            resp.setHeader("Error-Message", orig.getMessage());
            resp.sendError(resp.SC_INTERNAL_SERVER_ERROR,orig.getMessage());
        }
    }
    
    
}
