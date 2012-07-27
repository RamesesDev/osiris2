/*
 * ServiceInvoker.java
 *
 * Created on May 12, 2012, 9:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.web.support;

import com.rameses.service.ScriptServiceContext;
import com.rameses.service.ServiceProxy;
import com.rameses.util.ExceptionManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Elmo
 */
public class ServiceInvoker {
    
    /*
    <%@ attribute name="appcontext"%>
<%@ attribute name="host"%>
<%@ attribute name="service"%>
<%@ attribute name="method"%>
<%@ attribute name="var"%>
<%@ attribute name="json"%>
<%@ attribute name="params" rtexprvalue="true" type="java.lang.Object" required="false"%>
<%@ attribute name="env" rtexprvalue="true" type="java.lang.Object" required="false"%>
<%@ attribute name="debug"%>
     */
    
    private HttpServletRequest request;
    private ServletContext application;
    private Map env;
    
    public ServiceInvoker( ServletContext c, HttpServletRequest request, Map env) {
        this.request = request;
        this.application = c;
        this.env = env;
    }
    
    public Map createConf() {
        String host = application.getInitParameter("app.host");
        String  appcontext = application.getInitParameter("app.context");
        Map conf = new HashMap();
        conf.put("app.host", host );
        conf.put("app.context", appcontext );
        return conf;
    }
    
    public Map createEnv() {
        Map _env = new HashMap();
        if(request.getAttribute("SESSIONID")!=null) {
            _env.put("sessionid", request.getAttribute("SESSIONID"));
        }
        if( env != null  ) {
            _env.putAll( (Map) env );
        }
        
        /* add also for multi-tenants. add to env all that starts with ds */
        Map ext = (Map)request.getAttribute( "APP_CONF" );
        if( ext != null ) {
            Iterator iter = ext.entrySet().iterator();
            while(iter.hasNext()) {
                Object z = iter.next();
                Map.Entry me = (Map.Entry)z;
                if( me.getKey().toString().startsWith("ds.")) {
                    _env.put( me.getKey(), me.getValue() );
                }
            }
        }
        return _env;
    }
    
    public <T> T create( String service, Class<T> intf ) {
        Map conf = createConf();
        Map _env = createEnv();
        ScriptServiceContext svc = new ScriptServiceContext(conf);
        if( intf == null )
            return (T) svc.create( service, _env );
        return svc.create( service, _env, intf );
    }
    
    public Object invoke( String service, String method, Object params )  {
        try {
            Map conf = createConf();
            Map _env = createEnv();
            
            ScriptServiceContext svc = new ScriptServiceContext(conf);
            ServiceProxy ac = (ServiceProxy) svc.create( service, _env );
            Object o = null;
            
            if(params==null) {
                o = ac.invoke(method);
            } else if( params instanceof javax.servlet.http.HttpServletRequest) {
                
                HttpServletRequest req = (HttpServletRequest)params;
                Map m = RequestUtils.requestMap(req);
                o = ac.invoke(method, new Object[]{m});
            } else if(params instanceof List) {
                o = ac.invoke(method, ((List) params).toArray() );
            } else if(params instanceof Object[]) {
                o = ac.invoke(method, (Object[]) params );
            } else {
                o = ac.invoke(method, new Object[]{params});
            }
            return o;
        } catch(Exception ex) {
            ex = ExceptionManager.getOriginal(ex);
            throw new RuntimeException(ex);
        }
        
        
    }
    
}
