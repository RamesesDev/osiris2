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
import com.rameses.invoker.client.DynamicHttpInvoker;
import groovy.lang.GroovyClassLoader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ScriptGeneratorService extends HttpServlet {
    
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Writer w = res.getWriter();
        try {
            NameParser np = new NameParser(req);
            DynamicHttpInvoker hp = new DynamicHttpInvoker(np.getHost(),np.getContext());
            byte[] bytes = hp.getService().getScriptInfo( np.getService() );
            GroovyClassLoader loader = new GroovyClassLoader();
            Class clazz = loader.parseClass(new ByteArrayInputStream(bytes));
            Map mapClass = ClassDefMap.toMap(clazz);
            String s = JsonUtil.toString( mapClass );
            w.write(s);
        } catch(Exception e) {
            throw new ServletException(e);
        } finally {
            try { w.close(); } catch (Exception ex) {;}
        }
    }
    
    
}
