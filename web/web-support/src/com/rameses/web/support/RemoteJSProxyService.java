/*
 * JsonInvoker.java
 * Created on April 1, 2011, 4:30 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.support;

import com.rameses.classutils.ClassDefMap;
import com.rameses.service.EJBServiceContext;
import com.rameses.service.ServiceProxy;
import com.rameses.web.common.RequestParser;
import groovy.lang.GroovyClassLoader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoteJSProxyService extends AbstractScriptService {
    
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setHeader("cache-control", "public");
        res.setHeader("cache-control", "max-age: 86400");
        Writer w = res.getWriter();    
        try {
            Map conf = super.getConf();
            RequestParser np = new RequestParser(req);
            String svc = np.getService();
            
            EJBServiceContext sv = new EJBServiceContext(conf);
            ServiceProxy proxy = sv.create("ScriptService/local");
            byte[] bytes = (byte[])proxy.invoke( "getScriptInfo", new Object[]{svc} );

            GroovyClassLoader loader = new GroovyClassLoader();
            Class clazz = loader.parseClass(new ByteArrayInputStream(bytes));
            Map classMap = ClassDefMap.toMap(clazz);
            classMap.put("name", svc );

            new JSProxyWriter(classMap).write(w);
            
        } catch(Exception e) {
            res.sendError(res.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            try { w.close(); } catch (Exception ex) {;}
        }
    }
    
}
