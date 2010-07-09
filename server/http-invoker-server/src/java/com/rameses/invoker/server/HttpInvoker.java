package com.rameses.invoker.server;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.beanutils.MethodUtils;

public class HttpInvoker extends HttpServlet {
    
    protected Object[] filterData( Object obj ) throws Exception {
        return  (Object[])obj;
    }
    
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        
        try {
            in = new ObjectInputStream(req.getInputStream());
            Object[] values = filterData( in.readObject() );
            StringBuffer reqPath = new StringBuffer();
            if ( req.getPathInfo() != null ) {
                reqPath.append(req.getPathInfo());
            } else if ( req.getServletPath() != null ) {
                reqPath.append(req.getServletPath());
            }
            
            String[] pathInfos = reqPath.toString().split("\\.");
            
            InitialContext ctx = new InitialContext();
            Object bean = ctx.lookup(pathInfos[0].substring(1) + "/local");
            Method[] methods = getMethodByName(bean, pathInfos[1], values.length);
            boolean methodFound = false;
            Object response = null;
            
            for (int i=0; i<methods.length; i++) {
                try {
                    response = MethodUtils.invokeMethod(bean, pathInfos[1], values, methods[i].getParameterTypes());
                    methodFound = true;
                    break; //break the loop
                } catch(NoSuchMethodException nsme) {;}
            }
            if (!methodFound)
                throw new Exception("No such method found '" + pathInfos[1] + "' for service '" + pathInfos[0]);
            
            if (response == null) {
                response = "#NULL";
            }
            
            out = new ObjectOutputStream(res.getOutputStream());
            out.writeObject(response);
        } catch (Exception ex) {
            Throwable t = ex;
            while( t.getCause() != null ) t = t.getCause();
            Exception ne = new Exception(t.getMessage());
            out = new ObjectOutputStream(res.getOutputStream());
            out.writeObject(ne);
        } finally {
            try { out.close(); } catch (Exception ex) {;}
        }
    }
    
    private Method[] getMethodByName(Object bean, String name, int argCount) throws Exception {
        List list = new ArrayList();
        Method[] methods = bean.getClass().getMethods();
        for (int i=0; i<methods.length; i++) {
            if (!methods[i].getName().equals(name)) continue;
            if (methods[i].getParameterTypes().length != argCount) continue;
            
            list.add(methods[i]);
        }
        return (Method[]) list.toArray(new Method[]{});
    }
    
}
