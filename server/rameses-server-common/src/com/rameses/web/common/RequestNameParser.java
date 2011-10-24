/*
 * NameParser.java
 * Created on April 1, 2011, 5:07 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.common;

import com.rameses.server.common.*;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jzamss
 */
public class RequestNameParser {
    
    private String host = "localhost:8080";
    private String service;
    private Object[] args;
    private String action;
    private String context;
    private Map env;
    private HttpServletRequest request;
    
    public RequestNameParser(HttpServletRequest req) {
        StringBuffer reqPath = new StringBuffer();
        if ( req.getPathInfo() != null ) {
            reqPath.append(req.getPathInfo());
        } else if ( req.getServletPath() != null ) {
            reqPath.append(req.getServletPath());
        }
        String _path = reqPath.toString();
        if(_path.indexOf(".")>=0) {
            String[] pathInfos = _path.split("\\.");    
            service = pathInfos[0].substring(1);
            action = pathInfos[1];
        }
        else {
            service = _path.substring(1);
        }
        
        if( req.getContextPath() != null && req.getContextPath().trim().length()>0 && !req.getContextPath().equals("/") ) {
            context = req.getServletPath();
            context = context.substring(context.lastIndexOf("/")+1);
        }
        
        this.request = req;
        host = req.getLocalName() + ":" + req.getLocalPort();
    }
    
    public String getService() {
        return service;
    }
    
    public void setService(String s) {
        this.service = s;
    }
    
    public Object[] getArgs() {
        //parse arguments
        if(args==null) {
            String parm = request.getParameter("args");
            if(parm!=null && parm.length()>0) {
                if(!parm.startsWith("["))
                    throw new RuntimeException("args must be enclosed with []");
                args = JsonUtil.toObjectArray( parm );
            }
        }
        return args;
    }
    
    public Map getEnv() {
        if(env==null) {
            //parse env
            String _env = request.getParameter("env");
            if(_env!=null && _env.length()>0 && _env.startsWith("{")) {
                env = JsonUtil.toMap( _env );
            }
        }
        return env;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String a) {
        action = a;
    }
    
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
    
    public String getHost() {
        return host;
    }

    
    
}
