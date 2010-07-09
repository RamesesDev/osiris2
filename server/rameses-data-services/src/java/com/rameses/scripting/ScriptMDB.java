package com.rameses.scripting;

import com.rameses.interfaces.ResponseServiceLocal;
import com.rameses.interfaces.ScriptServiceLocal;
import com.rameses.invoker.client.HttpClientManager;
import com.rameses.invoker.client.HttpInvokerClient;


import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class ScriptMDB implements MessageListener {
    
    @Resource(mappedName="ScriptService/local")
    private ScriptServiceLocal scriptService;
    
    @Resource(mappedName="ResponseService/local")
    private ResponseServiceLocal responseService;
    
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
                
                //execute the result;
                Object result = scriptService.invoke("~" +script,method,params,env);
                executeResponse( responseHandler, script, result, env, origin, sameServer, requestId);
            }   
        } catch(Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
    
     private void executeResponse( String responseHandler, String script,Object result, Map env, String origin, boolean sameServer, String requestId ) throws Exception {
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
                Map m = new HashMap();
                m.put("response.host", origin + ":8080" );
                HttpInvokerClient hc = HttpClientManager.getInstance().getService( "response.host", m );
                hc.invoke( "ScriptService.invoke", new Object[]{ script, method,  new Object[]{result} } );
            }
        } else {
            System.out.println("request Id to register " + requestId);
            if(sameServer && result!=null) {
                responseService.registerData( requestId, result );
            } else {
                Map m = new HashMap();
                m.put("response.host", origin + ":8080" );
                HttpInvokerClient hc = HttpClientManager.getInstance().getService( "response.host", m );
                hc.invoke( "ResponseService.registerData", new Object[]{ requestId, result } );
            }
        }
    }
   
    
    
}
