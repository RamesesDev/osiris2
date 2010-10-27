/*
 * ResponseHandler.java
 *
 * Created on October 25, 2010, 10:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.invoker.client;

import com.rameses.common.AsyncListener;

/**
 *
 * @author ms
 */
public class ResponseHandler {
    
    private String requestId;
    private AsyncListener listener;
    private HttpScriptService scriptService;
    
    public ResponseHandler(HttpScriptService svc, String requestId, AsyncListener h) {
        this.listener = h;
        this.scriptService = svc;
        this.requestId = requestId;
    }
    
    public boolean execute() {
        int counter = 0;
        while(true) {
            Object result = scriptService.getPollData(requestId);
            if( result == null ) {
                break;
            }
            else {
                listener.onMessage( result );
                counter++;
            }
        }
        return (counter > 0);
    }
}
