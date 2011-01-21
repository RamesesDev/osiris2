package com.rameses.eserver;


import com.rameses.invoker.client.HttpScriptService;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJBException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public abstract class AbstractScriptMDB implements MessageListener {
    
    public abstract boolean isRemote();
    
    public void onMessage(Message message) {
        String requestId = null;
        try {
            Map map = (Map)((ObjectMessage)message).getObject();
            if( map == null ) return;
            
            //script must be marked with ~ so it will ignore async annotation
            String script = (String)map.get("script");
            String method = (String)map.get("method");
            Object[] params = (Object[])map.get("params");
            boolean hasReturnType = Boolean.valueOf(map.get("hasReturnType")+"");
            
            Map env = (Map)map.get("env");
            String origin = (String)map.get("origin");
            String originAppContext = (String)map.get("app.context");
            String originPort = (String)map.get("originPort");
            
            String host = System.getProperty("jboss.bind.address");
            
            String responseHandler = (String)map.get("responseHandler");
            requestId = (String)map.get("requestId");
            boolean loop = (map.get("loop")!=null) ? true : false;
            
            Map ae = new HashMap();
            ae.put("loop", 0);
            ae.put("requestId", requestId);
            env.put("async-event", ae);
            
            //execute the result;
            if(!loop) {
                Object result = ScriptServiceDelegate.getScriptService().invoke( "~" +script,method,params,env );
                if(hasReturnType) executeResponse( responseHandler, script, result, env, origin, requestId, originAppContext, originPort);
            } else {
                while( true ) {
                    int i = Integer.parseInt(ae.get("loop")+"");
                    ae.put("loop", i + 1);
                    Object result = ScriptServiceDelegate.getScriptService().invoke("~" +script,method,params,env);
                    if(result!=null) {
                        if(hasReturnType) executeResponse( responseHandler, script, result, env, origin, requestId, originAppContext, originPort);
                        if( result instanceof Exception ) break;
                    } else {
                        break;
                    }
                }
            }
        } catch(Exception ex) {
            throw new EJBException(ex);
        }
    }
    
    private void executeResponse( String responseHandler, String script,Object result, Map env, String origin, String requestId, String originAppContext, String originPort ) throws Exception {
        if(responseHandler!=null) {
            String method = null;
            if( responseHandler.contains(".") ) {
                String[] arr = responseHandler.split("\\.");
                script = arr[0];
                method = arr[1];
            } else {
                method = responseHandler;
            }
            if(!isRemote()) {
                ScriptServiceDelegate.getScriptService().invoke(script,method,new Object[]{result},env);
            } else {
                Map m = new HashMap();
                m.put("response.host", origin + ":" + originPort );
                m.put("app.context", originAppContext );
                HttpScriptService svc = ScriptServiceDelegate.createRemoteService( "response.host", m );
                svc.invoke(script,method,new Object[]{result},env);
            }
        } else {
            //send response back to original requester which is the machine key of the client.
            if(!isRemote() && result!=null) {
                ScriptServiceDelegate.getScriptService().pushResponse( requestId, result );
            } else {
                Map m = new HashMap();
                m.put("response.host", origin + ":" + originPort );
                m.put("app.context", originAppContext );
                HttpScriptService svc = ScriptServiceDelegate.createRemoteService( "response.host", m );
                svc.pushResponse(requestId,  result );
            }
        }
    }
    
}
