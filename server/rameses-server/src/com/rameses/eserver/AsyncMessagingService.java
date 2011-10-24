package com.rameses.eserver;

import com.rameses.messaging.BasicFileEventQueue;

import com.rameses.invoker.client.DynamicHttpInvoker;
import com.rameses.messaging.ByteQueueMessage;
import com.rameses.messaging.EventHandler;
import com.rameses.messaging.QueueMessage;
import com.rameses.messaging.TextQueueMessage;
import com.rameses.server.common.AppContext;
import com.rameses.server.common.JndiUtil;
import com.rameses.eserver.ScriptServiceDelegate;
import java.io.Serializable;
import java.util.HashMap;
import javax.naming.InitialContext;

public class AsyncMessagingService implements AsyncMessagingServiceMBean, Serializable {
    
    private BasicFileEventQueue provider;
    
    private String name;
    private String host = "localhost:8080";
    private String appContext;
    private String scriptName;
    private String method;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    private boolean initialized = false;
    
    private void init() throws Exception {
        if(initialized) return;
        initialized = true;
        String rootPath = System.getProperty("jboss.server.home.dir")+"/output/log";
        provider = new BasicFileEventQueue(name,rootPath);
        provider.addEventHandler( new ScriptHandler() );
    }
    
    public void start() throws Exception{
        System.out.println("STARTING ASYNC MESSAGING SERVICE");
        init();
        
        provider.start();
        InitialContext ctx = new InitialContext();
        JndiUtil.bind( ctx, AppContext.getPath()+ AsyncMessagingService.class.getSimpleName(), this );
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING ASYNC MESSAGING SERVICE");
        provider.stop();
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind( ctx, AppContext.getPath()+ AsyncMessagingService.class.getSimpleName() );
    }
    
    public void sendMessage(final String message) {
        provider.send( new QueueMessage(){
            public Object getMessage() {
                return message;
            }
        });
    }
    
    public class ScriptHandler implements EventHandler {
        final String svcname = getScriptName();
        final String smethod = getMethod();
        public void onMessage(QueueMessage msg) {
            String parms = null;
            if(msg instanceof TextQueueMessage ) {
                TextQueueMessage txt = (TextQueueMessage)msg;
                parms =(String) txt.getMessage();
            } 
            else if(msg instanceof ByteQueueMessage ) {
                ByteQueueMessage txt = (ByteQueueMessage)msg;
                parms = new String((byte[])txt.getMessage());
            }
            
            try {
                if( host.startsWith("localhost")) {
                    ScriptServiceDelegate.getScriptService().invoke(scriptName,method,new Object[]{parms},new HashMap());
                } else {
                    if(appContext == null )appContext = AppContext.getPath();
                    DynamicHttpInvoker invoker = new DynamicHttpInvoker(host,appContext);
                    DynamicHttpInvoker.Action action = invoker.create(scriptName);
                    action.invoke(method, new Object[]{ parms});
                }
            } catch(Exception e) {
                System.out.println("LOG ERROR->" + e.getMessage());
            }
        }
    }
    
    public String getScriptName() {
        return scriptName;
    }
    
    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getAppContext() {
        return appContext;
    }
    
    public void setAppContext(String appContext) {
        this.appContext = appContext;
    }

    public void pause() throws Exception{
        provider.stop();
    }

    public void resume() throws Exception {
        provider.start();
    }
    
    
}
