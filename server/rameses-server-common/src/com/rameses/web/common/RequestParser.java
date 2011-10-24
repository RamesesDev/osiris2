/*
 * NameParser.java
 * Created on September 20, 2011, 7:28 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.common;

import com.rameses.server.common.*;
import com.rameses.util.SealedMessage;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jzamss
 */
public class RequestParser {
    
    private String service;
    private String action;
    private String appContext;
    private Object[] args;
    private boolean encrypted = false;
    private HttpServletRequest req;
    
    public static String APP_FILTER = "com.rameses.invoker.server.filter";
    
    /** Creates a new instance of NameParser */
    public RequestParser(HttpServletRequest req) {
        this.req = req;
        String pathInfo = req.getPathInfo();
        if( pathInfo == null )
            throw new RuntimeException("No service specified");
        
        
        //this is applicable for special requests. example: json format is json:TestService.test
        //check if there are output filters specified
        String appFilter = (String) req.getAttribute(APP_FILTER);
        if(appFilter!=null) {
            int len = appFilter.length();
            service = pathInfo.substring(len, pathInfo.indexOf(".") );
        } else {
            service = pathInfo.substring( 1, pathInfo.indexOf(".") );
        }
        if( pathInfo.indexOf(".") <= 0 ) {
            throw new RuntimeException("Wrong servlet path format. It should be " + "appContext/serviceName.methodName" );
        }
        action = pathInfo.substring( pathInfo.indexOf(".")+1 );
        //extract the parameters
        
    }
    
    public String getService() {
        return service;
    }
    
    public String getAction() {
        return action;
    }
    
    public String getAppContext() {
        if(appContext==null ){
            try {
                AppContext.load();
                appContext = AppContext.getName();
            } catch(Exception e) {;}
        }
        return appContext;
    }
    
    public Object[] getArgs() {
        if(args==null) {
            Object _args = ServletUtils.getRequestInfo(req);
            
            if(_args instanceof SealedMessage) {
                encrypted = true;
                SealedMessage sm = (SealedMessage)_args;
                _args = sm.getMessage();
            }
            args = new Object[]{};
            if( _args !=null ) {
                if(_args instanceof Object[]) {
                    args = (Object[])_args;
                } else {
                    args = new Object[]{ _args };
                }
            }
        }
        return args;
    }
    
    public boolean isEncrypted() {
        return encrypted;
    }
    
    public String getAppContextService() {
        String ac = getAppContext();
        return (ac!=null && ac.trim().length()>0) ? ac + "/" + service : service;
    }
    
    
}
