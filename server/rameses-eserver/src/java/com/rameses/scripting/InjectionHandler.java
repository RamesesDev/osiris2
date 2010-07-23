/*
 * ResourceMgr.java
 *
 * Created on June 26, 2009, 7:39 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.classutils.AnnotationFieldHandler;
import com.rameses.interfaces.ScriptServiceLocal;
import com.rameses.annotations.Env;
import com.rameses.annotations.Resource;
import com.rameses.annotations.Service;
import com.rameses.annotations.SqlContext;
import com.rameses.sql.SqlManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author elmo
 */
public class InjectionHandler implements AnnotationFieldHandler {
    
    private Map<String, Object> map = new Hashtable<String, Object>();
    private javax.ejb.SessionContext ctx;
    private Map env;
    
   
    public InjectionHandler(javax.ejb.SessionContext ctx, Map env) {
        this.ctx = ctx;
        this.env = env;
    }
    
    
    public Object lookup(String name, Hashtable tbl) throws Exception {
        if( !map.containsKey(name)) {
            Object o = null;
            if( ctx != null ) {
                o = ctx.lookup(name);
            } else {
                InitialContext c = null;
                if(tbl==null) {
                    c = new InitialContext();
                } else {
                    c = new InitialContext(tbl);
                }
                o = c.lookup(name);
            }
            map.put(name, o);
        }
        return map.get(name);
    }
    
    public Object getResource(Field field, Annotation annotation) throws Exception {
        if( annotation instanceof Resource) {
            String resname = correctValue(((Resource)annotation).value());
            return lookup(resname,null);
        } 
        else if( annotation instanceof Service) {
            //check first if the script info is already cached. if not yet then cache it
            Service asvc = (Service)annotation;
            String scriptname = correctValue(asvc.value());
            String host = correctValue(asvc.host());
            
            return RemoteHttpInvokerProxy.getInstance().create(scriptname, host);
        }
        /*
        else if( annotation instanceof Script ) {
            String scriptname = ((Script)annotation).value();
            ScriptServiceLocal ssl = (ScriptServiceLocal)lookup(ScriptService.class.getSimpleName() + "/local", null) ;
            Class clazz = ssl.getScriptIntfClass(scriptname);
            return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new LocalScriptInvocationHandler(scriptname, ssl));
        } 
         */
        else if(annotation instanceof Env ) {
            return env;
        }
        else if(annotation instanceof com.rameses.annotations.AsyncEvent ) {
            return env.get(AsyncEvent.class.getName());
        }
        else if(annotation instanceof com.rameses.annotations.SessionContext) {
            return ctx;
        }
        else if(annotation instanceof com.rameses.annotations.SqlContext) {
            String dsName = correctValue(((SqlContext)annotation).value());
            DataSource ds = (DataSource)lookup(dsName,null);
            return new SqlManager(ds);
        }
        return null;
    }
    
    private static final Pattern p = Pattern.compile("#\\{.*?\\}");
    
    private String correctValue(String name) {
        if( name == null ) return null;
        Matcher m = p.matcher(name);
        StringBuffer sb = new StringBuffer();
        int start = 0;
        while(m.find()) {
            String s = m.group().substring(2, m.group().length()-1);
            sb.append( name.substring(start, m.start()) );
            sb.append( System.getProperty(s) );
            start = m.end();
        }
        sb.append( name.substring(start));
        return sb.toString();
    }
    
    
    
    public class LocalScriptInvocationHandler implements InvocationHandler {
        
        private ScriptServiceLocal local;
        private String scriptName;
        
        public LocalScriptInvocationHandler(String scriptName, ScriptServiceLocal local) {
            this.local = local;
            this.scriptName = scriptName;
        }
        
        public Object invoke(Object object, Method method, Object[] args) throws Throwable {
            if( method.getName().equals("toString") ) {
                return scriptName;
            }
            return local.invoke(scriptName, method.getName(), args, env );
        }
        
    }
    
    public void destroy() {
        map.clear();
        map = null;
        ctx = null;
    }
}
