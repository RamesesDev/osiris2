/*
 * ResponseHandler.java
 *
 * Created on October 25, 2010, 10:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.invoker.client;

import com.rameses.common.AsyncHandler;
import com.rameses.common.AsyncResponse;

/**
 *
 * @author ms
 */
public class ResponseHandler {
    
    private String requestId;
    private AsyncHandler listener;
    private HttpScriptService scriptService;
    private AsyncResponse response;
    
    public ResponseHandler(HttpScriptService svc, AsyncResponse response, AsyncHandler h) {
        this.listener = h;
        this.scriptService = svc;
        this.response = response;
        this.requestId = (String)response.get("id");
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
