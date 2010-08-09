package com.rameses.scripting;

import com.rameses.interfaces.AsyncResponseServiceLocal;
import com.rameses.interfaces.ScriptServiceLocal;


import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class ScriptMDB implements MessageListener {
    
    @Resource(mappedName="ScriptService/local")
    private ScriptServiceLocal scriptService;
    
    @Resource(mappedName="AsyncResponseService/local")
    private AsyncResponseServiceLocal responseService;
    
    public void onMessage(Message message) {
        try {
            Map map = (Map)((ObjectMessage)message).getObject();
            if( map != null ) {
                //script must be marked with ~ so it will ignore async annotation
                String script = (String)map.get("script");
                String method = (String)map.get("method");
                Object[] params = (Object[])map.get("params");
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
                    Object result = scriptService.invoke("~" +script,method,params,env);
                    executeResponse( responseHandler, script, result, env, origin, sameServer, requestId);
                }
                else {
                    if(env==null) env = new HashMap();
                    AsyncEvent ae = new AsyncEvent();
                    env.put(AsyncEvent.class.getName(), ae);
                    
                    while( true ) {
                        ae.moveNext();
                        Object result = scriptService.invoke("~" +script,method,params,env);
                        if(result!=null) {
                            executeResponse( responseHandler, script, result, env, origin, sameServer, requestId);
                        }
                        else {
                            break;
                        }
                    }    
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
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
                scriptService.invoke(script,method,new Object[]{result},env);
            } else {
                RemoteDelegate.getScriptService("response.host",m).invoke(script,method,new Object[]{result},env);
            }
        } else {
            
            //send response back to original requester which is the machine key of the client.
            String machinekey = (String)env.get("machinekey");
            if(sameServer && result!=null) {
                responseService.registerData( requestId, machinekey, result );
            } else {
                RemoteDelegate.getResponseService("response.host", m).registerData(requestId, machinekey, result);
            }
        }
    }
    
}
