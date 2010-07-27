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
import com.rameses.interfaces.ResponseServiceLocal;
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
    
    
    public static class RemoteResponseService implements ResponseServiceLocal {
        
        private HttpInvokerClient client;
        public RemoteResponseService( HttpInvokerClient client ) {
            this.client = client;
        }
        
        public void registerData(String requestId, Object data) {
            try {
                client.invoke( CONSTANTS.RESPONSE_SERVICE + ".registerData", new Object[]{ requestId, data } );
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        public void removeStaleObjects() {
            try {
                client.invoke( CONSTANTS.RESPONSE_SERVICE + ".removeStaleObjects", new Object[]{} );
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        public Object getResponseData(String requestId) {
            try {
                return client.invoke( CONSTANTS.RESPONSE_SERVICE + ".getResponseData", new Object[]{requestId} );
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
        
    }
    
    
}
