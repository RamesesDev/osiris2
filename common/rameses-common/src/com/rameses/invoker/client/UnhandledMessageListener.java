/*
 * UnhandledMessageListener.java
 *
 * Created on October 25, 2010, 10:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.invoker.client;

import com.rameses.common.AsyncHandler;
import com.rameses.common.AsyncResult;

/**
 *
 * @author ms
 */
public class UnhandledMessageListener implements AsyncHandler {
    
    public void onMessage(AsyncResult o) {
        System.out.println("unhandled message " + o);
    }
}
