/*
 * ScriptService.java
 *
 * Created on October 16, 2010, 8:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.scripting.ScriptExecutor;
import com.rameses.scripting.ScriptManager;
import com.rameses.scripting.ScriptServiceLocal;
import java.io.Serializable;
import java.rmi.server.UID;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 *
 * @author ms
 *
 */
@Stateless
@Local(ScriptServiceLocal.class)
public class ScriptService implements ScriptServiceLocal {
    
    @Resource
    private SessionContext context;
    
    public byte[] getScriptInfo(String name) {
        return ScriptManager.getInstance().getProxyIntfBytes(name);
    }
    
    public Object invoke(String name, String method, Object[] params, Map env) {
        ScriptExecutor se = null;
        try {
            CustomResourceInjector si = new CustomResourceInjector(name,context,env,this);
            se = ScriptManager.getInstance().createExecutor( name, method,params, si );
            return se.execute(this,params,env);
        } catch(Exception e) {
            throw new EJBException(e);
        }
        finally {
            if(se!=null)se.close();
        }
    }
    
    public Object invokeAsync(Map map, String destination) {
        Connection conn = null;
        Session session = null;
        MessageProducer sender = null;
        try {
            //inject here additional info for the request
            String origin = System.getProperty("jboss.bind.address");
            map.put("origin", origin);
            map.put("app.context", AppContext.getName());
            
            String requestId = "ASYNC:"+new UID();
            /**
             * remote host = the server contains the actual code
             */
            if( destination == null || destination.equals("queue") )
                destination = "queue/" + AppContext.getPath() + "ScriptQueue";
            else
                destination = "topic/" + AppContext.getPath() + "ScriptTopic";
            conn = ((ConnectionFactory)context.lookup("java:/JmsXA")).createConnection();
            
            Destination dest = (Destination) context.lookup(destination);
            
            session = conn.createSession(true, 0);
            sender = session.createProducer(dest);
            map.put("requestId", requestId);
            
            ObjectMessage ob = session.createObjectMessage();
            ob.setObject((Serializable) map);
            sender.send(ob);
            return requestId;
        } catch(Exception e1) {
            throw new IllegalStateException(e1);
        } finally {
            try { sender.close(); } catch (JMSException ex) {}
            try { session.close(); } catch (JMSException ex) {}
            try { conn.close(); } catch (JMSException ex) { }
        }
    }

    public void pushResponse(String requestId, Object data) {
        System.out.println("pushing response " + requestId + " data->" + data);
    }

    public Object getPollData(String requestId) {
        System.out.println("removing response");
        return null;
    }
    
}
