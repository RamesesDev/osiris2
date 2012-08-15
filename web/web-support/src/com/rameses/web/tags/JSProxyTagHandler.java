/*
 * GroovyScriptTagHandler.java
 *
 * Created on February 4, 2012, 10:28 PM
 */

package com.rameses.web.tags;

import com.rameses.classutils.ClassDefMap;
import com.rameses.service.EJBServiceContext;
import com.rameses.service.ServiceProxy;
import com.rameses.web.support.JSProxyWriter;
import groovy.lang.GroovyClassLoader;
import java.io.ByteArrayInputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.*;

/**
 *
 * @author  jaycverg
 */

public class JSProxyTagHandler extends TagSupport 
{
    private String service;

    public int doEndTag() throws JspException 
    {
        try {
            Writer w = pageContext.getOut();
            
            EJBServiceContext sv = new EJBServiceContext( getConf() );
            ServiceProxy proxy = sv.create("ScriptService/local");
            byte[] bytes = (byte[])proxy.invoke( "getScriptInfo", new Object[]{service} );

            GroovyClassLoader loader = new GroovyClassLoader();
            Class clazz = loader.parseClass(new ByteArrayInputStream(bytes));
            Map classMap = ClassDefMap.toMap(clazz);
            classMap.put("name", service );

            new JSProxyWriter(classMap, false).write(w);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return super.doEndTag();
    }
    
    private Map getConf() 
    {
        ServletContext app = pageContext.getServletContext();
        String appContext = app.getInitParameter("app.context");
        String host = app.getInitParameter("app.host");
        Map map = new HashMap();
        if(appContext!=null) map.put("app.context", appContext);
        if(host!=null)  map.put("app.host", host);
        return map;
    }

    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
    //</editor-fold>
}
