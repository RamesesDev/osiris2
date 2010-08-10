/*
 * OsirisMethodResolver.java
 *
 * Created on July 5, 2010, 11:21 AM
 * @author jaycverg
 */

package com.rameses.osiris2.client;

import com.rameses.classutils.ClassDefUtil;
import com.rameses.messaging.ConnectionManager;
import com.rameses.messaging.Message;
import com.rameses.messaging.MessageListener;
import com.rameses.messaging.MessagingConnection;
import com.rameses.messaging.SystemMessage;
import com.rameses.rcp.common.ScheduledTask;
import com.rameses.rcp.common.Task;
import com.rameses.rcp.annotations.Async;
import com.rameses.rcp.common.AsyncEvent;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.util.MethodResolver;
import com.rameses.util.PropertyResolver;
import com.rameses.util.ValueUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;
import org.apache.commons.beanutils.MethodUtils;

public class OsirisMethodResolver implements MethodResolver {
    
    private PushMessageListner pushListener;
    private Map<String, AsyncResponseDelegate> respDelegates = new Hashtable();
    
    
    private AsyncResponseDelegate getResponseDelegate(String host) {
        if ( !respDelegates.containsKey(host) ) {
            respDelegates.put(host, new AsyncResponseDelegate(host));
        }
        
        return respDelegates.get(host);
    }
    
    private Object invokeMethod(Object xbean, String xaction, Object[] args, Class[] params ) throws Exception {
        if(params==null)
            return MethodUtils.invokeMethod(xbean, xaction, args);
        else
            return MethodUtils.invokeMethod(xbean, xaction, args, params);
    }
    
