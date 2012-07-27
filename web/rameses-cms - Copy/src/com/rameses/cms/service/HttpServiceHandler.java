/*
 * HttpInvicationHandler.java
 *
 * Created on June 23, 2012, 3:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms.service;

import com.rameses.cms.JsonUtil;
import com.rameses.http.HttpClient;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class HttpServiceHandler extends AbstractServiceHandler {
    
    public String getName() {
        return "http";
    }
    
    protected AbstractServiceHandler.ServiceInvoker getServiceInvoker(String name, Map conf) {
        return new MyHttpInvoker(name, conf);
    }
    
    private class MyHttpInvoker implements AbstractServiceHandler.ServiceInvoker {
        private Map conf;
        private String ext;
        private HttpClient httpClient;
        private String name;
        private String host;
        
        public MyHttpInvoker( String name, Map m) {
            this.conf = m;
            this.name = name;
            this.ext = (String)conf.get("ext");
            if( this.ext!=null && !this.ext.startsWith(".")) {
                this.ext = "."+this.ext;
            }
            if( !conf.containsKey("host"))
                throw new RuntimeException("No host specified in conf");
            host = (String)conf.get("host");
            this.httpClient = new HttpClient(host+"/"+name);
        }
        
        public Object invoke(String methodName, Object[] args) {
            try {
                Object params = args;
                if( (args !=null) && (args.length == 1) && (args[0] instanceof Map) ) {
                    params = (Map)args[0];
                }
                String result = (String) httpClient.post( methodName + ext, params );
                String str = result.trim();
                if( str.startsWith("[") || str.startsWith("{")) {
                    return JsonUtil.toObject(result);
                } else {
                    return str;
                }
            } catch (Exception ex) {
                return "<font color=red>error service:" + ex.getMessage()+"</font>";
            }
        }
    }
    
    
}
