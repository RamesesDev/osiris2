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
import com.rameses.common.AsyncResult;
import com.rameses.util.BreakException;
import com.rameses.util.ExceptionManager;

/**
 *
 * @author ms
 */
public class ResponseHandler {
    
    private static final long serialVersionUID = 1L;
    
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
        try {
            while(true) {
                Object result = scriptService.getPollData(requestId);
                if( result == null ) {
                    break;
                } else if(result instanceof BreakException ) {
                    throw (BreakException) result;
                } else if(result instanceof Exception ) {
                    throw ExceptionManager.getOriginal((Exception)result);
                } else {
                    listener.onMessage( new AsyncResult( result, AsyncResult.PROCESSING ) );
                    counter++;
                }
            }
        } catch(BreakException be) {
            listener.onMessage( new AsyncResult(null, AsyncResult.COMPLETED) );
            throw be;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return (counter > 0);
    }
}
