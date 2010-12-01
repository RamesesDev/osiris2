/*
 * InvokerProxy.java
 *
 * Created on June 28, 2009, 6:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client;

import com.rameses.invoker.client.AbstractScriptServiceProxy;
import com.rameses.invoker.client.ResponseHandler;
import com.rameses.rcp.common.ScheduledTask;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.invoker.client.ScriptInterfaceProvider;
import com.rameses.rcp.common.MsgBox;
import com.rameses.util.BreakException;
import com.rameses.util.ExceptionManager;
import groovy.lang.GroovyClassLoader;
import java.io.ByteArrayInputStream;
import java.util.Map;

public class InvokerProxy extends AbstractScriptServiceProxy {
    
    private GroovyClassLoader classLoader = new GroovyClassLoader(ClientContext.getCurrentContext().getClassLoader());
    private ScriptInterfaceProvider scriptProvider = new GroovyScriptInterfaceProvider();
    
    private static InvokerProxy instance;
    
    public synchronized static InvokerProxy getInstance() {
        if ( instance == null ) {
            instance = new InvokerProxy();
        }
        return instance;
    }
    
    
    public InvokerProxy() {
        super(OsirisContext.getSession().getEnv());
    }

    public Map getEnv() {
        return OsirisContext.getEnv();
    }
   
    public synchronized void reset() {
        classLoader = new GroovyClassLoader(ClientContext.getCurrentContext().getClassLoader());
        instance = null;
    }
    
    protected ScriptInterfaceProvider getScriptInterfaceProvider() {
        return scriptProvider;
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
                if ( handler.execute() ) {
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
        }
        
        public boolean accept() {
            return true;
        }
        
        public boolean isEnded() {
            return counter >= 10;
        }
        
    }
    
    
    private class GroovyScriptInterfaceProvider extends ScriptInterfaceProvider {
        
        protected Class parseClass(byte[] bytes) {
            return classLoader.parseClass( new ByteArrayInputStream(bytes));
        }
        
        public ClassLoader getProxyClassLoader() {
            return classLoader;
        }
    }
    
}
