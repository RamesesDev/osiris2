/*
 * OsirisMethodResolver.java
 *
 * Created on July 5, 2010, 11:21 AM
 * @author jaycverg
 */

package com.rameses.osiris2.client;

import com.rameses.classutils.ClassDefUtil;
import com.rameses.invoker.client.HttpClientManager;
import com.rameses.invoker.client.HttpInvokerClient;
import com.rameses.rcp.common.ScheduledTask;
import com.rameses.rcp.common.Task;
import com.rameses.common.annotations.Async;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.util.MethodResolver;
import com.rameses.util.PropertyResolver;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.MethodUtils;

public class OsirisMethodResolver implements MethodResolver {
    
    public Object invoke(Object bean, String action, Class[] paramTypes, Object[] args) throws Exception {
        String xaction = action;
        Object xbean = bean;
        if( xaction.indexOf(".")>0) {
            xaction = action.substring(action.lastIndexOf(".")+1);
            String p = action.substring(0, action.lastIndexOf("."));
            PropertyResolver resolver = ClientContext.getCurrentContext().getPropertyResolver();
            xbean = resolver.getProperty(bean, p);
        }
        
        Method m = ClassDefUtil.getInstance().findMethodByName(xbean.getClass(), xaction);
        if ( m != null && m.isAnnotationPresent(Async.class) ) {
            Async async = (Async) m.getAnnotation(Async.class);
            String resp = async.responseHandler();
            String host = async.host();
            AsyncAction aa = new AsyncAction(xbean, xaction, paramTypes, args, resp, host);
            ClientContext.getCurrentContext().getTaskManager().addTask(aa);
            return null;
        }
        
        return MethodUtils.invokeMethod(xbean, xaction, args);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  AsyncAction (class)  ">
    public class AsyncAction extends Task {
        
        private Object bean;
        private String action;
        private Class[] paramTypes;
        private Object[] args;
        private String responseHandler;
        private String host;
        
        
        public AsyncAction(Object bean, String action, Class[] paramTypes, Object[] args, String respHandler, String host) {
            this.bean = bean;
            this.action = action;
            this.paramTypes = paramTypes;
            this.args = args;
            this.responseHandler = respHandler;
            this.host = host;
        }
        
        public boolean accept() {
            return true;
        }
        
        public void execute() {
            setEnded(true);
            System.out.println("firing execute: bean is " + bean + ", action is " + action);
            try {
                Object o = MethodUtils.invokeMethod(bean, action, args);
                if ( o != null && o instanceof String && o.toString().startsWith("ASYNC:")) {
                    AsyncPollTask apt = new AsyncPollTask(bean, responseHandler, o.toString(), host);
                    ClientContext.getCurrentContext().getTaskManager().addTask(apt);
                    
                } else if ( responseHandler != null ) {
                    MethodUtils.invokeMethod(bean, responseHandler, new Object[]{ o }, new Class[]{ Object.class });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
                Map env = OsirisContext.getSession().getEnv();
                HttpInvokerClient client = HttpClientManager.getInstance().getService(host, env);
                System.out.println("getting result using id: " + reqId);
                List result = (List) client.invoke("ResponseService.getResponseData", new Object[]{ reqId });
                if ( result != null && result.size() > 0 ) {
                    counter = 0;
                    for ( Object o: result) {
                        MethodUtils.invokeMethod(bean, respHandler, new Object[]{ o });
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
}
