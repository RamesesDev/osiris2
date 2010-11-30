/*
 * ScriptService.java
 *
 * Created on October 16, 2010, 8:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.common.AsyncResponse;
import com.rameses.scripting.ScriptExecutor;
import com.rameses.scripting.ScriptManager;
import com.rameses.scripting.ScriptServiceLocal;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.server.UID;
import java.util.HashMap;
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
        try {
            CustomResourceInjector si = new CustomResourceInjector(name,context,env,this);
            ScriptExecutor se = ScriptManager.getInstance().createExecutor( name, method,params, si );
            return se.execute(this,params,env);
        } catch(Exception e) {
            throw new EJBException(e);
        }
    }
    
    public Object invokeAsync(String name, String method, Object[] params, Map env, Map asyncInfo ) {
        Map pass = new HashMap();
        String destinationType = (String)asyncInfo.get("destination");
        if(destinationType.trim().length()==0) destinationType = "queue";
        
        String responseHandler = (String)asyncInfo.get("responseHandler");
        if(responseHandler==null || responseHandler.trim().length()==0) responseHandler = null;
        
        boolean hasReturnType = false;
        try {
            hasReturnType = Boolean.parseBoolean(asyncInfo.get("hasReturnType")+"");
        }catch(Exception ign){;}
        
        boolean loop = false;
        try {
            loop = Boolean.parseBoolean( asyncInfo.get("loop")+"" );
        }catch(Exception ign){;}
        
        pass.put("script", name);
        pass.put("method", method);
        pass.put("params", params);
        pass.put("env", env);
        pass.put("hasReturnType", hasReturnType);
        
        //apply response handler only if there is a return type
        if(hasReturnType) {
            if( responseHandler!=null ) pass.put("responseHandler", responseHandler);
            if( loop ) {
                pass.put("loop", true);
                pass.put( "loopVar", asyncInfo.get("loopVar") );
            }
        }
        return invokeAsync( pass, destinationType );
    }
    
    private Object invokeAsync(Map map, String destination) {
        Connection conn = null;
        Session session = null;
        MessageProducer sender = null;
        try {
            //inject here additional info for the request
            String origin = System.getProperty("jboss.bind.address");
            map.put("origin", origin);
            map.put("app.context", AppContext.getName());
            map.put("originPort", "8080");
            
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
            
            AsyncResponse response = new AsyncResponse();
            response.put("id", requestId);
            return response;
            
            
        } catch(Exception e1) {
            throw new IllegalStateException(e1);
        } finally {
            try { sender.close(); } catch (JMSException ex) {}
            try { session.close(); } catch (JMSException ex) {}
            try { conn.close(); } catch (JMSException ex) { }
        }
    }

    public void pushResponse(String requestId, Object data) {
        SqlContext ctx  = SqlManager.getInstance().createContext(AppContext.getSystemDs());
        ObjectOutputStream oos = null;
        ByteArrayOutputStream bos = null;
        try {
            ctx.openConnection();
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject( data );
            SqlExecutor qe = ctx.createNamedExecutor("eserver:async-push");
            qe.setParameter(1,"RESP:"+new UID());
            qe.setParameter(2, requestId );
            qe.setParameter(3, bos.toByteArray());
            qe.execute();
        }
        catch(Exception e) {
            throw new EJBException(e);
        }
        finally {
            ctx.closeConnection();
        }
    }

    public Object getPollData(String requestId) {
        SqlContext ctx  = SqlManager.getInstance().createContext(AppContext.getSystemDs());
        ObjectInputStream ois = null;
        try {
            ctx.openConnection();
            Map map = (Map)ctx.createNamedQuery("eserver:async-poll").setParameter(1,requestId).getSingleResult();
            if(map==null) return null;
            String objid = (String)map.get( "objid" );
            byte[] bytes = (byte[])map.get("data"); 
            ois = new ObjectInputStream( new ByteArrayInputStream(bytes) );
            Object returnObject =  ois.readObject();
            ctx.createNamedExecutor("eserver:async-remove").setParameter(1, objid).execute();
            return returnObject;
        }
        catch(Exception e) {
            throw new EJBException(e);
        }
        finally {
            ctx.closeConnection();
            try {ois.close();} catch(Exception ign){;}
        }
    }

    
    
}
