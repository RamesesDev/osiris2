package com.rameses.eserver.async;


import java.util.HashMap;
import java.util.Map;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class ScriptMDB implements MessageListener {
    
    public void onMessage(Message message) {
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
            boolean sameServer = true;
            
            String host = System.getProperty("jboss.bind.address");
            if(host!=null) sameServer = host.equals(origin);
            
            String responseHandler = (String)map.get("responseHandler");
            String requestId = (String)map.get("requestId");
            boolean loop = (map.get("loop")!=null) ? true : false;
            
            //execute the result;
            if(!loop) {
                Object result = ScriptDelegate.getInstance().invoke("~" +script,method,params,env);
                if(hasReturnType) executeResponse( responseHandler, script, result, env, origin, sameServer, requestId);
            } else {
                if(env==null) env = new HashMap();

                Map ae = new HashMap();
                ae.put("loop", 0);
                env.put("async-event", ae);
                
                
                while( true ) {
                    int i = Integer.parseInt(ae.get("loop")+"");
                    ae.put("loop", i + 1);
                    Object result = ScriptDelegate.getInstance().invoke("~" +script,method,params,env);
                    if(result!=null) {
                        if(hasReturnType) executeResponse( responseHandler, script, result, env, origin, sameServer, requestId);
                    } else {
                        break;
                    }
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void executeResponse( String responseHandler, String script,Object result, Map env, String origin, boolean sameServer, String requestId ) throws Exception {
        Map m = new HashMap();
        m.put("response.host", origin + ":8080" );
        if(responseHandler!=null) {
            String method = null;
            if( responseHandler.contains(".") ) {
                String[] arr = responseHandler.split("\\.");
                script = arr[0];
                method = arr[1];
            } else {
                method = responseHandler;
            }
            if(sameServer) {
                ScriptDelegate.getInstance().invoke(script,method,new Object[]{result},env);
            } else {
                //RemoteDelegate.getScriptService("response.host",m).invoke(script,method,new Object[]{result},env);
            }
        } else {
            
            //send response back to original requester which is the machine key of the client.
            if(sameServer && result!=null) {
                ScriptDelegate.getInstance().pushResponse( requestId, result, env );
            } else {
                //RemoteResponseService rr = RemoteDelegate.getResponseService("response.host", m);
                //rr.pushResponse(requestId, result);
            }
        }
    }
    
}
