package com.rameses.eserver;

import com.rameses.invoker.client.HttpClientManager;
import com.rameses.invoker.client.HttpInvokerClient;
import com.rameses.invoker.client.HttpScriptService;
import com.rameses.scripting.ScriptServiceLocal;
import com.rameses.server.common.*;
import java.util.Map;
import javax.naming.InitialContext;


public final class ScriptServiceDelegate {
    
    private static ScriptServiceLocal scriptService;
    
    public static ScriptServiceLocal getScriptService() throws Exception {
        if(scriptService==null) {
            InitialContext ctx = new InitialContext();
            scriptService = (ScriptServiceLocal)ctx.lookup( AppContext.getPath() + "ScriptService/local" );
        }
        return scriptService;
    }
    
    public static HttpScriptService createRemoteService(String hostKey, Map map) throws Exception {
        HttpInvokerClient client = HttpClientManager.getInstance().getService( hostKey, map );
        return new HttpScriptService(client);
    }    
    
}
