/*
 * InvokerService.java
 *
 * Created on June 26, 2009, 7:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.classutils.ClassDef;
import com.rameses.eserver.TransactionManager;
import com.rameses.interfaces.ScriptServiceLocal;

import com.rameses.annotations.After;
import com.rameses.annotations.Async;
import com.rameses.annotations.Before;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

@Stateless
@Local(ScriptServiceLocal.class)
public class ScriptService implements ScriptServiceLocal {
    
    @Resource(mappedName="ScriptMgmt")
    private ScriptMgmtMBean scriptMgmt;
    
    @javax.annotation.Resource()
    private SessionContext context;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Object invoke(String name, String method, Object[] params, Map env) {
        
        InjectionHandler injectionHandler = null;
        try {
            //check if name is marked with ~. If a method is marked this way,
            //it means this is being called from an async method then we need to bypass async
            //and call the procedure directly.
            boolean bypassAsync = false;
            if( name.startsWith("~") ) {
                bypassAsync = true;
                name = name.substring(1);
            }
            
            ScriptObject imeta = scriptMgmt.getScriptObject(name);
            
            Object target = imeta.getTargetClass().newInstance();
            ClassDef classDef = imeta.getClassDef();
            Method actionMethod = classDef.findMethodByName( method );
            
            boolean async = (!bypassAsync && actionMethod.isAnnotationPresent(Async.class));
            if(!async) {
                
                //inject the resources
                TransactionManager txnManager = new TransactionManager();
                injectionHandler = new InjectionHandler(context,env,txnManager);
                classDef.injectFields( target, injectionHandler );
                
                
                //check if interceptors should fire
                //normally if this is an interceptor method, then this should not fire.
                boolean hasInterceptors = true;
                if( actionMethod.isAnnotationPresent(Before.class)) hasInterceptors = false;
                else if( actionMethod.isAnnotationPresent(After.class)) hasInterceptors = false;
                
                //fire before interceptors;
                String fullName = name + "." + method;
                
                if( hasInterceptors ) {
                    ActionEvent ae = null;
                    ScriptEval se = null;
                    try {
                        //start transaction
                        txnManager.begin();                        
                        ae = new ActionEvent( imeta.getName(), actionMethod.getName(), params);
                        se = new ScriptEval(ae);
                        ScriptServiceLocal  scriptService = (ScriptServiceLocal)context.lookup("ScriptService/local");
                        for(String b: scriptMgmt.findBeforeInterceptors(fullName)) {
                            executeInterceptor(b, ae, se, scriptService, env);    
                        }
                        Object retval =  actionMethod.invoke( target, params );
                        ae.setResult(retval);
                        for(String b: scriptMgmt.findAfterInterceptors(fullName)) {
                            executeInterceptor(b, ae, se, scriptService, env);    
                        }
                        txnManager.commit();
                        return retval;
                    }
                    catch(Exception e) {
                        txnManager.rollBack();
                        throw e;
                    }
                    finally {
                        try { txnManager.close(); } catch(Exception ign){;}
                        if(se!=null) se.destroy();
                        if(ae!=null) ae.destroy();
                    }
                }
                else {
                    //invoke the actual method
                    try {
                        txnManager.begin();
                        Object retval = actionMethod.invoke( target, params );
                        txnManager.commit();
                        return retval;
                    }
                    catch(Exception ex) {
                        txnManager.rollBack();
                        throw ex;
                    }
                    finally {
                        try { txnManager.close(); } catch(Exception ign){;}
                    }
                }    
            } 
            else {
                /************************************************
                 * ASYNCHRONOUS PROCESS
                 ***********************************************/
                
                Map pass = new HashMap();
                Async asc = (Async) actionMethod.getAnnotation(Async.class);
                String destinationType = asc.type();
                if(destinationType.trim().length()==0) destinationType = "queue";
                String responseHandler = asc.responseHandler();
                if(responseHandler.trim().length()==0) responseHandler = null;
                
                String origin = System.getProperty("jboss.bind.address");
                boolean loop = asc.loop();
                
                pass.put("script", name);
                pass.put("method", method);
                pass.put("params", params);
                pass.put("origin", origin);
                pass.put("env", env);
                if( responseHandler!=null ) pass.put("responseHandler", responseHandler);
                if( loop ) {
                    pass.put("loop", true);
                    pass.put( "loopVar", asc.loopVar() );
                }
                
                return invokeAsync( pass, destinationType );
            }
        } catch(Exception ex) {
            Throwable e = ex;
            while( e.getCause()!=null) {
                e = e.getCause();
            }
            throw new EJBException(e.getMessage());
        } finally {
            if(injectionHandler!=null) injectionHandler.destroy();
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void executeInterceptor(String serviceName, ActionEvent ae, ScriptEval se, ScriptServiceLocal scriptService, Map env) {
        boolean hasParm = false;
        boolean passEval = true;
        
        if( serviceName.indexOf("#")>0) {
            String[] sarr = serviceName.split("#");
            serviceName = sarr[0];
            se.setResult(ae.getResult());
            passEval = se.eval(sarr[1]);
        }
        
        String[] arr = serviceName.split("\\.");
        String n = arr[0];
        if(n.startsWith("@")) {
            hasParm = true;
            n = n.substring(1);
        }
        String _action = arr[1];
        if(passEval) {
            if(hasParm)
                scriptService.invoke( n,_action,new Object[]{ae},env);
            else
                scriptService.invoke(n,_action,new Object[]{},env);
        }
    }
    
    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private Object invokeAsync( Map map,String destination ) {
        Connection conn = null;
        Session session = null;
        MessageProducer sender = null;
        try {
            String requestId = "ASYNC:"+new UID();
            /**
             * remote host = the server contains the actual code
             */
            if( destination == null || destination.equals("queue") )
                destination = "queue/ScriptQueue";
            else
                destination = "topic/ScriptTopic";
            
            
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
    
    
    public byte[] getScriptInfo(String name) {
        return scriptMgmt.getScriptObject(name).getProxyInterface();
    }
    
    
    
    
}