    public Object invoke(Object bean, String action, Class[] paramTypes, Object[] args) throws Exception {
        String xaction = action;
        Object xbean = bean;
        if( xaction.indexOf(".")>0) {
            xaction = action.substring(action.lastIndexOf(".")+1);
            String p = action.substring(0, action.lastIndexOf("."));
            PropertyResolver resolver = ClientContext.getCurrentContext().getPropertyResolver();
            xbean = resolver.getProperty(bean, p);
        }
        
        ClassDefUtil util = ClassDefUtil.getInstance();
        Method m = util.findMethodByName(xbean.getClass(), xaction);
        if ( m != null && m.isAnnotationPresent(Async.class) ) {
            Async async = (Async) m.getAnnotation(Async.class);
            String resp = async.responseHandler();
            String host = async.host();
            String connection = async.connection();
            
            String eventVar = null;
            Field f = util.findAnnotatedField(  xbean.getClass(), com.rameses.rcp.annotations.AsyncEvent.class );
            if(f!=null) eventVar = f.getName();
            
            AsyncAction aa = new AsyncAction(xbean, xaction, paramTypes, args, resp, host, async.loop(), eventVar );
            ClientContext.getCurrentContext().getTaskManager().addTask(aa);
            
            return null;
        }
        
        return invokeMethod(xbean, xaction, args, null);
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  AsyncAction (class)  ">
    public class AsyncAction extends Task {
        
        private Object bean;
        private String action;
        private Class[] paramTypes;
        private Object[] args;
        private String responseHandler;
        private String host;
        private boolean loop;
        private String eventVar;
        
        public AsyncAction(Object bean, String action, Class[] paramTypes, Object[] args, String respHandler, String host, boolean loop, String eventVar) {
            this.bean = bean;
            this.action = action;
            this.paramTypes = paramTypes;
            this.args = args;
            this.responseHandler = respHandler;
            this.host = host;
            this.loop = loop;
            this.eventVar = eventVar;
        }
        
        public boolean accept() {
            return true;
        }
        
        public void execute() {
            if(!loop) {
                executeMethod();
            } else {
                //if there is a loop var, check bean if there is a variable named loopVar
                AsyncEvent ae = new AsyncEvent();
                while(true) {
                    if ( !ValueUtil.isEmpty(eventVar) ) {
                        PropertyResolver res = ClientContext.getCurrentContext().getPropertyResolver();
                        res.setProperty(bean, eventVar,ae);
                    }
                    boolean _end = executeMethod();
                    if(_end) {
                        ae.flagLast();
                        break;
                    }
                    ae.nextLoop();
                }
            }
            setEnded(true);
        }
        
        //returns true if return value is null. This signals calling method to end loop
        private boolean executeMethod() {
            boolean retVal = false;
            try {
                
                // before invoking the async method, add CONSTANTS.HEADER_REPLY_TO property
                // to ClientContext headers
                MessagingConnection sysCon = ConnectionManager.getInstance().getConnection("system");
                if ( sysCon != null && sysCon.isConnected() ) {
                    String replyTo = sysCon.getUsername() + "@" + sysCon.getHost();
                    
                    Map headers = ClientContext.getCurrentContext().getHeaders();
                    headers.put(CONSTANTS.HEADER_REPLY_TO, replyTo);
                }
                
                Object o = invokeMethod(bean, action, args, null);
                if(o==null) retVal = true;
                //invoke a special method if the response is an async id.
                if ( (o != null) && (o instanceof String) && (o.toString().startsWith("ASYNC:"))) {
                    String reqId = o.toString();
                    if ( sysCon != null && sysCon.isConnected() ) {
                        AsyncPushResponse ar = new AsyncPushResponse(bean, responseHandler, reqId, host);
                        registerPushListener( sysCon, reqId, ar );
                        
                    } else {
                        AsyncPollTask apt = new AsyncPollTask(bean, responseHandler, reqId, host);
                        ClientContext.getCurrentContext().getTaskManager().addTask(apt);
                    }
                    
                    retVal = true;
                    
                } else if ( !ValueUtil.isEmpty(responseHandler) ) {
                    invokeMethod(bean, responseHandler, new Object[]{ o }, new Class[]{ Object.class });
                    
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
                retVal = true; //end if an exception is thrown
            }
            return retVal;
        }
        
        private void registerPushListener(MessagingConnection con, String reqId, AsyncPushResponse ar) {
            if ( pushListener == null ) {
                pushListener = new PushMessageListner();
                con.addMessageListener( pushListener );
            }
            pushListener.register(reqId, ar);
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  AsyncPollTask (class)  ">
    public class AsyncPollTask extends ScheduledTask {
        
        private int counter = 0;
        private int maxCount = 5;
        
        private Object bean;
        private String respHandler;
        private String reqId;
        private String host;
        
        
        public AsyncPollTask(Object bean, String respHandler, String reqId, String host) {
            this.bean = bean;
            this.respHandler = respHandler;
            this.reqId = reqId;
            this.host = host;
        }
        
        public long getInterval() {
            return 2000;
        }
        
        public void execute() {
            counter++;
            try {
                AsyncResponseDelegate ard = getResponseDelegate(host);
                while(true) {
                    Object obj = ard.getPollData( reqId );
                    if ( obj != null ) {
                        counter = 0;
                        invokeMethod(bean, respHandler, new Object[]{ obj }, null);
                    } else {
                        break;
                    }
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        public boolean isEnded() {
            return counter > maxCount;
        }
        
        public boolean isImmediate() {
            return true;
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  PushMessageListner (class)  ">
    public class PushMessageListner implements MessageListener {
        
        private Map<String, AsyncPushResponse> listeners = new Hashtable();
        
        public void register(String reqId, AsyncPushResponse listener) {
            if ( !listeners.containsKey(reqId) ) {
                listeners.put(reqId, listener);
            }
        }
        
        public void onMessage(Message message) {
            if ( message instanceof SystemMessage ) {
                SystemMessage msg = (SystemMessage) message;
                String key = msg.getRequestid();
                
                AsyncPushResponse resp = listeners.remove(key);
                if ( resp == null ) return;
                
                resp.onResponse( msg.getPushid() );
            }
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  AsyncPushResponse (class)  ">
    public class AsyncPushResponse {
        
        private Object bean;
        private String respHandler;
        private String reqId;
        private String host;
        
        
        public AsyncPushResponse(Object bean, String respHandler, String reqId, String host) {
            this.bean = bean;
            this.respHandler = respHandler;
            this.reqId = reqId;
            this.host = host;
        }
        
        public void onResponse(String pushId) {
            try {
                AsyncResponseDelegate ard = getResponseDelegate(host);
                System.out.println("push id is " + pushId);
                Object obj = ard.getPushData( pushId );
                System.out.println("response object is " + obj);
                if ( obj != null ) {
                    invokeMethod(bean, respHandler, new Object[]{ obj }, null);
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    //</editor-fold>
}
