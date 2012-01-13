/*
 * InvokerProxy.java
 *
 * Created on June 28, 2009, 6:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client;

import com.rameses.invoker.client.ResponseHandler;
import com.rameses.rcp.common.ScheduledTask;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.common.MsgBox;
import com.rameses.service.EJBServiceContext;
import com.rameses.util.BreakException;
import com.rameses.util.ExceptionManager;
import groovy.lang.GroovyClassLoader;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class InvokerProxy  {
    
    private GroovyClassLoader classLoader = new GroovyClassLoader(ClientContext.getCurrentContext().getClassLoader());
    
    
    private static InvokerProxy instance;
    private Map env;
    
    public InvokerProxy() {
    }
    
    public synchronized static InvokerProxy getInstance() {
        if ( instance == null ) {
            instance = new InvokerProxy();
        }
        return instance;
    }
    
    public Map getAppEnv() {
        return OsirisContext.getSession().getEnv();
    }
    
    public synchronized void reset() {
        classLoader = new GroovyClassLoader(ClientContext.getCurrentContext().getClassLoader());
        instance = null;
    }
    
    public void invokeLater(ResponseHandler handler) {
        AsyncTask task = new AsyncTask(handler);
        ClientContext.getCurrentContext().getTaskManager().addTask(task);
    }
    
    
    private class AsyncTask extends ScheduledTask {
        
        private ResponseHandler handler;
        private int counter = 0;
        
        
        AsyncTask(ResponseHandler handler) {
            this.handler = handler;
        }
        
        public long getInterval() {
            return 2000;
        }
        
        public void execute() {
            try {
                if ( handler.isCancelled() ) {
                    counter = 10;
                } else if ( handler.execute() ) {
                    counter = 0;
                } else {
                    counter++;
                }
            } catch(BreakException be) {
                counter = 10;
            } catch(Exception e) {
                MsgBox.err( ExceptionManager.getOriginal(e) );
                counter = 10;
            }
            
            if( isEnded() ) {
                handler.end();
            }
        }
        
        public boolean accept() {
            return true;
        }
        
        public boolean isEnded() {
            return counter >= 10;
        }
        
    }
    
    
    private interface ScriptInterfaceService {
        byte[] getScriptInfo(String name);
    }
    
    private Map<String,Class> interfaces = new HashMap();
    public synchronized Object create(String name) {
        try {
            Class cls = interfaces.get(name);
            if(cls==null) {
                if( !interfaces.containsKey(name) ) {
                    EJBServiceContext ect = new EJBServiceContext(getAppEnv());
                    ScriptInterfaceService s = ect.create( "ScriptService/local", ScriptInterfaceService.class );
                    byte[] bytes = s.getScriptInfo( name );
                    cls = classLoader.parseClass( new ByteArrayInputStream(bytes));
                    interfaces.put( name, cls );
                }
            }
            return Proxy.newProxyInstance(classLoader, new Class[]{cls}, new OsirisInvokerHandler( name, getAppEnv()));
            
        } catch(Exception e) {
            if( ClientContext.getCurrentContext().isDebugMode() ) {
                e.printStackTrace();
            }
            throw new RuntimeException(e);
        }
    }
}
