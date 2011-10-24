/*
 * SessionClient.java
 * Created on September 28, 2011, 12:00 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.server.session;

import com.rameses.client.session.MessageListener;
import com.rameses.client.session.Notifier;
import com.rameses.client.session.NotifierConnection;
import com.rameses.service.ScriptServiceContext;
import com.rameses.service.ServiceProxy;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Properties;

/**
 *
 * @author jzamss
 */
public class SessionNotifierService implements SessionNotifierServiceMBean{
    
    private String name;
    private Object info;
    private Properties conf;
    private Notifier notifier;
    
    public void start() throws Exception {
        System.out.println("STARTING SESSION CLIENT ["+ name + "]");
        if(name!=null) {
            NotifierConnection conn = new NotifierConnection(conf);
            notifier = conn.createConnection(name,info);
            notifier.addListener( new  MessageHandler());
            notifier.connect();
        }
    }
    
    public class MessageHandler implements MessageListener, Serializable {
        
        public void onMessage(Object data) {
            System.out.println("receiving message..." +data);
            ScriptServiceContext sc = new ScriptServiceContext(null);
            ServiceProxy p = sc.create("ScriptService");
            //p.invoke( scriptName, new Object[]{data});
        }
        
    }
    
    public void stop() throws Exception {
        System.out.println("STARTING SESSION CLIENT ["+ name + "]");
        notifier.stop();
    }
    
    public void setUsername(String name) {
        this.name = name;
    }
    
    public void setInfo(Object info) {
        this.info = info;
    }
    
    
    public String getUsername() {
        return name;
    }
    
    public Object getInfo() {
        return info;
    }
    
    public void setConf(String s) {
        try {
            this.conf = new Properties();
            this.conf.load( new ByteArrayInputStream(s.getBytes()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
