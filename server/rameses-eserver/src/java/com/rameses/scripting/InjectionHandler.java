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
import com.rameses.eserver.*;
import com.rameses.annotations.Env;
import com.rameses.annotations.Resource;
import com.rameses.annotations.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.InitialContext;

/**
 *
 * @author elmo
 */
public class InjectionHandler implements AnnotationFieldHandler {
    
    private Map<String, Object> map = new Hashtable<String, Object>();
    private javax.ejb.SessionContext ctx;
    private Map env;
    private String callerService;
   
    public InjectionHandler(String callerService, javax.ejb.SessionContext ctx, Map env) {
        this.callerService = callerService;
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
        if( annotation instanceof com.rameses.annotations.Resource) {
            String resname = correctValue(((Resource)annotation).value());
            return lookup(resname,null);
        } 
        else if( annotation instanceof com.rameses.annotations.Service) {
            //check first if the script info is already cached. if not yet then cache it
            Service asvc = (Service)annotation;
            String scriptname = correctValue(asvc.value());
            String host = correctValue(asvc.host());
            ScriptMgmtMBean mbean = (ScriptMgmtMBean)lookup(CONSTANTS.SCRIPT_MGMT,null);
            
            if(host==null || host.trim().length()==0) {
                if(scriptname==null || scriptname.trim().length()==0) scriptname = callerService;
                return mbean.createLocalProxy(scriptname, env );
            }    
            else {
                if(scriptname==null || scriptname.trim().length()==0) 
                    throw new IllegalStateException("Please provide a remote service name value for @Service");
                return mbean.createRemoteProxy(scriptname, env, host );
            }
        }
        
        else if( annotation instanceof com.rameses.annotations.Invoker) {
            return new InvokerLookup( callerService, env );
        }
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
            PersistenceMgmtMBean sql = (PersistenceMgmtMBean)lookup(CONSTANTS.PERSISTENCE_MGMT,null);
            String dsName = correctValue(((com.rameses.annotations.SqlContext)annotation).value());
            if(dsName!=null && dsName.trim().length()>0)
                return sql.createSqlContext( dsName );
            else
                return sql.createSqlContext();
        }
        
        else if(annotation instanceof com.rameses.annotations.PersistenceContext) {
            PersistenceMgmtMBean pm = (PersistenceMgmtMBean)lookup(CONSTANTS.PERSISTENCE_MGMT,null);
            String dsName = correctValue(((com.rameses.annotations.PersistenceContext)annotation).value());
            if(dsName!=null && dsName.trim().length()>0)
                return pm.createPersistenceContext(dsName);
            else    
                return pm.createPersistenceContext(null);
        }
        
        return null;
    }
    
    private static final Pattern p = Pattern.compile("#\\{.*?\\}");
    
    private String correctValue(String name) {
        if( name == null ) return null;
        if(name.trim().length()==0) return "";
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
    
    
    public void destroy() {
        map.clear();
        map = null;
        ctx = null;
    }
     
    
}
