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
import com.rameses.interfaces.ScriptServiceLocal;
import com.rameses.annotations.Async;
import com.rameses.annotations.ProxyMethod;
import com.rameses.eserver.CONSTANTS;
import com.rameses.eserver.SchemaMgmtMBean;
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
            
            ScriptObject scriptObj = scriptMgmt.getScriptObject(name);

            Object target = scriptObj.getTargetClass().newInstance();
            ClassDef classDef = scriptObj.getClassDef();
            Method actionMethod = classDef.findMethodByName( method );
            
            //before firing method do parameter checks here.
            checkParameters( scriptObj, method, params );
            
            
            boolean async = (!bypassAsync && actionMethod.isAnnotationPresent(Async.class));
            if(!async) {
                
                
                injectionHandler = new InjectionHandler(name, context,env);
                classDef.injectFields( target, injectionHandler );
                
                //check if interceptors should fire. This is applied only to all proxy methods.
                boolean applyInterceptors = false;
                if(actionMethod.isAnnotationPresent(ProxyMethod.class)) applyInterceptors = true;
                
                
                //fire before interceptors;
                String fullName = name + "." + method;
                
                if( applyInterceptors ) {
                    ActionEvent ae = null;
                    ScriptEval se = null;
                    try {
                        
                        //make sure to load interceptors first
                        scriptMgmt.loadInterceptors( scriptObj, name, method );
                        
                        
                        //start transaction
                        ae = new ActionEvent( scriptObj.getName(), actionMethod.getName(), params, env);
                        se = new ScriptEval(ae);
                        ScriptServiceLocal  scriptService = (ScriptServiceLocal)context.lookup("ScriptService/local");
                        for(String b: scriptObj.findBeforeInterceptors(method)) {
                            Object test = executeInterceptor(b, ae, se, scriptService, env);    
                            if(test!=null && (test instanceof Exception )) return test;
                        }
                        Object retval =  actionMethod.invoke( target, params );
                        
                        //do not proceed with other interceptors if the return value is an exception
                        if( retval instanceof Exception )
                            return retval;
                        
                        ae.setResult(retval);
                        for(String b: scriptObj.findAfterInterceptors(method)) {
                            Object test = executeInterceptor(b, ae, se, scriptService, env);    
                            if(test!=null && (test instanceof Exception )) return test;
                        }
                        return retval;
                    }
                    catch(Exception e) {
                        throw e;
                    }
                    finally {
                        if(se!=null) se.destroy();
                        if(ae!=null) ae.destroy();
                    }
                }
                else {
                    //invoke the actual method
                    try {
                        return actionMethod.invoke( target, params );
                    }
                    catch(Exception ex) {
                        throw ex;
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
                boolean hasReturnType = (!actionMethod.getReturnType().toString().equals("void"));
                String origin = System.getProperty("jboss.bind.address");
                boolean loop = asc.loop();
                
                pass.put("script", name);
                pass.put("method", method);
                pass.put("params", params);
                pass.put("origin", origin);
                pass.put("env", env);
                pass.put("hasReturnType", hasReturnType);
                
                //apply response handler only if there is a return type
                if(hasReturnType) {
                    if( responseHandler!=null ) pass.put("responseHandler", responseHandler);
                    if( loop ) {
                        pass.put("loop", true);
                        pass.put( "loopVar", asc.loopVar() );
                    }
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
    private Object executeInterceptor(String serviceName, ActionEvent ae, ScriptEval se, ScriptServiceLocal scriptService, Map env) {
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
        Object retval = null;
        if(passEval) {
            if(hasParm)
                retval = scriptService.invoke( n,_action,new Object[]{ae},env);
            else
                retval = scriptService.invoke(n,_action,new Object[]{},env);
        }
        return retval;
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
        String proxyInterface = scriptMgmt.getScriptObject(name).getProxyIntfScript();
        if(proxyInterface==null)
            throw new IllegalStateException("Proxy interface " + name + " not found. Please ensure that there is at least one @ProxyMethod");
        return proxyInterface.getBytes();
    }
    
    
    private void checkParameters( ScriptObject obj, String method, Object[] args ) throws Exception {
        CheckedParameter[] checkedParams = obj.getCheckedParameters(method);
        for( CheckedParameter p : checkedParams ) {
            if(p.isRequired() && args[p.getIndex()]==null ) 
                throw new Exception( "argument " + p.getIndex() + " for method " + method + " must not be null" );
            
            String schemaName = p.getSchema(); 
            if(schemaName!=null && schemaName.trim().length()>0) {
                SchemaMgmtMBean schemaMgmt = (SchemaMgmtMBean)context.lookup(CONSTANTS.SCHEMA_MGMT);
                schemaMgmt.validate(schemaName, args[p.getIndex()] );
            }
        }
        
    }
    
}
