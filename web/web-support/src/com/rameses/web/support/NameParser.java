/*
 * NameParser.java
 * Created on April 1, 2011, 5:07 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.support;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jzamss
 */
public class NameParser {
    
    private String host = "localhost:8080";
    private String service;
    private Object[] args;
    private String action;
    private String context;
    private Map env;
    
    public NameParser(HttpServletRequest req) {
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
        
        //parse arguments
        String parm = req.getParameter("args");
        
        if(parm!=null && parm.length()>0) {
            if(!parm.startsWith("["))
                throw new RuntimeException("args must be enclosed with []");
            args = JsonUtil.toObjectArray( parm );
        }
        
        //parse env
        String _env = req.getParameter("env");
        if(_env!=null && _env.length()>0 && _env.startsWith("{")) {
            env = JsonUtil.toMap( _env );
        }
        host = req.getLocalName() + ":" + req.getLocalPort();
    }
    
    public String getService() {
        return service;
    }
    
    public Object[] getArgs() {
        return args;
    }
    
    public String getAction() {
        return action;
    }
    
    public String getContext() {
        return context;
    }

    public String getHost() {
        return host;
    }

    public Map getEnv() {
        return env;
    }
    
}
