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
import com.rameses.common.AsyncHandler;
import com.rameses.invoker.client.DynamicHttpInvoker;
import groovy.lang.GroovyClassLoader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoteJSProxyService extends HttpServlet {
    
    private ServletConfig config;
    
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        super.init(config);
    }
    
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setHeader("cache-control", "public");
        res.setHeader("cache-control", "max-age: 86400");
        res.setContentType("text/javascript");
        Writer w = res.getWriter();
        try {
            NameParser np = new NameParser(req);
            String svc = np.getService();
            
            ServletContext app = this.config.getServletContext();
            String appContext = app.getInitParameter("app.context");
            String host = app.getInitParameter("app.host");
            
            if(host==null || host.trim().length()==0) host = np.getHost();
            DynamicHttpInvoker hp = new DynamicHttpInvoker(host,appContext);
            byte[] bytes = hp.getService().getScriptInfo( svc );
            
            GroovyClassLoader loader = new GroovyClassLoader();
            Class clazz = loader.parseClass(new ByteArrayInputStream(bytes));
            Map classMap = ClassDefMap.toMap(clazz);
            classMap.put("name", svc );
            writeJs( appContext, classMap, w );
            
        } catch(Exception e) {
            throw new ServletException(e);
        } finally {
            try { w.close(); } catch (Exception ex) {;}
        }
    }
    
    private void writeJs(String context, Map m, Writer w) throws Exception {
        String name = (String) m.get("name");
        w.write( "new function() {\n"  );
        w.write( "this.proxy =  new DynamicProxy(\"" + context + "\").create(\""+ name + "\");\n"  );
        
        List<Map> methods = (List)m.get("methods");
        for( Map mth : methods ) {
            //check first if the method contains an AsyncHandler
            StringBuffer args = new StringBuffer();
            StringBuffer parms = new StringBuffer();

            String methodName = (String)mth.get("name");
            List params = (List)mth.get("params");
            
            boolean includeMethod = true;
            int i=0;
            
            //search each parameter if it has AsyncHandler. do not continue if there is.
            for(i=0;i<params.size();i++) {
                String clz = (String)params.get(i);
                if(clz.equals(AsyncHandler.class.getName())) {
                    includeMethod = false;
                    break;
                }
                //arguments
                args.append( "p" + i + ",");
                
                //parameters
                if( i > 0 ) parms.append(", ");
                parms.append( "p" + i);
            }
            if(!includeMethod) continue;
            
            w.write("this." + methodName + "= function(");
            w.write(args.toString());
            w.write("handler ) {\n");
            if( !mth.get("returnType").equals("void") ) w.write("return ");
            w.write( "this.proxy.invoke(\"" + methodName + "\"" );
            w.write( ",");
            w.write("["+parms.toString()+"]");
            w.write(", handler ); \n");
            w.write("} \n");
        }
        
        w.write( "}" );
    }
    
    
    
}
