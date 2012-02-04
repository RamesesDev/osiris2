/*
 * JsonInvoker.java
 * Created on April 1, 2011, 4:30 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.support;

import com.rameses.server.common.JsonUtil;

import com.rameses.service.ScriptServiceContext;
import com.rameses.service.ServiceProxy;
import com.rameses.util.ExceptionManager;
import com.rameses.web.common.RequestParser;
import com.rameses.web.common.ServletUtils;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * This is the main invoker for the rameses-proxy.js script
 */
public class JsonInvoker extends AbstractScriptService {
    
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Object result = null;
        String action = null;
        try {
            RequestParser np = new RequestParser(req);
            Map conf = super.getConf();
            
            //extract the parameters
            String _args = req.getParameter("args");
            Object[] args = null;
            if(_args!=null && _args.length()>0) {
                if(!_args.startsWith("["))
                    throw new RuntimeException("args must be enclosed with []");
                args = JsonUtil.toObjectArray( _args );
            }
            
            //extract the environment variables
            String _env = req.getParameter("env");
            
            Map env = new HashMap();
            if(_env!=null && _env.length()>0 ) {
                if( !_env.startsWith("{"))
                    throw new RuntimeException("env must be enclosed with {}");
                env = JsonUtil.toMap( _env );
            }
            ScriptServiceContext sv = new ScriptServiceContext(conf);
            ServiceProxy proxy = sv.create(np.getService(),env);
            result = proxy.invoke( np.getAction(), args );
            action = np.getService() + "." + np.getAction();
            String n = JsonUtil.toString( result );
            ServletUtils.writeText(res, n);
        } 
        catch(Exception e) {
            e.printStackTrace();
            e = ExceptionManager.getOriginal(e);
            Writer w = res.getWriter();
            w.write(e.getMessage());
            res.setStatus(res.SC_INTERNAL_SERVER_ERROR);
            //res.sendError(res.SC_INTERNAL_SERVER_ERROR, e.getMessage() );
        }
    }
    
    
    
    
}
