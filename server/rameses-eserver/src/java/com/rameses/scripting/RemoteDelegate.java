/*
 * RemoteDelegate.java
 *
 * Created on July 27, 2010, 11:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.eserver.CONSTANTS;
import com.rameses.interfaces.AsyncResponseServiceLocal;
import com.rameses.interfaces.ScriptServiceLocal;
import com.rameses.invoker.client.HttpClientManager;
import com.rameses.invoker.client.HttpInvokerClient;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class RemoteDelegate {
    
    
    private RemoteDelegate() {
    }
    
    public static RemoteScriptService getScriptService(String hostKey, Map env) {
        HttpInvokerClient hc = HttpClientManager.getInstance().getService(hostKey,env);
        return new RemoteScriptService(hc);
    }
    
    public static RemoteResponseService getResponseService(String hostKey, Map env) {
        HttpInvokerClient hc = HttpClientManager.getInstance().getService(hostKey,env);
        return new RemoteResponseService(hc);
    }
    
    
    public static class RemoteScriptService implements ScriptServiceLocal {
        private HttpInvokerClient client;
        public RemoteScriptService( HttpInvokerClient client ) {
            this.client = client;
        }
        public byte[] getScriptInfo(String name) {
            try {
                return (byte[]) client.invoke(CONSTANTS.SCRIPT_SERVICE+".getScriptInfo", new Object[]{name});
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
        public Object invoke(String name, String method, Object[] params, Map env) {
            try {
                return client.invoke( CONSTANTS.SCRIPT_SERVICE+".invoke", new Object[]{ name, method, params, env } );
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
    }
    
    
    public static class RemoteResponseService implements AsyncResponseServiceLocal {
        
        private HttpInvokerClient client;
        public RemoteResponseService( HttpInvokerClient client ) {
            this.client = client;
        }
        
        public void pushResponse(String requestId, Object data) {
            try {
                client.invoke( CONSTANTS.RESPONSE_SERVICE + ".pushResponse", new Object[]{ requestId, data } );
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        public Object getPollData(String requestId) {
            try {
                return client.invoke( CONSTANTS.RESPONSE_SERVICE + ".getPollData", new Object[]{requestId} );
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

    }
    
    
}
